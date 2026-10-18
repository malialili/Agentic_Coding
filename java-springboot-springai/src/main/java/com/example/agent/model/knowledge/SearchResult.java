package com.example.agent.model.knowledge;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * DTO pour les résultats de recherche dans la base de connaissances.
 * 
 * @author Vibe Code (Mistral AI)
 */
@Data
public class SearchResult implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Liste des documents trouvés.
     */
    private List<DocumentDTO> documents;
    
    /**
     * Score de similarité pour chaque document (pour la recherche sémantique).
     */
    private List<Double> scores;
    
    /**
     * Nombre total de résultats.
     */
    private long totalCount;
    
    /**
     * Temps d'exécution de la recherche.
     */
    private long executionTimeMs;
}
