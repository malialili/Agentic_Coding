package com.example.agent.service;

import com.example.agent.model.RequestDTO;
import com.example.agent.model.ResponseDTO;
import com.example.agent.tool.Tool;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service principal pour l'agent IA utilisant Spring AI.
 * 
 * Ce service gère la logique métier de l'agent, y compris :
 * - La réception des requêtes utilisateur
 * - L'exécution des outils appropriés (y compris Spring AI)
 * - La génération des réponses
 * - La gestion de la mémoire conversationnelle
 * 
 * @author Vibe Code (Mistral AI)
 */
@Service
public class AgentService {
    
    private final List<Tool> tools;
    private final ChatModel chatModel; // ChatModel de Spring AI pour Mistral
    private final Map<String, String> memory;  // Mémoire simple en mémoire
    
    // Expression régulière pour extraire les appels d'outils
    private static final Pattern TOOL_CALL_PATTERN = Pattern.compile("<tool>(.*?)</tool>");
    
    /**
     * Constructeur du AgentService.
     * 
     * @param tools La liste des outils disponibles pour l'agent.
     * @param chatModel Le ChatModel de Spring AI pour Mistral.
     */
    @Autowired
    public AgentService(List<Tool> tools, ChatModel chatModel) {
        this.tools = tools;
        this.chatModel = chatModel;
        this.memory = new HashMap<>();
    }
    
    /**
     * Traite une requête utilisateur et retourne une réponse.
     * 
     * @param request La requête utilisateur.
     * @return La réponse de l'agent.
     */
    public ResponseDTO processRequest(RequestDTO request) {
        try {
            // 1. Charger le contexte de la session
            String sessionId = request.getSessionId();
            if (sessionId == null) {
                sessionId = java.util.UUID.randomUUID().toString();
            }
            String context = memory.getOrDefault(sessionId, "");
            
            // 2. Construire le prompt pour le LLM
            String prompt = buildPrompt(request.getQuestion(), context);
            
            // 3. Appeler le LLM (Mistral via Spring AI)
            String llmResponse = chatModel.call(new Prompt(prompt)).getContent();
            
            // 4. Extraire les appels d'outils
            List<String> toolCalls = extractToolCalls(llmResponse);
            String finalAnswer = llmResponse;
            List<String> sources = new ArrayList<>();
            
            // 5. Exécuter les outils demandés
            for (String toolCall : toolCalls) {
                String[] parts = toolCall.split(":", 2);
                String toolName = parts[0].trim();
                String toolInput = parts.length > 1 ? parts[1].trim() : "";
                
                Tool tool = findToolByName(toolName);
                if (tool != null) {
                    try {
                        String toolOutput = tool.execute(toolInput);
                        finalAnswer = finalAnswer.replace("<tool>" + toolCall + "</tool>", toolOutput);
                        
                        // Ajouter la source si c'est un outil de recherche
                        if (toolName.equals("web_search")) {
                            sources.add("Tavily Search: " + toolInput);
                        }
                    } catch (Exception e) {
                        finalAnswer = finalAnswer.replace("<tool>" + toolCall + "</tool>", 
                                "[Erreur lors de l'exécution de l'outil " + toolName + ": " + e.getMessage() + "]");
                    }
                } else {
                    finalAnswer = finalAnswer.replace("<tool>" + toolCall + "</tool>", 
                            "[Outil non trouvé: " + toolName + "]");
                }
            }
            
            // 6. Mettre à jour la mémoire
            String newContext = context + 
                    "\nUtilisateur: " + request.getQuestion() + 
                    "\nAgent: " + finalAnswer + 
                    "\n";
            memory.put(sessionId, newContext);
            
            // 7. Retourner la réponse
            ResponseDTO response = new ResponseDTO();
            response.setAnswer(finalAnswer);
            response.setSessionId(sessionId);
            response.setSources(sources);
            response.setSuccess(true);
            
            // Ajouter des métadonnées
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("model", "Mistral AI (via Spring AI)");
            metadata.put("toolsUsed", toolCalls);
            response.setMetadata(metadata);
            
            return response;
            
        } catch (Exception e) {
            ResponseDTO errorResponse = new ResponseDTO();
            errorResponse.setSuccess(false);
            errorResponse.setErrorMessage("Erreur lors du traitement de la requête: " + e.getMessage());
            errorResponse.setTimestamp(LocalDateTime.now());
            return errorResponse;
        }
    }
    
    /**
     * Construire le prompt pour le LLM.
     * 
     * @param question La question de l'utilisateur.
     * @param context Le contexte de la conversation.
     * @return Le prompt complet.
     */
    private String buildPrompt(String question, String context) {
        return """
            Tu es un assistant IA avancé nommé AgenticAI. Utilise les outils disponibles pour répondre précisément.
            
            **Contexte précédent:**
            %s
            
            **Outils disponibles:**
            %s
            
            **Question:** %s
            
            **Instructions:**
            - Réponds en français.
            - Si tu as besoin d'informations externes, utilise <tool>web_search:ta requête</tool>.
            - Pour analyser des données, utilise <tool>analyze_data:ton texte</tool>.
            - Structure ta réponse de manière claire et organisée.
            - Cite toujours tes sources.
            """.formatted(
                context.isEmpty() ? "Aucun contexte précédent." : context,
                tools.stream()
                        .map(t -> "- " + t.getName() + ": " + t.getDescription())
                        .collect(Collectors.joining("\n")),
                question
            );
    }
    
    /**
     * Extrait les appels d'outils d'une chaîne de caractères.
     * 
     * @param text Le texte contenant les appels d'outils.
     * @return Une liste des appels d'outils.
     */
    private List<String> extractToolCalls(String text) {
        Matcher matcher = TOOL_CALL_PATTERN.matcher(text);
        List<String> toolCalls = new ArrayList<>();
        
        while (matcher.find()) {
            toolCalls.add(matcher.group(1));
        }
        
        return toolCalls;
    }
    
    /**
     * Trouve un outil par son nom.
     * 
     * @param name Le nom de l'outil.
     * @return L'outil correspondant, ou null s'il n'est pas trouvé.
     */
    private Tool findToolByName(String name) {
        return tools.stream()
                .filter(tool -> tool.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Efface la mémoire pour une session donnée.
     * 
     * @param sessionId L'ID de la session à effacer.
     */
    public void clearSessionMemory(String sessionId) {
        memory.remove(sessionId);
    }
    
    /**
     * Efface toute la mémoire.
     */
    public void clearAllMemory() {
        memory.clear();
    }
    
    /**
     * Retourne le nombre de sessions en mémoire.
     * 
     * @return Le nombre de sessions.
     */
    public int getSessionCount() {
        return memory.size();
    }
}
