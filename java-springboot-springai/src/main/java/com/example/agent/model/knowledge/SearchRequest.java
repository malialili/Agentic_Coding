package com.example.agent.model.knowledge;

import lombok.Data;
import java.io.Serializable;

/**
 * DTO pour les requêtes de recherche dans la base de connaissances.
 * 
 * @author Vibe Code (Mistral AI)
 */
@Data
public class SearchRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * La requête de recherche.
     */
    private String query;
    
    /**
     * La catégorie à filtrer (optionnel).
     */
    private String category;
    
    /**
     * Le nombre maximum de résultats.
     */
    private int limit = 10;
    
    /**
     * Utiliser la recherche sémantique (basée sur les embeddings).
     */
    private boolean semanticSearch = false;
}
