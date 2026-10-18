package com.example.agent.model.knowledge;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour les documents de la base de connaissances.
 * Utilisé pour les requêtes et réponses de l'API REST.
 * 
 * @author Vibe Code (Mistral AI)
 */
@Data
public class DocumentDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String title;
    private String content;
    private String category;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String metadata;
    
    /**
     * Convertit une entité Document en DTO.
     * 
     * @param document L'entité Document.
     * @return Le DTO correspondant.
     */
    public static DocumentDTO fromEntity(Document document) {
        if (document == null) {
            return null;
        }
        
        DocumentDTO dto = new DocumentDTO();
        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setContent(document.getContent());
        dto.setCategory(document.getCategory());
        dto.setTags(document.getTags());
        dto.setCreatedAt(document.getCreatedAt());
        dto.setUpdatedAt(document.getUpdatedAt());
        dto.setMetadata(document.getMetadata());
        return dto;
    }
    
    /**
     * Convertit un DTO en entité Document.
     * 
     * @return L'entité Document correspondante.
     */
    public Document toEntity() {
        Document document = new Document();
        document.setId(this.id);
        document.setTitle(this.title);
        document.setContent(this.content);
        document.setCategory(this.category);
        document.setTags(this.tags);
        document.setMetadata(this.metadata);
        return document;
    }
}
