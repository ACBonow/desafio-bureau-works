package com.arthur.translatormanager.controller;

import com.arthur.translatormanager.dto.DocumentDTO;

import com.arthur.translatormanager.model.Document;
import com.arthur.translatormanager.service.DocumentService;
import com.arthur.translatormanager.service.OpenAIService;
import com.arthur.translatormanager.service.TranslatorService;
import com.arthur.translatormanager.utils.Conversor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:8081")
public class DocumentController {

    // Arthur Bonow - 21-04-2025
    // Português: Controllador para gerenciar documentos
    // English: Document management controller
    private final DocumentService documentService;
    private final TranslatorService translatorService;
    private final OpenAIService openAIService;
    private final Conversor conversor;

    public DocumentController(DocumentService documentService,
                              TranslatorService translatorService,
                              OpenAIService openAIService,
                              Conversor conversor) {
        this.documentService = documentService;
        this.translatorService = translatorService;
        this.openAIService = openAIService;
        this.conversor = conversor;
    }

    // Arthur Bonow - 21-04-2025
    // Português: endpoint para criar um novo documento
    // English: endpoint to create a new document
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> create(@RequestBody Document document) {
        try {

            // Arthur Bonow - 21-04-2025
            // Portugês: se não tiver idioma definido, classifica o idioma do conteúdo usando OpenAI
            // English: if no language is set, classify the language of the content using OpenAI
                if (document.getLocale() == null || document.getLocale().isEmpty()) {
                    String classifiedLocale = openAIService.classifyLocale(document.getContent());
                    System.out.println("Classified Locale: " + classifiedLocale);
                    document.setLocale(conversor.normalizeLanguage(classifiedLocale));
                } else {
                // Arthur Bonow - 21-04-2025
                // Português: se tiver idioma, padroniza o formato da String para o padrão ISO
                // English: if it has language, standardizes the String format to ISO standard
                    document.setLocale(conversor.normalizeLanguage(document.getLocale()));
                }
            // Arthur Bonow - 21-04-2025
            // Português: busca um Tradutor aleatório idioma de origem e setta no documento
            // English: search for random Translator by source language and set it in the document
            document.setTranslator(translatorService.findRandomTranslatorBySourceLanguage(document.getLocale()));

            Document savedDocument = documentService.save(document);
            return ResponseEntity.ok(savedDocument);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving the document, please " +
                    "check if you have a translator for this language");
        }
    }

    // Arthur Bonow - 21-04-2025
    // Português: endpoint para obter todos documentos
    // English: endpoint to get all documents
    @GetMapping
    public ResponseEntity<Page<DocumentDTO>> listAll(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentDTO> documents = documentService.getAll(pageable);
        return ResponseEntity.ok(documents);
    }

    // Arthur Bonow - 21-04-2025
    // Português: endpoint para obter um documento por ID
    // English: endpoint to get a document by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<Document> document = documentService.getById(id);
        return document.map(doc -> ResponseEntity.ok((Object) doc))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found"));
    }

    // Arthur Bonow - 21-04-2025
    // Português: endpoint para atualizar um documento
    // English: endpoint to update a document
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Document document) {
        try {
            Document updatedDocument = documentService.update(id, document);
            return ResponseEntity.ok(updatedDocument);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Arthur Bonow - 21-04-2025
    // Português: endpoint para deletar um documento por ID
    // English: endpoint to delete a document by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            documentService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Arthur Bonow - 21-04-2025
    // Português: endpoint para carregar documentos a partir de um arquivo CSV
    // English: endpoint to load documents from a CSV file
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadCsv(@RequestParam("file") MultipartFile file) {
        List<Document> documents = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            String expectedHeader = "subject;content;locale;author";

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    if (!line.equals(expectedHeader)) {
                        throw new IllegalArgumentException("Invalid CSV header. Expected: " + expectedHeader);
                    }
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length != 4) {
                    throw new IllegalArgumentException("Invalid format in CSV file");
                }

                Document document = new Document();
                document.setSubject(fields[0]);
                document.setContent(fields[1]);
                document.setLocale(conversor.normalizeLanguage(fields[2]));

                if (fields[2] == null || fields[2].isEmpty()) {
                    String classifiedLocale = openAIService.classifyLocale(document.getContent());
                    document.setLocale(conversor.normalizeLanguage(classifiedLocale));
                }

                document.setAuthor(fields[3]);
                document.setTranslator(translatorService.findRandomTranslatorBySourceLanguage(document.getLocale()));

                if (document.getTranslator() == null) {
                    throw new Exception("Translator not found for the specified source language: " + document.getLocale());
                }

                documents.add(documentService.save(document));
            }
            return ResponseEntity.ok(documents);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace(); // Log para identificar o problema
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace(); // Log para identificar problemas de leitura
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the CSV file");
        } catch (Exception ex) {
            ex.printStackTrace(); // Log para capturar erros inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred" + ex.getMessage());
        }
    }
}