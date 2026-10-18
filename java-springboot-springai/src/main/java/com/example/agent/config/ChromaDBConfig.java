package com.example.agent.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.mistral.MistralEmbeddingModel;
import org.springframework.ai.vectorstore.ChromaVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChromaDBConfig {

    @Value("${chromadb.host:localhost}")
    private String chromaHost;

    @Value("${chromadb.port:8000}")
    private int chromaPort;

    @Value("${chromadb.collection.name:agentic_knowledge}")
    private String collectionName;

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return new ChromaVectorStore(
            chromaHost,
            chromaPort,
            collectionName,
            embeddingModel
        );
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return new MistralEmbeddingModel(
            System.getenv("MISTRAL_API_KEY")
        );
    }
}
