# 🤖 **Agentic Coding - Exemples de Développement Agentique IA**

Bienvenue dans le dépôt **Agentic Coding** ! Ce projet contient des **exemples complets** de développement d'agents IA autonomes en **Python** et **Java/Spring Boot**, illustrant les concepts clés du développement agentique.

---

## 📌 **À propos du projet**

Ce dépôt a été créé pour démontrer comment construire des **agents IA autonomes** capables de :
- **Raisonner** (Chain-of-Thought, ReAct)
- **Utiliser des outils externes** (recherche web, APIs, bases de données)
- **Mémoriser les conversations** (contexte persistant)
- **S’auto-corriger** (boucles de feedback)
- **Collaborer** (équipes d’agents)

Les exemples sont conçus pour être **modulaires, extensibles et prêts pour la production**.

---

## 📂 **Structure du Projet**

```
Agentic_Coding/
├── python/                          # Exemple en Python avec LangChain
│   ├── agent_recherche.py           # Agent principal avec recherche web et analyse
│   ├── requirements.txt             # Dépendances Python
│   ├── .env.example                 # Exemple de configuration des clés API
│   └── README.md                    # Documentation détaillée
│
├── java-springboot/                 # Exemple en Java avec Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/agent/
│   │   │   │   ├── config/          # Configuration Spring
│   │   │   │   ├── model/           # DTOs (RequestDTO, ResponseDTO)
│   │   │   │   ├── service/        # Logique métier (AgentService)
│   │   │   │   ├── tool/           # Outils (WebSearchTool, AnalysisTool, MistralTool)
│   │   │   │   ├── controller/     # API REST (AgentController)
│   │   │   │   └── AgenticAiApplication.java
│   │   │   └── resources/
│   │   │       ├── application.yml  # Configuration Spring
│   │   │       └── prompts/
│   │   │           └── agent-prompt.txt
│   │   └── test/                    # Tests unitaires
│   └── pom.xml                      # Dépendances Maven
│   └── README.md                    # Documentation détaillée
│
└── README.md                        # Documentation principale (ce fichier)
```

---

## 🚀 **Exemples Disponibles**

| Langage/Framework | Description | Lien |
|-------------------|-------------|------|
| **Python + LangChain** | Agent IA avec recherche web (Tavily) et LLM (Mistral AI). Idéal pour prototyper rapidement. | [Lien](./python/) |
| **Java + Spring Boot** | Agent IA avec architecture modulaire, API REST, et intégration avec Mistral AI et Tavily. Parfait pour les applications d'entreprise. | [Lien](./java-springboot/) |

---

## 🛠 **Prérequis Communs**

Pour les deux exemples, vous aurez besoin de :

