package com.example.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Classe principale de l'application Spring Boot + Spring AI pour l'agent IA.
 * 
 * Cette application démarre un serveur Spring Boot avec une API REST
 * pour interagir avec un agent IA utilisant Spring AI.
 * 
 * Spring AI simplifie l'intégration avec les modèles de langage (LLM) comme Mistral AI.
 * 
 * @author Vibe Code (Mistral AI)
 * @version 1.0.0
 * @see <a href="https://github.com/spring-projects/spring-ai">Spring AI</a>
 */
@SpringBootApplication
@EnableFeignClients
public class AgenticAiSpringAiApplication {

    /**
     * Point d'entrée de l'application.
     * 
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        SpringApplication.run(AgenticAiSpringAiApplication.class, args);
    }

    /**
     * Configuration du WebClient pour les appels HTTP réactifs.
     * Utilisé principalement pour les appels à Tavily (recherche web).
     * 
     * @param builder Le builder de WebClient injecté par Spring.
     * @return Une instance de WebClient configurée.
     */
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
}
