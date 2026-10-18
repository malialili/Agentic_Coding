package com.example.agent.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.io.Serializable;

/**
 * DTO (Data Transfer Object) pour les requêtes envoyées à l'agent IA.
 * 
 * @author Vibe Code (Mistral AI)
 */
@Data
public class RequestDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * La question posée par l'utilisateur.
     * Ne peut pas être vide ou null.
     */
    @NotBlank(message = "La question ne peut pas être vide")
    private String question;
    
    /**
     * ID de session pour maintenir le contexte de la conversation.
     * Si null, une nouvelle session sera créée.
     */
    private String sessionId;
    
    /**
     * Paramètres optionnels pour personnaliser la réponse.
     * Exemple: {"temperature": 0.9, "max_tokens": 1000}
     */
    private Object parameters;
}
