package com.example.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Classe principale de l'application Spring Boot pour l'agent IA.
 * 
 * Cette application démarre un serveur Spring Boot avec une API REST
 * pour interagir avec un agent IA capable de :
 * - Raisonner (Chain-of-Thought)
 * - Utiliser des outils externes (recherche web, analyse de données)
 * - Mémoriser les conversations
 * 
 * @author Vibe Code (Mistral AI)
 * @version 1.0.0
 */
@SpringBootApplication
@EnableFeignClients
public class AgenticAiApplication {

    /**
     * Point d'entrée de l'application.
     * 
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        SpringApplication.run(AgenticAiApplication.class, args);
    }

    /**
     * Configuration du WebClient pour les appels HTTP réactifs.
     * 
     * @param builder Le builder de WebClient injecté par Spring.
     * @return Une instance de WebClient configurée.
     */
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
}
