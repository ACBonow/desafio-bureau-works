package com.arthur.translatormanager.service;
import com.arthur.translatormanager.dto.DocumentDTO;
import com.arthur.translatormanager.dto.TranslatorDTO;
import com.arthur.translatormanager.model.Translator;
import com.arthur.translatormanager.model.Document;
import com.arthur.translatormanager.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;


@Service
public class DocumentService {
    // Arthur Bonow - 21-04-2025
    // Português: Service para gerenciar documentos, responsável pela logica de negocio
    // English: repository to manage documents, responsible for business logic

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public Page<DocumentDTO> getAll(Pageable pageable) {
        return documentRepository.findAll(pageable)
                .map(document -> {
                    TranslatorDTO translatorDTO = null;
                    if (document.getTranslator() != null) {
                        translatorDTO = new TranslatorDTO(
                                document.getTranslator().getId(),
                                document.getTranslator().getName(),
                                document.getTranslator().getEmail(),
                                document.getTranslator().getSourceLanguage(),
                                document.getTranslator().getTargetLanguage()
                        );
                    }
                    return new DocumentDTO(
                            document.getId(),
                            document.getSubject(),
                            document.getContent(),
                            document.getLocale(),
                            document.getAuthor(),
                            translatorDTO
                    );
                });
    }

    public List<Document> getByTranslator(Translator translator) {
        return documentRepository.findByTranslator(translator);
    }

    public Optional<Document> getById(Long id) {
        return documentRepository.findById(id);
    }

    public Document update(Long id, Document updated) {
        return documentRepository.findById(id)
                .map(existingDocument -> {
                    existingDocument.setSubject(updated.getSubject());
                    existingDocument.setContent(updated.getContent());
                    existingDocument.setLocale(updated.getLocale());
                    existingDocument.setAuthor(updated.getAuthor());
                    return documentRepository.save(existingDocument);
                }).orElseThrow(() -> new RuntimeException("Document not found"));
    }


    public void delete(Long id) {
        documentRepository.deleteById(id);
    }
}