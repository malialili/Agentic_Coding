# 🤖 Agent IA avec Spring Boot + Spring AI

Ce projet est un **exemple complet d'agent IA développé avec Spring Boot et Spring AI**, intégrant :
- **Spring AI** pour une intégration native avec les modèles de langage (LLM) comme Mistral AI
- **Outils externes** (recherche web via Tavily, analyse de données)
- **Mémoire conversationnelle** (contexte persistant)
- **API REST** pour interagir avec l'agent
- **Architecture modulaire** et propre

---

## 📌 **Différences avec la version sans Spring AI**

| Aspect | Version Standard (java-springboot/) | Version Spring AI (java-springboot-springai/) |
|--------|------------------------------------|--------------------------------------------------|
| **Intégration LLM** | Appels manuels à l'API Mistral via WebClient | Utilisation du **ChatModel** de Spring AI |
| **Configuration** | Manuelle dans chaque outil | Centralisée via `MistralConfig` |
| **Dépendances** | WebClient, Jackson | **Spring AI Mistral** + WebClient |
| **Code** | Plus verbeux pour les appels LLM | Plus concis et maintenable |
| **Fonctionnalités** | Basiques | **Avancées** (streaming, embeddings, etc.) |

---

## 🛠 **Prérequis**

### 1. **Java 21+**
Vérifiez votre version de Java :
```bash
java -version
```

### 2. **Maven 3.8+**
Vérifiez votre version de Maven :
```bash
mvn -version
```

