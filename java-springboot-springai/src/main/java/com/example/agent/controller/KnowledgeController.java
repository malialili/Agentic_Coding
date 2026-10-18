package com.example.agent.controller;

import com.example.agent.model.knowledge.DocumentDTO;
import com.example.agent.model.knowledge.SearchRequest;
import com.example.agent.model.knowledge.SearchResult;
import com.example.agent.service.knowledge.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la base de connaissances.
 * 
 * Ce contrôleur expose les endpoints pour gérer les documents de la base de connaissances.
 * 
 * @author Vibe Code (Mistral AI)
 */
@RestController
@RequestMapping("/knowledge")
@Tag(name = "Base de Connaissances", description = "API pour gérer la base de connaissances")
public class KnowledgeController {
    
    private final DocumentService documentService;
    
    /**
     * Constructeur du KnowledgeController.
     * 
     * @param documentService Le service de gestion des documents.
     */
    public KnowledgeController(DocumentService documentService) {
        this.documentService = documentService;
    }
    
    // ============================================================================
    // CRUD Operations
    // ============================================================================
    
    /**
     * Récupère tous les documents.
     * 
     * @return La liste de tous les documents.
     */
    @GetMapping("/documents")
    @Operation(summary = "Récupère tous les documents")
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        List<DocumentDTO> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }
    
    /**
     * Récupère un document par son ID.
     * 
     * @param id L'ID du document.
     * @return Le document trouvé.
     */
    @GetMapping("/documents/{id}")
    @Operation(summary = "Récupère un document par ID")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable String id) {
        DocumentDTO document = documentService.getDocumentById(id);
        return ResponseEntity.ok(document);
    }
    
    /**
     * Crée un nouveau document.
     * 
     * @param documentDTO Le document à créer.
     * @return Le document créé.
     */
    @PostMapping("/documents")
    @Operation(summary = "Crée un nouveau document")
    public ResponseEntity<DocumentDTO> createDocument(@RequestBody DocumentDTO documentDTO) {
        DocumentDTO createdDocument = documentService.createDocument(documentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDocument);
    }
    
    /**
     * Met à jour un document existant.
     * 
     * @param id L'ID du document à mettre à jour.
     * @param documentDTO Les données mises à jour.
     * @return Le document mis à jour.
     */
    @PutMapping("/documents/{id}")
    @Operation(summary = "Met à jour un document")
    public ResponseEntity<DocumentDTO> updateDocument(
            @PathVariable String id, 
            @RequestBody DocumentDTO documentDTO) {
        DocumentDTO updatedDocument = documentService.updateDocument(id, documentDTO);
        return ResponseEntity.ok(updatedDocument);
    }
    
    /**
     * Supprime un document.
     * 
     * @param id L'ID du document à supprimer.
     * @return Confirmation de suppression.
     */
    @DeleteMapping("/documents/{id}")
    @Operation(summary = "Supprime un document")
    public ResponseEntity<Map<String, String>> deleteDocument(@PathVariable String id) {
        documentService.deleteDocument(id);
        Map<String, String> response = Map.of("message", "Document supprimé avec succès");
        return ResponseEntity.ok(response);
    }
    
    // ============================================================================
    // Recherche
    // ============================================================================
    
    /**
     * Effectue une recherche dans les documents.
     * 
     * @param searchRequest La requête de recherche.
     * @return Les résultats de la recherche.
     */
    @PostMapping("/search")
    @Operation(summary = "Effectue une recherche dans les documents")
    public ResponseEntity<SearchResult> searchDocuments(@RequestBody SearchRequest searchRequest) {
        SearchResult result = documentService.searchDocuments(searchRequest);
        return ResponseEntity.ok(result);
    }
    
    /**
     * Effectue une recherche simple (GET).
     * 
     * @param q La requête de recherche.
     * @param category La catégorie à filtrer (optionnel).
     * @param limit Le nombre maximum de résultats (optionnel).
     * @return Les résultats de la recherche.
     */
    @GetMapping("/search")
    @Operation(summary = "Effectue une recherche simple")
    public ResponseEntity<SearchResult> simpleSearch(
            @RequestParam String q,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "10") int limit) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQuery(q);
        searchRequest.setCategory(category);
        searchRequest.setLimit(limit);
        
        SearchResult result = documentService.searchDocuments(searchRequest);
        return ResponseEntity.ok(result);
    }
    
    // ============================================================================
    // Catégories
    // ============================================================================
    
    /**
     * Récupère la liste des catégories disponibles.
     * 
     * @return La liste des catégories.
     */
    @GetMapping("/categories")
    @Operation(summary = "Récupère la liste des catégories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = documentService.getCategories();
        return ResponseEntity.ok(categories);
    }
    
    // ============================================================================
    // Import/Export
    // ============================================================================
    
    /**
     * Importe des documents depuis un fichier.
     * 
     * @param file Le fichier à importer.
     * @return Le résultat de l'import.
     */
    @PostMapping("/import")
    @Operation(summary = "Importe des documents depuis un fichier")
    public ResponseEntity<Map<String, Object>> importDocuments(@RequestParam("file") MultipartFile file) {
        try {
            int count = documentService.importDocuments(file);
            Map<String, Object> response = Map.of(
                "message", "Import réussi",
                "documentsImported", count
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                "error", "Erreur lors de l'import",
                "message", e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    /**
     * Exporte tous les documents.
     * 
     * @return La liste de tous les documents.
     */
    @GetMapping("/export")
    @Operation(summary = "Exporte tous les documents")
    public ResponseEntity<List<DocumentDTO>> exportDocuments() {
        List<DocumentDTO> documents = documentService.exportDocuments();
        return ResponseEntity.ok(documents);
    }
    
    // ============================================================================
    // Statistiques
    // ============================================================================
    
    /**
     * Récupère les statistiques sur les documents.
     * 
     * @return Les statistiques.
     */
    @GetMapping("/stats")
    @Operation(summary = "Récupère les statistiques des documents")
    public ResponseEntity<DocumentService.DocumentStatistics> getStatistics() {
        DocumentService.DocumentStatistics stats = documentService.getStatistics();
        return ResponseEntity.ok(stats);
    }
    
    // ============================================================================
    // Gestion des erreurs
    // ============================================================================
    
    /**
     * Gestionnaire d'exceptions pour les erreurs générales.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralExceptions(Exception ex) {
        Map<String, String> errorResponse = Map.of(
            "error", "Erreur interne du serveur",
            "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
