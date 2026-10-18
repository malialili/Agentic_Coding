package com.example.agent.tool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

/**
 * Outil pour interagir avec l'API Mistral AI.
 * 
 * Cet outil permet à l'agent IA d'utiliser les capacités de Mistral AI
 * pour générer du texte, répondre à des questions, etc.
 * 
 * @author Vibe Code (Mistral AI)
 */
@Component
public class MistralTool implements Tool {
    
    private final WebClient webClient;
    private final String mistralApiKey;
    private final String mistralBaseUrl;
    private final String model;
    private final double temperature;
    private final int maxTokens;
    
    /**
     * Constructeur du MistralTool.
     * 
     * @param webClient Le client HTTP réactif.
     * @param mistralApiKey La clé API pour Mistral.
     * @param mistralBaseUrl L'URL de base de l'API Mistral.
     * @param model Le modèle à utiliser.
     * @param temperature La température pour la génération.
     * @param maxTokens Le nombre maximum de tokens à générer.
     */
    public MistralTool(
            WebClient.Builder webClientBuilder,
            @Value("${app.mistral.api-key}") String mistralApiKey,
            @Value("${app.mistral.base-url}") String mistralBaseUrl,
            @Value("${app.mistral.model}") String model,
            @Value("${app.mistral.temperature}") double temperature,
            @Value("${app.mistral.max-tokens}") int maxTokens) {
        this.webClient = webClientBuilder.baseUrl(mistralBaseUrl).build();
        this.mistralApiKey = mistralApiKey;
        this.mistralBaseUrl = mistralBaseUrl;
        this.model = model;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }
    
    @Override
    public String getName() {
        return "mistral_llm";
    }
    
    @Override
    public String getDescription() {
        return "Utilise le modèle de langage Mistral AI pour générer du texte, répondre à des questions, " +
               "ou effectuer des tâches de traitement du langage naturel.";
    }
    
    @Override
    public String getParametersDescription() {
        return "prompt: String (le prompt à envoyer au modèle)";
    }
    
    @Override
    public String execute(String prompt) {
        // Construction de la requête pour l'API Mistral
        Map<String, Object> message = Map.of(
            "role", "user",
            "content", prompt
        );
        
        Map<String, Object> requestBody = Map.of(
            "model", model,
            "messages", List.of(message),
            "temperature", temperature,
            "max_tokens", maxTokens
        );
        
        try {
            // Appel à l'API Mistral
            Mono<Map> responseMono = webClient.post()
                    .uri("/chat/completions")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + mistralApiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class);
            
            Map<String, Object> response = responseMono.block();
            
            // Extraction de la réponse
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            
            if (choices == null || choices.isEmpty()) {
                return "Aucune réponse générée par Mistral AI.";
            }
            
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> messageResponse = (Map<String, Object>) firstChoice.get("message");
            String content = (String) messageResponse.get("content");
            
            return content != null ? content : "Aucun contenu dans la réponse.";
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'appel à Mistral AI: " + e.getMessage(), e);
        }
    }
}