### 3. **Clés API**
Vous avez besoin de deux clés API :
- **Mistral AI** : [S'inscrire ici](https://console.mistral.ai/)
- **Tavily** (pour la recherche web) : [S'inscrire ici](https://tavily.com/)

---

## 🚀 **Installation et Exécution**

### 1. **Cloner le projet**
```bash
cd java-springboot-springai
```

### 2. **Configurer les clés API**

#### Option 1 : Variables d'environnement (recommandé)
```bash
# Sur Linux/Mac
export MISTRAL_API_KEY=votre_cle_api_mistral
export TAVILY_API_KEY=votre_cle_api_tavily

# Sur Windows (PowerShell)
$env:MISTRAL_API_KEY="votre_cle_api_mistral"
$env:TAVILY_API_KEY="votre_cle_api_tavily"
```

#### Option 2 : Modifier `application.yml`
Éditez le fichier `src/main/resources/application.yml` :
```yaml
spring:
  ai:
    mistral:
      api-key: votre_cle_api_mistral
      base-url: https://api.mistral.ai/v1

app:
  tavily:
    api-key: votre_cle_api_tavily
```

⚠️ **Important** : Ne commitez **jamais** vos clés API dans le code source !

### 3. **Compiler et exécuter**
```bash
# Compiler le projet
mvn clean compile

# Exécuter l'application
mvn spring-boot:run
```

L'application démarrera sur `http://localhost:8081/api` (port 8081 pour éviter les conflits).

---

## 🌐 **API REST**

### **Endpoints Disponibles**

| Méthode | Endpoint | Description | Exemple |
|---------|----------|-------------|---------|
| POST | `/agent/ask` | Pose une question à l'agent | [Voir ci-dessous](#post-agentask) |
| GET | `/agent/ask?question=...` | Pose une question simple | [Voir ci-dessous](#get-agentask) |
| GET | `/agent/tools` | Liste les outils disponibles | - |
| DELETE | `/agent/sessions/{sessionId}` | Efface la mémoire d'une session | - |
| DELETE | `/agent/memory` | Efface toute la mémoire | - |
| GET | `/agent/stats` | Statistiques sur les sessions | - |

---

### **📝 Exemples de Requêtes**

#### **1. POST `/agent/ask`**
**Requête :**
```bash
curl -X POST http://localhost:8081/api/agent/ask \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Quelles sont les tendances IA en 2025 ?",
    "sessionId": "ma-session-123"
  }'
```

**Réponse :**
```json
{
  "answer": "D'après mes recherches via Spring AI, les tendances IA en 2025 incluent :\n\n1. **Modèles Multi-Modaux** : Combinaison de texte, image, audio (ex: Mistral-3).\n2. **Agents Autonomes** : Capables de s'auto-améliorer.\n3. **Spring AI** : Intégration native des LLM dans les applications Spring.\n\nSources : [Tavily Search Results]",
  "sources": ["Tavily Search: tendances IA 2025"],
  "sessionId": "ma-session-123",
  "timestamp": "2025-01-15T10:30:00",
  "success": true,
  "metadata": {
    "model": "Mistral AI (via Spring AI)",
    "toolsUsed": ["web_search:tendances IA 2025"]
  }
}
```

---

## 📂 **Structure du Projet**

```
java-springboot-springai/
├── pom.xml                                      # Dépendances Maven (incluant Spring AI)
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── example/
    │   │           └── agent/
    │   │               ├── config/                          # Configuration Spring AI
    │   │               │   └── MistralConfig.java           # Configuration de Mistral AI
    │   │               ├── model/                           # DTOs
    │   │               │   ├── RequestDTO.java
    │   │               │   └── ResponseDTO.java
    │   │               ├── service/                        # Logique métier
    │   │               │   └── AgentService.java
    │   │               ├── tool/                           # Outils
    │   │               │   ├── AnalysisTool.java
    │   │               │   ├── MistralTool.java            # Utilise Spring AI ChatModel
    │   │               │   ├── Tool.java
    │   │               │   └── WebSearchTool.java
    │   │               ├── controller/                     # Contrôleurs REST
    │   │               │   └── AgentController.java
    │   │               └── AgenticAiSpringAiApplication.java # Classe principale
    │   └── resources/
    │       ├── application.yml                          # Configuration
    │       └── prompts/
    │           └── agent-prompt.txt                      # Template de prompt
    └── test/                                        # Tests unitaires
```

---

## 🔧 **Points Clés de l'Implémentation**

### **1. Configuration Spring AI**

La classe `MistralConfig` configure l'intégration avec Mistral AI :

```java
@Configuration
public class MistralConfig {
    @Bean
    public ChatModel mistralChatModel(MistralApi mistralApi) {
        return new MistralChatModel(mistralApi);
    }
}
```

- **`RestClient`** : Configuré avec l'API key et l'URL de base.
- **`MistralApi`** : Client pour interagir avec l'API Mistral.
- **`ChatModel`** : Abstraction Spring AI pour les interactions avec les LLM.

---

### **2. Utilisation de Spring AI dans les Outils**

Le `MistralTool` utilise directement le `ChatModel` injecté :

```java
@Component
public class MistralTool implements Tool {
    private final ChatModel chatModel;
    
    @Autowired
    public MistralTool(ChatModel chatModel) {
        this.chatModel = chatModel;
    }
    
    @Override
    public String execute(String prompt) {
        return chatModel.call(new Prompt(prompt)).getContent();
    }
}
```

**Avantages :**
- **Code plus simple** : Pas besoin de gérer manuellement les appels HTTP.
- **Abstraction** : Spring AI gère les détails de l'API (authentification, sérialisation, etc.).
- **Extensible** : Facile à remplacer par un autre fournisseur (OpenAI, Azure, etc.).

---

### **3. Configuration dans `application.yml`**

Spring AI utilise une configuration dédiée :

```yaml
spring:
  ai:
    mistral:
      api-key: ${MISTRAL_API_KEY}
      base-url: https://api.mistral.ai/v1
      chat:
        model: mistral-tiny
        temperature: 0.7
        max-tokens: 2000
```

---

### **4. Service Agent avec Spring AI**

Le `AgentService` utilise à la fois :
- Le **`ChatModel`** de Spring AI pour les appels directs au LLM.
- Les **outils personnalisés** pour les fonctionnalités spécifiques.

```java
@Service
public class AgentService {
    private final List<Tool> tools;
    private final ChatModel chatModel; // Injecté par Spring AI
    
    @Autowired
    public AgentService(List<Tool> tools, ChatModel chatModel) {
        this.tools = tools;
        this.chatModel = chatModel;
    }
    
    public ResponseDTO processRequest(RequestDTO request) {
        // Utilisation du ChatModel pour générer une réponse
        String llmResponse = chatModel.call(new Prompt(prompt)).getContent();
        // ...
    }
}
```

---

## 🌟 **Avantages de Spring AI**

### **1. Intégration Native**
- **Aucun code boilerplate** pour les appels aux APIs LLM.
- **Gestion automatique** des erreurs, timeouts, etc.
- **Support de plusieurs fournisseurs** (Mistral, OpenAI, Azure, etc.) avec la même API.

### **2. Fonctionnalités Avancées**
- **Streaming** : Réponses en temps réel.
- **Embeddings** : Support des modèles d'embedding pour les bases vectorielles.
- **Prompt Templates** : Réutilisation des prompts avec des variables.
- **Métriques** : Intégration avec Micrometer pour le monitoring.

### **3. Écosystème Spring**
- **Intégration transparente** avec Spring Boot, Spring Cloud, etc.
- **Support des tests** : Facile à tester avec `@SpringBootTest`.
- **Documentation** : Bien documenté par la communauté Spring.

---

## 📚 **Comparaison avec la Version Standard**

### **Version Standard (java-springboot/)**
```java
// Appel manuel à l'API Mistral
public String callMistral(String prompt) {
    WebClient mistralClient = WebClient.builder()
        .baseUrl("https://api.mistral.ai/v1")
        .defaultHeader("Authorization", "Bearer " + apiKey)
        .build();
    
    Map<String, Object> request = Map.of(
        "model", "mistral-tiny",
        "messages", List.of(Map.of("role", "user", "content", prompt))
    );
    
    return mistralClient.post()
        .uri("/chat/completions")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(Map.class)
        .map(response -> (String) ((Map) ((List) response.get("choices")).get(0)).get("message")).get("content"))
        .block();
}
```

### **Version Spring AI (java-springboot-springai/)**
```java
// Utilisation de Spring AI
@Autowired
private ChatModel chatModel;

public String execute(String prompt) {
    return chatModel.call(new Prompt(prompt)).getContent();
}
```

**→ Code 10x plus simple et maintenable !**

---

## 🛠 **Personnalisation**

### **1. Changer le Fournisseur LLM**

Pour utiliser **OpenAI** au lieu de Mistral :

1. **Ajouter la dépendance** dans `pom.xml` :
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-openai</artifactId>
    <version>${spring-ai.version}</version>
</dependency>
```

2. **Créer une configuration OpenAI** :
```java
@Configuration
public class OpenAIConfig {
    @Bean
    public ChatModel openAiChatModel(OpenAiApi openAiApi) {
        return new OpenAiChatModel(openAiApi);
    }
}
```

3. **Mettre à jour `application.yml`** :
```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      base-url: https://api.openai.com/v1
```

---

### **2. Ajouter le Streaming**

Spring AI supporte le **streaming** pour les réponses en temps réel :

```java
@Bean
public ChatModel streamingMistralChatModel(MistralApi mistralApi) {
    return new MistralChatModel(mistralApi, new MistralChatOptions()
        .setStreaming(true));
}

// Dans le contrôleur
@GetMapping("/stream")
public Flux<String> streamResponse(@RequestParam String question) {
    Prompt prompt = new Prompt(question);
    return chatModel.stream(prompt);
}
```

---

### **3. Utiliser des Embeddings**

Pour ajouter des fonctionnalités de recherche sémantique :

1. **Ajouter la dépendance** :
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-embedding</artifactId>
    <version>${spring-ai.version}</version>
</dependency>
```

2. **Configurer l'EmbeddingModel** :
```java
@Bean
public EmbeddingModel embeddingModel(MistralApi mistralApi) {
    return new MistralEmbeddingModel(mistralApi);
}
```

---

## 🌍 **Cas d'Usage Avancés**

| Cas d'Usage | Implémentation avec Spring AI | Bénéfices |
|-------------|--------------------------------|-----------|
| **Chatbot avec mémoire** | Utiliser `ChatModel` + mémoire conversationnelle | Conversations fluides et contextuelles |
| **Recherche sémantique** | `EmbeddingModel` + base vectorielle (FAISS, ChromaDB) | Recherche précise dans de grands volumes de données |
| **Agent multi-outils** | Combiner `ChatModel` + outils personnalisés | Flexibilité maximale |
| **Traitement par lots** | `ChatModel` avec batch processing | Efficacité pour les tâches répétitives |
| **APIs serverless** | Déployer avec Spring Cloud Function | Évolutivité et coût optimisé |

---

## 📚 **Ressources**

### **Documentation Officielle**
- [Spring AI GitHub](https://github.com/spring-projects/spring-ai)
- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Spring AI Mistral](https://github.com/spring-projects/spring-ai/tree/main/models/spring-ai-mistral)

### **Exemples et Tutoriels**
- [Spring AI Samples](https://github.com/spring-projects/spring-ai/tree/main/samples)
- [Spring AI Blog](https://spring.io/blogs)
- [Mistral AI Docs](https://docs.mistral.ai/)

### **Communauté**
- [Spring Discord](https://discord.gg/spring)
- [Stack Overflow - spring-ai](https://stackoverflow.com/questions/tagged/spring-ai)

---

## ⚠ **Bonnes Pratiques avec Spring AI**

### **1. Gestion des Erreurs**
- **Utilisez `@ExceptionHandler`** pour gérer les erreurs de manière centralisée.
- **Validez les entrées** avec `@Valid` et les annotations de validation.
- **Gérez les timeouts** en configurant `RestClient` avec des timeouts appropriés.

### **2. Performance**
- **Cachez les réponses** fréquentes avec `@Cacheable`.
- **Limitez les tokens** pour réduire les coûts (configurez `max-tokens` dans `application.yml`).
- **Utilisez le streaming** pour les réponses longues.

### **3. Sécurité**
- **Ne stockez pas les clés API** dans le code source.
- **Utilisez des profils Spring** pour séparer les configurations dev/prod.
- **Limitez les permissions** des clés API.

### **4. Tests**
- **Mock les appels aux APIs** avec `@MockBean` dans les tests unitaires.
- **Testez les contrôleurs** avec `@WebMvcTest`.
- **Testez les services** avec `@SpringBootTest`.

---

## 🎯 **Prochaines Étapes**

1. **Tester l’application** avec vos propres clés API.
2. **Explorer les fonctionnalités avancées** de Spring AI :
   - Streaming
   - Embeddings
   - Prompt Templates
3. **Déployer en production** :
   - **Docker** : Construire une image et déployer avec Kubernetes.
   - **Cloud** : Déployer sur AWS, GCP ou Azure.
4. **Contribuer** :
   - Ouvrir une **issue** pour signaler un bug ou proposer une amélioration.
   - Soumettre une **pull request** pour ajouter un nouvel exemple ou fonctionnalité.

---

## 💬 **Auteur**

Ce projet a été généré par **Vibe Code** (Mistral AI) pour illustrer le développement agentique en IA avec **Spring Boot et Spring AI**.

Pour toute question ou suggestion, n’hésitez pas à contribuer ! 🚀

---

## 📜 **Licence**

Ce projet est sous licence **MIT**. Vous êtes libre de l’utiliser, le modifier et le distribuer selon vos besoins.
