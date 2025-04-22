package com.arthur.translatormanager.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    // Arthur Bonow - 21-04-2025
    // Adicionada Configuração do Swagger para a documentação da API
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Translator Manager API")
                        .version("1.0")
                        .description("Documentação da API do Translator Manager"));
    }
}