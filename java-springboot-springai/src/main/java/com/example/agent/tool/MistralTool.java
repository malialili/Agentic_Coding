package com.example.agent.tool;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Outil pour interagir avec Mistral AI via Spring AI.
 * 
 * Cet outil utilise le ChatModel de Spring AI pour simplifier l'intégration
 * avec les modèles de langage Mistral.
 * 
 * @author Vibe Code (Mistral AI)
 * @see <a href="https://github.com/spring-projects/spring-ai">Spring AI</a>
 */
@Component
public class MistralTool implements Tool {
    
    private final ChatModel chatModel;
    
    /**
     * Constructeur du MistralTool.
     * 
     * @param chatModel Le ChatModel injecté par Spring AI (configuré pour Mistral).
     */
    @Autowired
    public MistralTool(ChatModel chatModel) {
        this.chatModel = chatModel;
    }
    
    @Override
    public String getName() {
        return "mistral_llm";
    }
    
    @Override
    public String getDescription() {
        return "Utilise le modèle de langage Mistral AI pour générer du texte, répondre à des questions, " +
               "ou effectuer des tâches de traitement du langage naturel. Intégré via Spring AI.";
    }
    
    @Override
    public String getParametersDescription() {
        return "prompt: String (le prompt à envoyer au modèle)";
    }
    
    @Override
    public String execute(String prompt) {
        try {
            // Utilisation du ChatModel de Spring AI
            // Spring AI gère automatiquement l'appel à l'API Mistral
            String response = chatModel.call(
                new Prompt(prompt)
            );
            
            return response;
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'appel à Mistral AI via Spring AI: " + e.getMessage(), e);
        }
    }
    
    /**
     * Méthode alternative utilisant un template de prompt.
     * 
     * @param template Le template de prompt avec des variables.
     * @param variables Les variables à substituer dans le template.
     * @return Le résultat généré par le modèle.
     */
    public String executeWithTemplate(String template, Map<String, Object> variables) {
        try {
            PromptTemplate promptTemplate = new PromptTemplate(template);
            Prompt prompt = promptTemplate.create(variables);
            return chatModel.call(prompt);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'exécution avec template: " + e.getMessage(), e);
        }
    }
}
