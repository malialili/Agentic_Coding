package com.example.agent.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO (Data Transfer Object) pour les réponses de l'agent IA.
 * 
 * @author Vibe Code (Mistral AI)
 */
@Data
public class ResponseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * La réponse générée par l'agent IA.
     */
    private String answer;
    
    /**
     * Liste des sources utilisées pour générer la réponse.
     * Peut contenir des URLs ou des références.
     */
    private List<String> sources;
    
    /**
     * ID de session pour maintenir le contexte de la conversation.
     */
    private String sessionId;
    
    /**
     * Horodatage de la réponse.
     */
    private LocalDateTime timestamp;
    
    /**
     * Indique si la réponse a été générée avec succès.
     */
    private boolean success = true;
    
    /**
     * Message d'erreur en cas d'échec.
     */
    private String errorMessage;
    
    /**
     * Métadonnées supplémentaires (ex: temps de traitement, tokens utilisés).
     */
    private Object metadata;
    
    /**
     * Constructeur par défaut.
     */
    public ResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Constructeur avec réponse et session.
     * 
     * @param answer La réponse générée.
     * @param sessionId L'ID de session.
     */
    public ResponseDTO(String answer, String sessionId) {
        this.answer = answer;
        this.sessionId = sessionId;
        this.timestamp = LocalDateTime.now();
    }
}
