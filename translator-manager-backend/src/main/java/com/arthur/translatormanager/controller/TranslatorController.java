package com.arthur.translatormanager.controller;

import com.arthur.translatormanager.model.Document;
import com.arthur.translatormanager.model.Translator;
import com.arthur.translatormanager.service.DocumentService;
import com.arthur.translatormanager.service.TranslatorService;
import com.arthur.translatormanager.utils.Conversor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/translators")
@CrossOrigin(origins = "http://localhost:8081")
public class TranslatorController {
    // Arthur Bonow - 21-04-2025
    // Português: Controllador para gerenciar tradutores
    // English: Translator management controller
    private final TranslatorService translatorService;
    private final Conversor conversor;
    private final DocumentService documentService;

    public TranslatorController(TranslatorService translatorService, Conversor conversor, DocumentService documentService) {
        this.translatorService = translatorService;
        this.conversor = conversor;
        this.documentService = documentService;
    }

    // Arthur Bonow - 21-04-2025
    // Português:  endpoint para criar um novo tradutor
    // English: endpoint to create a new translator
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Translator translator) {
        try {
            translator.setTargetLanguage(conversor.normalizeLanguage(translator.getTargetLanguage()));
            translator.setSourceLanguage(conversor.normalizeLanguage(translator.getSourceLanguage()));
            Translator savedTranslator = translatorService.save(translator);
            return ResponseEntity.ok(savedTranslator);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating translator");
        }
    }

    // Arthur Bonow - 21-04-2025
    // Português: endpoint para listar todos os tradutores
    // English: endpoint to list all translators
    @GetMapping
    public ResponseEntity<Page<Translator>> listAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Translator> translators = translatorService.getAll(pageable);
        return ResponseEntity.ok(translators);
    }

    // Arthur Bonow - 21-04-2025
    // Português: endpoint para listar tradutores por id
    // English: endpoint to list translators by id
   @GetMapping("/{id}")
   public ResponseEntity<Object> getById(@PathVariable Long id) {
       Optional<Translator> translator = translatorService.getById(id);
       return translator.map(t -> ResponseEntity.ok((Object) t))
               .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Translator not found"));
   }

    // Arthur Bonow - 21-04-2025
    // Português: endpoint para atualizar tradutores
    // English: endpoint to update translators
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Translator translator) {
        try {
            Translator updatedTranslator = translatorService.update(id, translator);
            return ResponseEntity.ok(updatedTranslator);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Arthur Bonow - 21-04-2025
    // Português: endpoint para deletar tradutores
    // English: endpoint to delete translators
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            Translator translator = translatorService.getById(id)
                    .orElseThrow(() -> new RuntimeException("Translator not found"));
            if (translator.getDocuments().size() > 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Translator has documents associated");
            }
            translatorService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

   @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   public ResponseEntity<Object> uploadCsv(@RequestParam("file") MultipartFile file) {
       List<Translator> translators = new ArrayList<>();
       try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
           String line;
           boolean isFirstLine = true;
           String expectedHeader = "name;email;source_language;target_language;";

           while ((line = reader.readLine()) != null) {
               if (isFirstLine) {
                   // Verifica se a primeira linha corresponde ao cabeçalho esperado
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

               Translator translator = new Translator();
               translator.setName(fields[0]);
               translator.setEmail(fields[1]);
               translator.setSourceLanguage(conversor.normalizeLanguage(fields[2]));
               translator.setTargetLanguage(conversor.normalizeLanguage(fields[3]));

               translators.add(translatorService.save(translator));
           }
           return ResponseEntity.ok(translators);
       } catch (IllegalArgumentException ex) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
       } catch (IOException ex) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing CSV file");
       }
   }
}