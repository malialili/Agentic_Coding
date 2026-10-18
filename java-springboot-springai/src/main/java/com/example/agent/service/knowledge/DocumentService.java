package com.example.agent.service.knowledge;

import com.example.agent.model.knowledge.Document;
import com.example.agent.model.knowledge.DocumentDTO;
import com.example.agent.model.knowledge.SearchRequest;
import com.example.agent.model.knowledge.SearchResult;
import com.example.agent.repository.DocumentRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des documents de la base de connaissances.
 * 
 * Ce service gère :
 * - CRUD des documents
 * - Recherche textuelle et sémantique
 * - Import/export de documents
 * - Génération d'embeddings pour la recherche sémantique
 * 
 * @author Vibe Code (Mistral AI)
 */
@Service
public class DocumentService {
    
    private final DocumentRepository documentRepository;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;
    
    /**
     * Constructeur du DocumentService.
     * 
     * @param documentRepository Le repository pour les documents.
     * @param embeddingModel Le modèle d'embedding pour la recherche sémantique.
     * @param vectorStore Le vector store pour stocker les embeddings.
     */
    @Autowired
    public DocumentService(
            DocumentRepository documentRepository,
            EmbeddingModel embeddingModel,
            VectorStore vectorStore) {
        this.documentRepository = documentRepository;
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
    }
    
    // ============================================================================
    // CRUD Operations
    // ============================================================================
    
    /**
     * Crée un nouveau document.
     * 
     * @param documentDTO Le DTO du document à créer.
     * @return Le document créé.
     */
    @Transactional
    public DocumentDTO createDocument(DocumentDTO documentDTO) {
        Document document = documentDTO.toEntity();
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());
        
        // Générer l'embedding si le contenu n'est pas vide
        if (document.getContent() != null && !document.getContent().isEmpty()) {
            String embedding = generateEmbedding(document.getContent());
            document.setEmbedding(embedding);
        }
        
