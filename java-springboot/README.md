# 🤖 Agent IA avec Spring Boot

Ce projet est un **exemple complet d'agent IA développé avec Spring Boot**, intégrant :
- **Raisonnement autonome** (Chain-of-Thought)
- **Outils externes** (recherche web via Tavily, analyse de données)
- **Mémoire conversationnelle** (contexte persistant)
- **API REST** pour interagir avec l'agent
- **Intégration avec Mistral AI** (ou tout autre LLM)

---

## 📌 **Fonctionnalités**

✅ **Architecture modulaire** : Séparation claire entre contrôleurs, services et outils.
✅ **Outils personnalisables** : Ajoutez facilement de nouveaux outils.
✅ **Mémoire conversationnelle** : Maintien du contexte entre les requêtes.
✅ **API REST bien documentée** : Utilisation de Swagger/OpenAPI.
✅ **Gestion des erreurs** : Réponses structurées en cas d'erreur.
✅ **Configuration flexible** : Paramétrage via `application.yml`.

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
- **Tavily API** (pour la recherche web) : [S'inscrire ici](https://tavily.com/)
- **Mistral API** (pour le LLM) : [S'inscrire ici](https://console.mistral.ai/)

---

## 🚀 **Installation et Exécution**

### 1. **Cloner le projet**
```bash
cd java-springboot
git clone https://github.com/malialili/Agentic_Coding.git
cd Agentic_Coding/java-springboot
```

### 2. **Configurer les clés API**

#### Option 1 : Variables d'environnement
```bash
# Sur Linux/Mac
export MISTRAL_API_KEY=votre_cle_api_mistral
export TAVILY_API_KEY=votre_cle_api_tavily

# Sur Windows (PowerShell)
$env:MISTRAL_API_KEY="votre_cle_api_mistral"
$env:TAVILY_API_KEY="votre_cle_api_tavily"
```

#### Option 2 : Modifier `application.yml`
Éditez le fichier `src/main/resources/application.yml` et remplacez les valeurs par défaut :
```yaml
app:
  mistral:
    api-key: votre_cle_api_mistral
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

L'application démarrera sur `http://localhost:8080/api`.

---

## 🌐 **API REST**

### **Endpoints Disponibles**

| Méthode | Endpoint | Description | Exemple de Requête |
|---------|----------|-------------|---------------------|
| POST | `/agent/ask` | Pose une question à l'agent | [Voir ci-dessous](#post-agentask) |
| GET | `/agent/ask?question=...` | Pose une question simple | [Voir ci-dessous](#get-agentask) |
| GET | `/agent/tools` | Liste les outils disponibles | - |
| DELETE | `/agent/sessions/{sessionId}` | Efface la mémoire d'une session | - |
| DELETE | `/agent/memory` | Efface toute la mémoire | - |
| GET | `/agent/stats` | Obtient des statistiques sur les sessions | - |

---

### **📝 Exemples de Requêtes**

#### **1. POST `/agent/ask`**
**Requête :**
```bash
curl -X POST http://localhost:8080/api/agent/ask \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Quelles sont les tendances IA en 2025 ?",
    "sessionId": "ma-session-123"
  }'
```

**Réponse :**
```json
{
  "answer": "D'après mes recherches, les tendances IA en 2025 incluent :\n\n1. **Modèles Multi-Modaux** : Combinaison de texte, image, audio (ex: Mistral-3).\n2. **Agents Autonomes** : Capables de s'auto-améliorer (ex: AutoGen).\n3. **Régulation** : Loi européenne sur l'IA.\n\nSources : [Tavily Search Results]",
  "sources": ["Tavily Search: tendances IA 2025"],
  "sessionId": "ma-session-123",
  "timestamp": "2025-01-15T10:30:00",
  "success": true
}
```

---

#### **2. GET `/agent/ask`**
**Requête :**
```bash
curl -X GET "http://localhost:8080/api/agent/ask?question=Quelles%20sont%20les%20tendances%20IA%20en%202025%20?"
```

**Réponse :**
```json
{
  "answer": "D'après mes recherches, les tendances IA en 2025 incluent :\n\n1. **Modèles Multi-Modaux** : ...",
  "sources": ["Tavily Search: tendances IA 2025"],
  "sessionId": "uuid-généré-automatiquement",
  "timestamp": "2025-01-15T10:30:00",
  "success": true
}
```

---

#### **3. GET `/agent/tools`**
**Requête :**
```bash
curl -X GET http://localhost:8080/api/agent/tools
```

**Réponse :**
```json
[
  {
    "name": "web_search",
    "displayName": "Recherche web",
    "description": "Effectue une recherche web pour trouver des informations récentes."
  },
  {
    "name": "analyze_data",
    "displayName": "Analyse de données",
    "description": "Analyse un texte pour en extraire des tendances ou mots-clés."
  },
  {
    "name": "mistral_llm",
    "displayName": "Modèle de langage Mistral",
    "description": "Utilise Mistral AI pour générer du texte ou répondre à des questions."
  }
]
```

---

## 📂 **Structure du Projet**

```
java-springboot/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── agent/
│   │   │               ├── config/          # Configuration Spring
│   │   │               ├── model/           # DTOs (RequestDTO, ResponseDTO)
│   │   │               ├── service/        # Logique métier (AgentService)
│   │   │               ├── tool/           # Outils (WebSearchTool, AnalysisTool, MistralTool)
│   │   │               ├── controller/     # Contrôleurs REST (AgentController)
│   │   │               └── AgenticAiApplication.java  # Classe principale
│   │   └── resources/
│   │       ├── application.yml          # Configuration Spring
│   │       └── prompts/
│   │           └── agent-prompt.txt      # Template de prompt
│   └── test/                            # Tests unitaires
└── pom.xml                              # Dépendances Maven
```

---

## 🔧 **Personnalisation**

### **1. Ajouter un nouvel outil**

1. **Créer une nouvelle classe** dans `src/main/java/com/example/agent/tool/` :

```java
package com.example.agent.tool;

import org.springframework.stereotype.Component;

@Component
public class MonNouvelOutil implements Tool {
    
    @Override
    public String getName() {
        return "mon_outil";
    }
    
    @Override
    public String getDescription() {
        return "Description de mon nouvel outil.";
    }
    
    @Override
    public String execute(String input) {
        // Logique de l'outil
        return "Résultat de mon outil: " + input;
    }
}
```

2. **L'outil sera automatiquement détecté** par Spring grâce à l'annotation `@Component`.

3. **Mettre à jour le prompt** pour inclure votre nouvel outil dans la description.

---

### **2. Changer le modèle LLM**

Pour utiliser un autre fournisseur (ex: OpenAI) :

1. **Créer un nouvel outil** :

```java
package com.example.agent.tool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OpenAITool implements Tool {
    
    private final WebClient webClient;
    private final String openaiApiKey;
    
    public OpenAITool(WebClient.Builder webClientBuilder,
                     @Value("${app.openai.api-key}") String openaiApiKey) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1").build();
        this.openaiApiKey = openaiApiKey;
    }
    
    @Override
    public String getName() {
        return "openai_llm";
    }
    
    @Override
    public String getDescription() {
        return "Utilise OpenAI pour générer du texte ou répondre à des questions.";
    }
    
    @Override
    public String execute(String prompt) {
        // Implémentation similaire à MistralTool
        // ...
    }
}
```

2. **Ajouter la configuration** dans `application.yml` :

```yaml
app:
  openai:
    api-key: ${OPENAI_API_KEY}
```

---

### **3. Utiliser une base de données pour la mémoire**

Pour remplacer la mémoire en mémoire par **Redis** :

1. **Ajouter la dépendance** dans `pom.xml` :

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

2. **Configurer Redis** dans `application.yml` :

```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

3. **Modifier `AgentService`** pour utiliser Redis :

```java
@Autowired
private RedisTemplate<String, String> redisTemplate;

// Remplacer la Map par Redis
public String getContext(String sessionId) {
    return redisTemplate.opsForValue().get("session:" + sessionId);
}

public void saveContext(String sessionId, String context) {
    redisTemplate.opsForValue().set("session:" + sessionId, context);
}
```

---

### **4. Ajouter Swagger/OpenAPI**

Pour une **documentation interactive** de l'API :

1. **Ajouter les dépendances** dans `pom.xml` :

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

2. **Accéder à la documentation** :
   - **Swagger UI** : `http://localhost:8080/api/swagger-ui.html`
   - **OpenAPI JSON** : `http://localhost:8080/api/v3/api-docs`

---

## 🌍 **Cas d'Usage**

| Domaine          | Exemple d’Utilisation                          | Adaptations Nécessaires               |
|------------------|-----------------------------------------------|----------------------------------------|
| **E-commerce**   | Recommandation de produits basée sur les tendances | Ajouter un outil d'accès à une base de produits |
| **Santé**        | Analyse de symptômes (avec prudence)          | Ajouter une base de connaissances médicales |
| **Finance**      | Suivi des marchés boursiers                   | Intégrer une API financière (ex: Alpha Vantage) |
| **Éducation**    | Tuteur virtuel pour expliquer des concepts    | Ajouter une base de connaissances éducatives |
| **DevOps**       | Automatisation de tâches (déploiement, logs)   | Intégrer GitHub API, Docker, etc. |

---

## 📚 **Ressources**

### **Documentations**
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
- [Mistral AI API](https://docs.mistral.ai/)
- [Tavily API](https://docs.tavily.com/)
- [Swagger/OpenAPI](https://swagger.io/docs/)

### **Frameworks Alternatifs**
- [LangChain4j](https://github.com/langchain4j/langchain4j) : Framework Java pour les agents IA
- [Quarkus](https://quarkus.io/) : Alternative à Spring Boot pour les applications cloud-native
- [Micronaut](https://micronaut.io/) : Autre framework Java léger

---

## ⚠ **Bonnes Pratiques**

### **1. Sécurité**
- **Ne jamais commiter vos clés API** dans le code source.
- Utilisez des **variables d'environnement** ou un **service de secrets** (ex: HashiCorp Vault).
- **Validez toujours les entrées** pour éviter les injections de prompts.
- **Limitez les permissions** des clés API (ex: lecture seule si possible).

### **2. Performance**
- **Cachez les résultats** des appels aux APIs externes (ex: avec Spring Cache).
- **Limitez le nombre de tokens** générés par les LLM pour réduire les coûts.
- **Utilisez des connexions HTTP persistantes** pour les appels aux APIs.

### **3. Éthique**
- **Respectez le RGPD** et les lois locales sur l’IA.
- **Ajoutez des garde-fous** contre les biais et les contenus inappropriés.
- **Soyez transparent** sur les limitations de l’agent.

### **4. Maintenabilité**
- **Structurez votre code** en modules clairs.
- **Documentez chaque outil et service**.
- **Écrivez des tests unitaires** pour les composants critiques.
- **Utilisez des logs** pour le débogage et le monitoring.

---

## 🎯 **Prochaines Étapes**

1. **Tester l’application** avec vos propres clés API.
2. **Ajouter un outil personnalisé** pour votre cas d’usage spécifique.
3. **Déployer l’application** :
   - **Localement** : `mvn spring-boot:run`
   - **Sur un serveur** : Construire un JAR et l’exécuter avec `java -jar agentic-ai-1.0.0.jar`
   - **Sur le cloud** : Déployer sur AWS, GCP ou Azure (ex: avec Spring Cloud)
4. **Explorer d’autres fonctionnalités** :
   - Ajouter un **système de planification** pour les tâches complexes.
   - Intégrer une **base de connaissances** (ex: avec FAISS ou ChromaDB).
   - Implémenter un **système de feedback** pour améliorer l’agent.

---

## 💬 **Auteur**

Ce projet a été généré par **Vibe Code** (Mistral AI) pour illustrer le développement agentique en IA avec **Spring Boot**.

Pour toute question, suggestion ou contribution, n’hésitez pas à ouvrir une issue ou une pull request ! 🚀

---

## 📜 **Licence**

Ce projet est sous licence **MIT**. Vous êtes libre de l’utiliser, le modifier et le distribuer selon vos besoins.
