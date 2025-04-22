package com.arthur.translatormanager.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {
    // Arthur Bonow - 21-04-2025
    // Português: add cache para melhorar a performance da aplicação
    // English: add cache to improve application performance
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("documents", "translators");
    }
}