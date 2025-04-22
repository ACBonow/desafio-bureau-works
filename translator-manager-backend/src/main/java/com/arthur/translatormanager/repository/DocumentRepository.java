package com.arthur.translatormanager.repository;

import com.arthur.translatormanager.model.Document;
import com.arthur.translatormanager.model.Translator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    // Arthur Bonow - 21-04-2025
    // Português: Repositório JPA para gerenciar documentos, responsável pelo acesso e realização de operações no banco
    // de dados.
    // English: JPA repository to manage documents, responsible for accessing and performing operations on the database.

    List<Document> findByTranslator(Translator translator);

}
