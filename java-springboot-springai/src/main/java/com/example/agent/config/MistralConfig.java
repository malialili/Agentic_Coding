package com.example.agent.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mistral.chat.MistralChatModel;
import org.springframework.ai.mistral.chat.api.MistralApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Configuration pour Mistral AI avec Spring AI.
 * 
 * Cette classe configure le ChatModel pour Mistral AI en utilisant
 * les propriétés définies dans application.yml.
 * 
 * @author Vibe Code (Mistral AI)
 */
@Configuration
public class MistralConfig {
    
    @Value("${spring.ai.mistral.api-key}")
    private String apiKey;
    
    @Value("${spring.ai.mistral.base-url}")
    private String baseUrl;
    
    /**
     * Crée un RestClient pour les appels à l'API Mistral.
     * 
     * @return RestClient configuré avec l'API key.
     */
    @Bean
    public RestClient mistralRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }
    
    /**
     * Crée un MistralApi pour interagir avec l'API Mistral.
     * 
     * @param restClient Le RestClient configuré.
     * @return MistralApi prêt à l'emploi.
     */
    @Bean
    public MistralApi mistralApi(RestClient restClient) {
        return new MistralApi(restClient);
    }
    
    /**
     * Crée un ChatModel pour Mistral AI.
     * 
     * Ce ChatModel est injecté automatiquement dans les services
     * qui en ont besoin (comme MistralTool).
     * 
     * @param mistralApi L'API Mistral configurée.
     * @return ChatModel pour Mistral AI.
     */
    @Bean
    public ChatModel mistralChatModel(MistralApi mistralApi) {
        return new MistralChatModel(mistralApi);
    }
}
