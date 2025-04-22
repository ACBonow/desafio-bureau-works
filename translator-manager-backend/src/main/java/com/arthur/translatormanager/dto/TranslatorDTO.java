package com.arthur.translatormanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TranslatorDTO {
    // Arthur Bonow - 21-04-2025
    // Português: Usado para converter o tradutor para o formato correto para exibição no frontend
    // English: Used to convert the translator to the correct format for display on the frontend
    private Long id;
    private String name;
    private String email;
    private String sourceLanguage;
    private String targetLanguage;
}