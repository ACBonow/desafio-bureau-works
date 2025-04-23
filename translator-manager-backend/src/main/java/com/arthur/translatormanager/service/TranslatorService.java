package com.arthur.translatormanager.service;

import com.arthur.translatormanager.model.Translator;
import com.arthur.translatormanager.repository.TranslatorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TranslatorService {
    // Arthur Bonow - 21-04-2025
    // Português: Service para gerenciar tradutores, responsável pela logica de negocio
    // English: repository to manage translators, responsible for business logic

    private final TranslatorRepository translatorRepository;

    public TranslatorService(TranslatorRepository translatorRepository) {
        this.translatorRepository = translatorRepository;
    }

    public Translator save(Translator translator) {
        return translatorRepository.save(translator);
    }

    public Page<Translator> getAll(Pageable pageable) {
        return translatorRepository.findAll(pageable);
    }

    public Optional<Translator> getById(Long id) {
        return translatorRepository.findById(id);
    }

    public Translator update(Long id, Translator updatedTranslator) {
        return translatorRepository.findById(id)
                .map(existingTranslator -> {
                    existingTranslator.setName(updatedTranslator.getName());
                    existingTranslator.setEmail(updatedTranslator.getEmail());
                    existingTranslator.setSourceLanguage(updatedTranslator.getSourceLanguage());
                    existingTranslator.setTargetLanguage(updatedTranslator.getTargetLanguage());
                    return translatorRepository.save(existingTranslator);
                }).orElseThrow(() -> new RuntimeException("Translator not found"));
    }

    public void delete(Long id) {
        translatorRepository.deleteById(id);
    }

    public Translator findRandomTranslatorBySourceLanguage( String sourceLanguage) {
        return translatorRepository.findRandomTranslatorBySourceLanguage( sourceLanguage);
    }

    public List<Translator> findTranslatorsByNameOrSouceLanguage(String search) {
        return translatorRepository.findByNameContainingIgnoreCaseOrSourceLanguageContainingIgnoreCase(search, search);
    }
}