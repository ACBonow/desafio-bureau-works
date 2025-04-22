package com.arthur.translatormanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {
    // Arthur Bonow - 21-04-2025
    // Português: Serviço para interagir com a OpenAI API, responsável por classificar o idioma do conteúdo através
    // de IA
    // English: Service to interact with the OpenAI API, responsible for classifying the language of the content through AI

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    public String classifyLocale(String content) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model",  "gpt-4o-mini");
        requestBody.put("store", true);
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "Você é um assistente especializado em identificar idiomas. Sempre responda apenas com a abreviação padrão do idioma, como pt-BR, es-AR, etc."),
                Map.of("role", "user", "content", "Identifique o idioma do seguinte texto: " + content)
        ));
        requestBody.put("max_tokens", 100);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    if (message != null && message.containsKey("content")) {
                        return message.get("content").toString().trim();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error identifying language with OpenAI API", e);
        }
        return "Error: Unable to identify language.";
    }
}