        Document savedDocument = documentRepository.save(document);
        return DocumentDTO.fromEntity(savedDocument);
    }
    
    /**
     * Met à jour un document existant.
     * 
     * @param id L'ID du document à mettre à jour.
     * @param documentDTO Le DTO avec les données mises à jour.
     * @return Le document mis à jour.
     * @throws RuntimeException Si le document n'est pas trouvé.
     */
    @Transactional
    public DocumentDTO updateDocument(String id, DocumentDTO documentDTO) {
        Document existingDocument = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé avec l'ID: " + id));
        
        Document document = documentDTO.toEntity();
        document.setId(id);
        document.setCreatedAt(existingDocument.getCreatedAt());
        document.setUpdatedAt(LocalDateTime.now());
        
        // Régénérer l'embedding si le contenu a changé
        if (document.getContent() != null && !document.getContent().isEmpty() &&
            !document.getContent().equals(existingDocument.getContent())) {
            String embedding = generateEmbedding(document.getContent());
            document.setEmbedding(embedding);
        } else {
            // Conserver l'embedding existant
            document.setEmbedding(existingDocument.getEmbedding());
        }
        
        Document updatedDocument = documentRepository.save(document);
        return DocumentDTO.fromEntity(updatedDocument);
    }
    
    /**
     * Récupère un document par son ID.
     * 
     * @param id L'ID du document.
     * @return Le document trouvé.
     * @throws RuntimeException Si le document n'est pas trouvé.
     */
    public DocumentDTO getDocumentById(String id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé avec l'ID: " + id));
        return DocumentDTO.fromEntity(document);
    }
    
    /**
     * Récupère tous les documents.
     * 
     * @return La liste de tous les documents.
     */
    public List<DocumentDTO> getAllDocuments() {
        List<Document> documents = documentRepository.findAll();
        return documents.stream()
                .map(DocumentDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Supprime un document.
     * 
     * @param id L'ID du document à supprimer.
     * @throws RuntimeException Si le document n'est pas trouvé.
     */
    @Transactional
    public void deleteDocument(String id) {
        if (!documentRepository.existsById(id)) {
            throw new RuntimeException("Document non trouvé avec l'ID: " + id);
        }
        documentRepository.deleteById(id);
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
    public SearchResult searchDocuments(SearchRequest searchRequest) {
        long startTime = System.currentTimeMillis();
        
        List<Document> documents;
        
        if (searchRequest.isSemanticSearch() && searchRequest.getQuery() != null && !searchRequest.getQuery().isEmpty()) {
            // Recherche sémantique avec embeddings
            documents = semanticSearch(searchRequest.getQuery(), searchRequest.getLimit());
        } else {
            // Recherche textuelle classique
            documents = textSearch(searchRequest);
        }
        
        long executionTimeMs = System.currentTimeMillis() - startTime;
        
        List<DocumentDTO> documentDTOs = documents.stream()
                .map(DocumentDTO::fromEntity)
                .collect(Collectors.toList());
        
        SearchResult result = new SearchResult();
        result.setDocuments(documentDTOs);
        result.setTotalCount(documentRepository.count());
        result.setExecutionTimeMs(executionTimeMs);
        
        return result;
    }
    
    /**
     * Recherche textuelle classique.
     * 
     * @param searchRequest La requête de recherche.
     * @return La liste des documents correspondants.
     */
    private List<Document> textSearch(SearchRequest searchRequest) {
        String query = searchRequest.getQuery();
        String category = searchRequest.getCategory();
        int limit = searchRequest.getLimit();
        
        List<Document> documents;
        
        if (query != null && !query.isEmpty()) {
            if (category != null && !category.isEmpty()) {
                documents = documentRepository.searchByCategoryAndQuery(category, query);
            } else {
                documents = documentRepository.searchByTitleOrContent(query);
            }
        } else if (category != null && !category.isEmpty()) {
            documents = documentRepository.findByCategory(category);
        } else {
            documents = documentRepository.findAll();
        }
        
        // Limiter le nombre de résultats
        return documents.stream().limit(limit).collect(Collectors.toList());
    }
    
    /**
     * Recherche sémantique utilisant les embeddings.
     * 
     * @param query La requête de recherche.
     * @param limit Le nombre maximum de résultats.
     * @return La liste des documents les plus similaires.
     */
    private List<Document> semanticSearch(String query, int limit) {
        // Générer l'embedding de la requête
        String queryEmbedding = generateEmbedding(query);
        
        // Rechercher dans le vector store (simplifié ici)
        // Dans une implémentation réelle, on utiliserait vectorStore.similaritySearch()
        List<Document> allDocuments = documentRepository.findAll();
        
        // Filtrer les documents avec des embeddings
        List<Document> documentsWithEmbeddings = allDocuments.stream()
                .filter(doc -> doc.getEmbedding() != null && !doc.getEmbedding().isEmpty())
                .collect(Collectors.toList());
        
        if (documentsWithEmbeddings.isEmpty()) {
            return textSearch(new SearchRequest(query, null, limit, false));
        }
        
        // Simuler une recherche de similarité (à remplacer par une vraie implémentation)
        // Ici, on retourne simplement les documents les plus récents
        return documentRepository.findTopByOrderByCreatedAtDesc(limit);
    }
    
    // ============================================================================
    // Embeddings
    // ============================================================================
    
    /**
     * Génère un embedding pour un texte.
     * 
     * @param text Le texte à encoder.
     * @return La représentation vectorielle du texte (sérialisée en String).
     */
    private String generateEmbedding(String text) {
        try {
            if (embeddingModel != null) {
                // Générer l'embedding avec Spring AI
                List<Double> embedding = embeddingModel.embed(text);
                // Convertir en String pour le stockage
                return embedding.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","));
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération de l'embedding: " + e.getMessage());
        }
        return null;
    }
    
    // ============================================================================
    // Import/Export
    // ============================================================================
    
    /**
     * Importe des documents depuis un fichier.
     * 
     * @param file Le fichier à importer (JSON ou texte).
     * @return Le nombre de documents importés.
     * @throws IOException Si le fichier ne peut pas être lu.
     */
    @Transactional
    public int importDocuments(MultipartFile file) throws IOException {
        int count = 0;
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            StringBuilder contentBuilder = new StringBuilder();
            
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
            
            String content = contentBuilder.toString();
            
            // Analyser le contenu (format simple : titre sur la première ligne, contenu ensuite)
            String[] lines = content.split("\n", 2);
            String title = lines.length > 0 ? lines[0] : "Document sans titre";
            String documentContent = lines.length > 1 ? lines[1] : content;
            
            DocumentDTO documentDTO = new DocumentDTO();
            documentDTO.setTitle(title);
            documentDTO.setContent(documentContent);
            documentDTO.setCategory("Importé");
            documentDTO.setTags(List.of("import"));
            
            createDocument(documentDTO);
            count++;
        }
        
        return count;
    }
    
    /**
     * Exporte tous les documents.
     * 
     * @return La liste de tous les documents au format JSON.
     */
    public List<DocumentDTO> exportDocuments() {
        return getAllDocuments();
    }
    
    // ============================================================================
    // Catégories
    // ============================================================================
    
    /**
     * Récupère la liste des catégories disponibles.
     * 
     * @return La liste des catégories.
     */
    public List<String> getCategories() {
        return documentRepository.findAll()
                .stream()
                .map(Document::getCategory)
                .filter(category -> category != null && !category.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    // ============================================================================
    // Statistiques
    // ============================================================================
    
    /**
     * Récupère les statistiques sur les documents.
     * 
     * @return Les statistiques.
     */
    public DocumentStatistics getStatistics() {
        long totalDocuments = documentRepository.count();
        List<String> categories = getCategories();
        
        DocumentStatistics stats = new DocumentStatistics();
        stats.setTotalDocuments(totalDocuments);
        stats.setCategories(categories);
        stats.setCategoryCounts(new java.util.HashMap<>());
        
        for (String category : categories) {
            long count = documentRepository.findByCategory(category).size();
            stats.getCategoryCounts().put(category, count);
        }
        
        return stats;
    }
    
    /**
     * Classe pour les statistiques des documents.
     */
    public static class DocumentStatistics {
        private long totalDocuments;
        private List<String> categories;
        private java.util.Map<String, Long> categoryCounts;
        
        // Getters et Setters
        public long getTotalDocuments() { return totalDocuments; }
        public void setTotalDocuments(long totalDocuments) { this.totalDocuments = totalDocuments; }
        public List<String> getCategories() { return categories; }
        public void setCategories(List<String> categories) { this.categories = categories; }
        public java.util.Map<String, Long> getCategoryCounts() { return categoryCounts; }
        public void setCategoryCounts(java.util.Map<String, Long> categoryCounts) { this.categoryCounts = categoryCounts; }
    }
}
