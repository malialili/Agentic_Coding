package com.example.agent.vectorstore;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VectorStoreService {

    private final VectorStore vectorStore;

    public VectorStoreService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void addDocument(Document document) {
        vectorStore.add(document);
    }

    public void addDocuments(List<Document> documents) {
        vectorStore.add(documents);
    }

    public List<Document> similarDocuments(String query, int topK) {
        return vectorStore.similaritySearch(query, topK);
    }

    public List<Document> similarDocuments(String query, int topK, double similarityThreshold) {
        return vectorStore.similaritySearch(query, topK, similarityThreshold);
    }

    public void deleteDocument(String documentId) {
        vectorStore.deleteById(documentId);
    }

    public void clearAll() {
        vectorStore.clear();
    }

    public List<Document> searchWithMetadata(String query, int topK, String metadataKey, String metadataValue) {
        return vectorStore.similaritySearch(
            query,
            topK,
            (doc) -> doc.getMetadata().get(metadataKey).equals(metadataValue)
        );
    }
}
