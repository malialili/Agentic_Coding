package com.example.agent.controller;

import com.example.agent.model.RequestDTO;
import com.example.agent.model.ResponseDTO;
import com.example.agent.service.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour l'API de l'agent IA.
 * 
 * Ce contrôleur expose les endpoints pour interagir avec l'agent IA.
 * 
 * @author Vibe Code (Mistral AI)
 */
@RestController
@RequestMapping("/agent")
@Tag(name = "Agent IA", description = "API pour interagir avec l'agent IA")
public class AgentController {
    
    private final AgentService agentService;
    
    /**
     * Constructeur du AgentController.
     * 
     * @param agentService Le service de l'agent IA.
     */
    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }
    
    /**
     * Endpoint pour poser une question à l'agent IA.
     * 
     * @param request La requête contenant la question.
     * @return La réponse de l'agent.
     */
    @PostMapping("/ask")
    @Operation(
        summary = "Pose une question à l'agent IA",
        description = "Envoyez une question à l'agent IA et recevez une réponse. " +
                      "Si un sessionId est fourni, la conversation sera maintenue.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "La requête contenant la question et optionnellement un sessionId",
            required = true
        )
    )
    public ResponseEntity<ResponseDTO> askQuestion(@RequestBody RequestDTO request) {
        ResponseDTO response = agentService.processRequest(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint pour poser une question simple (sans session).
     * 
     * @param question La question à poser.
     * @return La réponse de l'agent.
     */
    @GetMapping("/ask")
    @Operation(
        summary = "Pose une question simple à l'agent IA",
        description = "Envoyez une question simple à l'agent IA sans maintenir de session."
    )
    public ResponseEntity<ResponseDTO> askSimpleQuestion(@RequestParam String question) {
        RequestDTO request = new RequestDTO();
        request.setQuestion(question);
        ResponseDTO response = agentService.processRequest(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint pour obtenir des informations sur les outils disponibles.
     * 
     * @return La liste des outils disponibles.
     */
    @GetMapping("/tools")
    @Operation(
        summary = "Liste les outils disponibles",
        description = "Retourne la liste des outils que l'agent IA peut utiliser."
    )
    public ResponseEntity<List<ToolInfo>> getAvailableTools() {
        // Dans une vraie implémentation, on injecterait la liste des outils
        // Ici, on retourne une liste statique pour l'exemple
        List<ToolInfo> tools = List.of(
            new ToolInfo("web_search", "Recherche web", "Effectue une recherche web pour trouver des informations récentes."),
            new ToolInfo("analyze_data", "Analyse de données", "Analyse un texte pour en extraire des tendances ou mots-clés."),
            new ToolInfo("mistral_llm", "Modèle de langage Mistral", "Utilise Mistral AI pour générer du texte ou répondre à des questions.")
        );
        return ResponseEntity.ok(tools);
    }
    
    /**
     * Endpoint pour effacer la mémoire d'une session.
     * 
     * @param sessionId L'ID de la session à effacer.
     * @return Un message de confirmation.
     */
    @DeleteMapping("/sessions/{sessionId}")
    @Operation(
        summary = "Efface la mémoire d'une session",
        description = "Efface la mémoire conversationnelle pour une session donnée."
    )
    public ResponseEntity<Map<String, String>> clearSessionMemory(@PathVariable String sessionId) {
        agentService.clearSessionMemory(sessionId);
        Map<String, String> response = Map.of("message", "Mémoire de la session " + sessionId + " effacée avec succès.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint pour effacer toute la mémoire.
     * 
     * @return Un message de confirmation.
     */
    @DeleteMapping("/memory")
    @Operation(
        summary = "Efface toute la mémoire",
        description = "Efface toute la mémoire conversationnelle de l'agent."
    )
    public ResponseEntity<Map<String, String>> clearAllMemory() {
        agentService.clearAllMemory();
        Map<String, String> response = Map.of("message", "Toute la mémoire a été effacée avec succès.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint pour obtenir des statistiques sur les sessions.
     * 
     * @return Le nombre de sessions actives.
     */
    @GetMapping("/stats")
    @Operation(
        summary = "Obtient des statistiques sur les sessions",
        description = "Retourne le nombre de sessions actives en mémoire."
    )
    public ResponseEntity<Map<String, Integer>> getSessionStats() {
        Map<String, Integer> stats = Map.of("activeSessions", agentService.getSessionCount());
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Classe interne pour représenter les informations sur un outil.
     */
    private static class ToolInfo {
        private String name;
        private String displayName;
        private String description;
        
        public ToolInfo(String name, String displayName, String description) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
        }
        
        // Getters et Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
    
    /**
     * Gestionnaire d'exceptions pour les erreurs de validation.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(IllegalArgumentException ex) {
        Map<String, String> errorResponse = Map.of(
            "error", "Erreur de validation",
            "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
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
