package com.arthur.translatormanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    // Arthur Bonow - 21-04-2025
    // Português: Usado para converter o documento para o formato correto para exibição no frontend
    // English: Used to convert the document to the correct format for display on the frontend
    private Long id;
    private String subject;
    private String content;
    private String locale;
    private String author;
    private TranslatorDTO translator;
}