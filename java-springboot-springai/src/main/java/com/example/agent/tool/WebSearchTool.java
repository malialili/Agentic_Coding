package com.example.agent.tool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

/**
 * Outil de recherche web utilisant l'API Tavily.
 * 
 * Cet outil permet à l'agent IA de rechercher des informations sur le web
 * pour répondre aux questions nécessitant des données externes.
 * 
 * @author Vibe Code (Mistral AI)
 */
@Component
public class WebSearchTool implements Tool {
    
    private final WebClient webClient;
    private final String tavilyApiKey;
    private final String tavilyBaseUrl;
    private final int maxResults;
    
    /**
     * Constructeur du WebSearchTool.
     * 
     * @param webClient Le client HTTP réactif.
     * @param tavilyApiKey La clé API pour Tavily.
     * @param tavilyBaseUrl L'URL de base de l'API Tavily.
     * @param maxResults Le nombre maximum de résultats à retourner.
     */
    public WebSearchTool(
            WebClient.Builder webClientBuilder,
            @Value("${app.tavily.api-key}") String tavilyApiKey,
            @Value("${app.tavily.base-url}") String tavilyBaseUrl,
            @Value("${app.tavily.max-results}") int maxResults) {
        this.webClient = webClientBuilder.baseUrl(tavilyBaseUrl).build();
        this.tavilyApiKey = tavilyApiKey;
        this.tavilyBaseUrl = tavilyBaseUrl;
        this.maxResults = maxResults;
    }
    
    @Override
    public String getName() {
        return "web_search";
    }
    
    @Override
    public String getDescription() {
        return "Effectue une recherche web pour trouver des informations récentes. " +
               "Utilisez cet outil lorsque vous avez besoin d'informations externes ou à jour.";
    }
    
    @Override
    public String getParametersDescription() {
        return "query: String (la requête de recherche)";
    }
    
    @Override
    public String execute(String query) {
        // Construction de la requête pour l'API Tavily
        Map<String, Object> requestBody = Map.of(
            "api_key", tavilyApiKey,
            "query", query,
            "max_results", maxResults,
            "search_depth", "basic",
            "include_answer", false,
            "include_raw_content", false
        );
        
        try {
            // Appel à l'API Tavily
            Mono<Map> responseMono = webClient.post()
                    .uri("/search")
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class);
            
            Map<String, Object> response = responseMono.block();
            
            // Extraction des résultats
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            
            if (results == null || results.isEmpty()) {
                return "Aucun résultat trouvé pour la requête: " + query;
            }
            
            // Formatage des résultats
            StringBuilder formattedResults = new StringBuilder();
            formattedResults.append("Résultats de recherche pour: ").append(query).append("\n\n");
            
            for (int i = 0; i < results.size(); i++) {
                Map<String, Object> result = results.get(i);
                formattedResults.append("--- Résultat ").append(i + 1).append(" ---\n");
                formattedResults.append("Titre: ").append(result.getOrDefault("title", "Sans titre")).append("\n");
                formattedResults.append("URL: ").append(result.getOrDefault("url", "#")).append("\n");
                formattedResults.append("Contenu: ").append(result.getOrDefault("content", "")).append("\n\n");
            }
            
            return formattedResults.toString();
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche web: " + e.getMessage(), e);
        }
    }
}
