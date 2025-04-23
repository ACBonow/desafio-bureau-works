package com.arthur.translatormanager.repository;

import com.arthur.translatormanager.model.Translator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslatorRepository extends JpaRepository<Translator, Long> {
    // Arthur Bonow - 21-04-2025
    // Português: Repositório JPA para gerenciar tradutores, responsável pelo acesso e realização de operações no banco de dados.
    // English: JPA repository to manage translators, responsible for accessing and performing operations on the database.

    @Query(value = "SELECT * FROM translator WHERE source_language = :sourceLanguage ORDER BY RANDOM() LIMIT 1",
            nativeQuery = true)
    Translator findRandomTranslatorBySourceLanguage(@Param("sourceLanguage") String sourceLanguage);

    List<Translator> findByNameContainingIgnoreCaseOrSourceLanguageContainingIgnoreCase(String name, String sourceLanguage);

}