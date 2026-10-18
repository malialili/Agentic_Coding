package com.example.agent.vectorstore;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DocumentEmbedder {

    private final EmbeddingModel embeddingModel;

    public DocumentEmbedder(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public Document embedDocument(String content, Map<String, String> metadata) {
        return new Document(content, metadata);
    }

    public List<Document> embedDocuments(List<String> contents, List<Map<String, String>> metadatas) {
        if (contents.size() != metadatas.size()) {
            throw new IllegalArgumentException("Contents and metadatas must have the same size");
        }
        
        return contents.stream()
            .map(content -> {
                int index = contents.indexOf(content);
                return new Document(content, metadatas.get(index));
            })
            .toList();
    }

    public List<Double> getEmbedding(String text) {
        return embeddingModel.embed(text);
    }

    public List<List<Double>> getEmbeddings(List<String> texts) {
        return embeddingModel.embed(texts);
    }
}
