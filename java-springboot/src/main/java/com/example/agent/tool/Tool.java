package com.example.agent.tool;

/**
 * Interface pour les outils utilisables par l'agent IA.
 * 
 * Chaque outil doit implémenter cette interface pour être reconnu
 * et utilisé par l'agent.
 * 
 * @author Vibe Code (Mistral AI)
 */
public interface Tool {
    
    /**
     * Retourne le nom unique de l'outil.
     * 
     * @return Le nom de l'outil.
     */
    String getName();
    
    /**
     * Retourne une description de l'outil.
     * Cette description est utilisée par l'agent pour décider quand utiliser l'outil.
     * 
     * @return La description de l'outil.
     */
    String getDescription();
    
    /**
     * Exécute l'outil avec l'entrée fournie.
     * 
     * @param input L'entrée pour l'outil.
     * @return Le résultat de l'exécution de l'outil.
     * @throws RuntimeException Si une erreur survient pendant l'exécution.
     */
    String execute(String input);
    
    /**
     * Retourne une chaîne représentant les paramètres attendus par l'outil.
     * Utile pour la documentation et l'auto-complétion.
     * 
     * @return Une chaîne décrivant les paramètres.
     */
    default String getParametersDescription() {
        return "input: String (l'entrée pour l'outil)";
    }
}
