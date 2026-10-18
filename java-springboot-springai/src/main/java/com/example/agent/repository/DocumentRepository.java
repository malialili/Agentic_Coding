package com.example.agent.repository;

import com.example.agent.model.knowledge.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des documents de la base de connaissances.
 * 
 * @author Vibe Code (Mistral AI)
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, String>, JpaSpecificationExecutor<Document> {
    
    /**
     * Trouve les documents par titre (recherche insensible à la casse).
     * 
     * @param title Le titre à rechercher.
     * @return La liste des documents correspondants.
     */
    List<Document> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Trouve les documents par catégorie.
     * 
     * @param category La catégorie à filtrer.
     * @return La liste des documents de cette catégorie.
     */
    List<Document> findByCategory(String category);
    
    /**
     * Trouve les documents par tag.
     * 
     * @param tag Le tag à rechercher.
     * @return La liste des documents contenant ce tag.
     */
    List<Document> findByTagsContaining(String tag);
    
    /**
     * Trouve les documents dont le contenu contient le texte spécifié.
     * 
     * @param content Le texte à rechercher dans le contenu.
     * @return La liste des documents correspondants.
     */
    List<Document> findByContentContainingIgnoreCase(String content);
    
    /**
     * Trouve les documents par titre ou contenu.
     * 
     * @param query Le texte à rechercher.
     * @return La liste des documents correspondants.
     */
    @Query("SELECT d FROM Document d WHERE LOWER(d.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(d.content) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Document> searchByTitleOrContent(@org.springframework.data.repository.query.Param("query") String query);
    
    /**
     * Trouve les documents par catégorie et texte de recherche.
     * 
     * @param category La catégorie à filtrer.
     * @param query Le texte à rechercher.
     * @return La liste des documents correspondants.
     */
    @Query("SELECT d FROM Document d WHERE d.category = :category AND (LOWER(d.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(d.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Document> searchByCategoryAndQuery(
        @org.springframework.data.repository.query.Param("category") String category,
        @org.springframework.data.repository.query.Param("query") String query
    );
    
    /**
     * Trouve les documents les plus récents.
     * 
     * @param limit Le nombre maximum de documents à retourner.
     * @return La liste des documents les plus récents.
     */
    List<Document> findTopByOrderByCreatedAtDesc(int limit);
    
    /**
     * Compte le nombre total de documents.
     * 
     * @return Le nombre total de documents.
     */
    long count();
    
    /**
     * Trouve les documents par ordre alphabétique de titre.
     * 
     * @return La liste des documents triés par titre.
     */
    List<Document> findAllByOrderByTitleAsc();
}
