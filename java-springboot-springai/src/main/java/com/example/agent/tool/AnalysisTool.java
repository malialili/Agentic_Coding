package com.example.agent.tool;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Outil d'analyse de texte pour extraire des tendances, mots-clés ou informations.
 * 
 * Cet outil permet à l'agent IA d'analyser du texte pour en extraire
 * des informations structurées comme des tendances, mots-clés, etc.
 * 
 * @author Vibe Code (Mistral AI)
 */
@Component
public class AnalysisTool implements Tool {
    
    // Expression régulière pour extraire les mots en majuscules
    private static final Pattern UPPERCASE_WORDS = Pattern.compile("\\b[A-ZÉÈÊËÀÂÇÔÏÜ][A-ZÉÈÊËÀÂÇÔÏÜ0-9'-]+\\b");
    
    // Expression régulière pour extraire les années
    private static final Pattern YEARS = Pattern.compile("\\b(19|20)\\d{2}\\b");
    
    // Expression régulière pour extraire les mots-clés entre crochets
    private static final Pattern KEYWORDS_IN_BRACKETS = Pattern.compile("\\[([^\\]]+)\\]");
    
    @Override
    public String getName() {
        return "analyze_data";
    }
    
    @Override
    public String getDescription() {
        return "Analyse un texte pour en extraire les tendances, mots-clés ou informations structurées. " +
               "Utilisez cet outil pour traiter du texte brut et en extraire des informations utiles.";
    }
    
    @Override
    public String getParametersDescription() {
        return "text: String (le texte à analyser)";
    }
    
    @Override
    public String execute(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "Aucun texte à analyser.";
        }
        
        StringBuilder analysis = new StringBuilder();
        
        // 1. Extraire les mots en majuscules (acronymes, noms propres)
        List<String> uppercaseWords = UPPERCASE_WORDS.matcher(text)
                .results()
                .map(match -> match.group())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        
        if (!uppercaseWords.isEmpty()) {
            analysis.append("🔠 **Mots en majuscules (acronymes/noms propres):**\n");
            analysis.append(String.join(", ", uppercaseWords)).append("\n\n");
        }
        
        // 2. Extraire les années
        List<String> years = YEARS.matcher(text)
                .results()
                .map(match -> match.group())
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        
        if (!years.isEmpty()) {
            analysis.append("📅 **Années mentionnées:** ");
            analysis.append(String.join(", ", years)).append("\n\n");
        }
        
        // 3. Extraire les mots-clés entre crochets
        List<String> keywordsInBrackets = KEYWORDS_IN_BRACKETS.matcher(text)
                .results()
                .map(match -> match.group(1))
                .distinct()
                .collect(Collectors.toList());
        
        if (!keywordsInBrackets.isEmpty()) {
            analysis.append("🏷️ **Mots-clés entre crochets:** ");
            analysis.append(String.join(", ", keywordsInBrackets)).append("\n\n");
        }
        
        // 4. Compter les mots
        String[] words = text.split("\\s+");
        analysis.append("📊 **Statistiques:**\n");
        analysis.append("- Nombre de mots: ").append(words.length).append("\n");
        analysis.append("- Nombre de caractères: ").append(text.length()).append("\n");
        
        // 5. Extraire les tendances (mots fréquents)
        Map<String, Long> wordFrequency = Arrays.stream(words)
                .filter(word -> word.length() > 3)  // Ignorer les mots courts
                .map(word -> word.replaceAll("[^a-zA-Zà-üÀ-Ü]", "").toLowerCase())
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
        
        List<Map.Entry<String, Long>> topWords = wordFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());
        
        if (!topWords.isEmpty()) {
            analysis.append("\n🔝 **Mots les plus fréquents:**\n");
            for (Map.Entry<String, Long> entry : topWords) {
                analysis.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" occurrences\n");
            }
        }
        
        return analysis.toString();
    }
}