### **1. Clés API**
| Service | Description | Lien |
|---------|-------------|------|
| **Mistral AI** | Clé API pour utiliser les modèles de langage Mistral. | [S'inscrire](https://console.mistral.ai/) |
| **Tavily** | Clé API pour la recherche web. | [S'inscrire](https://tavily.com/) |

⚠️ **Important** : 
- Ne **commitez jamais** vos clés API dans le code source.
- Utilisez toujours des **variables d’environnement** (`.env` pour Python, `application.yml` pour Spring Boot).

---

## 🏃 **Démarrage Rapide**

### **Option 1 : Python (LangChain)**

1. **Installer les dépendances** :
   ```bash
   cd python
   pip install -r requirements.txt
   ```

2. **Configurer les clés API** :
   ```bash
   cp .env.example .env
   # Éditez .env avec vos clés API
   ```

3. **Exécuter l'agent** :
   ```bash
   python agent_recherche.py
   ```

4. **Tester** :
   L'agent répondra directement dans la console. Pour une API REST, ajoutez FastAPI (voir [documentation Python](./python/README.md)).

---

### **Option 2 : Java/Spring Boot**

1. **Compiler le projet** :
   ```bash
   cd java-springboot
   mvn clean compile
   ```

2. **Configurer les clés API** :
   - Soit via des variables d’environnement :
     ```bash
     export MISTRAL_API_KEY=votre_cle
     export TAVILY_API_KEY=votre_cle
     ```
   - Soit en éditant `src/main/resources/application.yml`.

3. **Exécuter l'application** :
   ```bash
   mvn spring-boot:run
   ```

4. **Tester l'API** :
   ```bash
   curl -X POST http://localhost:8080/api/agent/ask \
     -H "Content-Type: application/json" \
     -d '{"question": "Quelles sont les tendances IA en 2025 ?"}'
   ```

---

## 🔧 **Concepts Clés du Développement Agentique**

| Concept | Description | Implémentation dans les Exemples |
|---------|-------------|----------------------------------|
| **Agent** | Entité autonome qui perçoit, raisonne et agit. | `AgentExecutor` (Python), `AgentService` (Java) |
| **Raisonnement** | Processus de pensée (Chain-of-Thought, ReAct). | Prompts structurés dans les deux exemples |
| **Outils (Tools)** | Fonctions externes appelables par l'agent. | `Tool` interface (Java), `@tool` décorateur (Python) |
| **Mémoire** | Stockage du contexte conversationnel. | `ConversationBufferMemory` (Python), `Map<String, String>` (Java) |
| **Planification** | Décomposition de tâches complexes. | À ajouter (ex: `LangGraph` en Python, workflow custom en Java) |
| **Auto-évaluation** | Vérification et amélioration des réponses. | À ajouter (ex: `langchain.evaluation` en Python) |

---

## 🌍 **Cas d’Usage**

Voici quelques idées pour adapter ces exemples à vos besoins :

| Domaine | Exemple d’Application | Adaptations Nécessaires |
|---------|----------------------|--------------------------|
| **E-commerce** | Agent de recommandation de produits | Ajouter un outil d’accès à une base de produits |
| **Santé** | Assistant médical (avec prudence) | Ajouter une base de connaissances médicales |
| **Finance** | Analyseur de marchés boursiers | Intégrer une API financière (ex: Alpha Vantage) |
| **Éducation** | Tuteur virtuel | Ajouter une base de connaissances éducatives |
| **DevOps** | Automatisation de tâches | Intégrer GitHub API, Docker, Kubernetes |
| **Support Client** | Chatbot de support | Intégrer une base de connaissances FAQ |
| **Recherche** | Agent de veille technologique | Configurer des recherches automatiques |

---

## 📚 **Ressources pour Aller Plus Loin**

### **Frameworks et Bibliothèques**

#### **Python**
- [LangChain](https://python.langchain.com/) : Framework pour construire des agents IA.
- [LangGraph](https://github.com/langchain-ai/langgraph) : Pour la planification et les workflows.
- [CrewAI](https://github.com/joaomdmoura/crewAI) : Pour les équipes d’agents.
- [LlamaIndex](https://github.com/run-llama/llama_index) : Pour les bases de connaissances.
- [AutoGen](https://github.com/microsoft/autogen) : Agents autonomes (Microsoft).

#### **Java**
- [LangChain4j](https://github.com/langchain4j/langchain4j) : Portage Java de LangChain.
- [Spring AI](https://github.com/spring-projects/spring-ai) : Intégration IA native avec Spring.
- [Quarkus LangChain](https://github.com/quarkusio/quarkus-langchain) : Pour les applications Quarkus.

#### **Multi-Langages**
- [Hugging Face](https://huggingface.co/) : Modèles open-source et APIs.
- [Tavily](https://tavily.com/) : Recherche web pour LLM.
- [Mistral AI](https://mistral.ai/) : Modèles de langage performants.

---

### **Tutoriels et Guides**
- [Guide LangChain](https://python.langchain.com/docs/get_started/introduction) : Documentation officielle.
- [Spring Boot Guides](https://spring.io/guides) : Tutoriels Spring Boot.
- [Mistral AI Docs](https://docs.mistral.ai/) : Documentation de l’API Mistral.
- [Agentic AI Patterns](https://github.com/microsoft/autogen/blob/main/README.md) : Patterns pour les agents autonomes.

---

## ⚠ **Bonnes Pratiques**

### **1. Sécurité**
- ❌ **Ne jamais** exposer vos clés API dans le code source.
- ✅ Utilisez des **variables d’environnement** ou des **services de secrets** (ex: Vault, AWS Secrets Manager).
- ✅ **Validez les entrées** pour éviter les injections de prompts.
- ✅ **Limitez les permissions** des clés API (principe du moindre privilège).

### **2. Performance**
- ✅ **Cachez les résultats** des appels aux APIs externes.
- ✅ **Limitez le nombre de tokens** générés par les LLM pour réduire les coûts.
- ✅ **Utilisez des connexions persistantes** pour les appels HTTP.
- ✅ **Optimisez les prompts** pour réduire la taille des requêtes.

### **3. Éthique et Conformité**
- ✅ **Respectez le RGPD** et les lois locales sur l’IA.
- ✅ **Ajoutez des garde-fous** contre les biais et les contenus inappropriés.
- ✅ **Soyez transparent** sur les limitations de l’agent.
- ✅ **Documentez les sources** utilisées pour les réponses.

### **4. Maintenabilité**
- ✅ **Structurez votre code** en modules clairs.
- ✅ **Documentez chaque composant** (outils, services, agents).
- ✅ **Écrivez des tests unitaires** pour les parties critiques.
- ✅ **Utilisez des logs** pour le débogage et le monitoring.

---

## 🎯 **Prochaines Étapes**

1. **Tester les exemples** avec vos propres clés API.
2. **Personnaliser un exemple** pour votre cas d’usage spécifique.
3. **Ajouter des fonctionnalités** :
   - Planification de tâches complexes.
   - Base de connaissances vectorielle (ex: FAISS, ChromaDB).
   - Système de feedback pour améliorer l’agent.
   - Interface utilisateur (ex: Streamlit, React).
4. **Déployer en production** :
   - **Python** : FastAPI + Docker + Kubernetes.
   - **Java** : Spring Boot + Docker + Cloud (AWS/GCP/Azure).
5. **Contribuer** :
   - Ouvrir une **issue** pour signaler un bug ou proposer une amélioration.
   - Soumettre une **pull request** pour ajouter un nouvel exemple ou fonctionnalité.

---

## 💬 **Contribution**

Les contributions sont les bienvenues ! Voici comment contribuer :

1. **Forker le dépôt** sur GitHub.
2. **Créer une branche** pour votre fonctionnalité (`git checkout -b feature/ma-nouvelle-fonctionnalité`).
3. **Commiter vos changements** (`git commit -m "Ajout de ma nouvelle fonctionnalité"`).
4. **Pousser vers la branche** (`git push origin feature/ma-nouvelle-fonctionnalité`).
5. **Ouvrir une Pull Request** sur GitHub.

---

## 📜 **Licence**

Ce projet est sous licence **MIT**. Vous êtes libre de :
- Utiliser le code pour vos projets personnels ou commerciaux.
- Modifier le code selon vos besoins.
- Distribuer le code sous les mêmes conditions.

Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

## 🙏 **Remerciements**

- **Mistral AI** : Pour les modèles de langage performants et open-source.
- **LangChain** : Pour le framework qui simplifie le développement d’agents IA.
- **Spring Boot** : Pour le framework Java qui rend le développement d’applications d’entreprise facile.
- **La communauté open-source** : Pour les bibliothèques et outils utilisés dans ce projet.

---

## 📞 **Contact**

Pour toute question ou suggestion, vous pouvez :
- Ouvrir une **issue** sur [GitHub](https://github.com/malialili/Agentic_Coding/issues).
- Contribuer directement via une **pull request**.
- Explorer les documentations liées dans la section [Ressources](#-ressources-pour-aller-plus-loin).

---

**Bonne exploration du développement agentique !** 🚀
