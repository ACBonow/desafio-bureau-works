package com.arthur.translatormanager.utils;

import org.springframework.stereotype.Component;

@Component
public class Conversor {

    // Arthur Bonow - 21-04-2025
    // Português: responsável por converter o idioma para o formato correto (ISO) para exibição no frontend
    // English: responsible for converting the language to the correct (ISO) format for display on the frontend


    public String normalizeLanguage(String language) {
        if(language == null || language.isEmpty()) {
            return null;
        }
        // Arthur Bonow - 21-04-2025
        // Português: Converte para minúsculas e substitui "_" por "-"
        // English: Converts to lowercase and replaces "_" with "-"
        language = language.toLowerCase().replace("_", "-");
        // Arthur Bonow - 21-04-2025
        // Português: Divide o language em partes
        // English: Splits the language into parts
        String[] parts = language.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Formato de language inválido: " + language);
        }
        // Arthur Bonow - 21-04-2025
        // Português: Retorna no formato correto: pt-BR
        // English: Returns in the correct format: pt-BR
        return parts[0].toLowerCase() + "-" + parts[1].toUpperCase();
    }
}
