package com.example.agent.model.knowledge;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entité représentant un document dans la base de connaissances.
 * 
 * @author Vibe Code (Mistral AI)
 */
@Entity
@Table(name = "documents")
@Data
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    /**
     * Titre du document.
     */
    @Column(nullable = false, length = 255)
    private String title;
    
    /**
     * Contenu du document (peut contenir du Markdown).
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    /**
     * Catégorie du document.
     */
    @Column(length = 100)
    private String category;
    
    /**
     * Tags associés au document.
     */
    @ElementCollection
    @CollectionTable(name = "document_tags", joinColumns = @JoinColumn(name = "document_id"))
    @Column(name = "tag")
    private List<String> tags;
    
    /**
     * Date de création du document.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Date de mise à jour du document.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Embedding vectoriel pour la recherche sémantique (optionnel).
     * Stocke la représentation vectorielle du contenu.
     */
    @Column(columnDefinition = "TEXT")
    private String embedding;
    
    /**
     * Métadonnées supplémentaires.
     */
    @Column(columnDefinition = "TEXT")
    private String metadata;
    
    /**
     * Constructeur par défaut.
     */
    public Document() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Met à jour la date de modification.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
