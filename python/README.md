# 🤖 Agent IA en Python - Exemple de Développement Agentique

Ce dossier contient un **exemple complet d'agent IA en Python** utilisant **LangChain**, **Tavily** (pour la recherche web) et **Mistral AI** (pour le modèle de langage).

---

## 📌 **Fonctionnalités**
✅ **Raisonnement autonome** (Chain-of-Thought)
✅ **Recherche web** (via Tavily API)
✅ **Analyse de données** (extraction de tendances)
✅ **Mémoire conversationnelle** (contexte persistant)
✅ **Outils personnalisables** (ajoutez vos propres outils)

---

## 🛠 **Prérequis**

### 1. **Python 3.10+**
Vérifiez votre version de Python :
```bash
python --version
```

### 2. **Clés API**
Vous avez besoin de deux clés API :
- **Tavily API** (pour la recherche web) : [S'inscrire ici](https://tavily.com/)
- **Mistral API** (pour le LLM) : [S'inscrire ici](https://console.mistral.ai/)

### 3. **Variables d'environnement**
Copiez le fichier `.env.example` en `.env` et ajoutez vos clés API :
```bash
cp .env.example .env
```

Éditez `.env` avec vos clés :
```env
TAVILY_API_KEY=votre_cle_api_tavily
MISTRAL_API_KEY=votre_cle_api_mistral
```

---

## 🚀 **Installation**

1. **Créer un environnement virtuel** (recommandé) :
   ```bash
   python -m venv venv
   source venv/bin/activate  # Sur Linux/Mac
   # OU
   venv\Scripts\activate   # Sur Windows
   ```

2. **Installer les dépendances** :
   ```bash
   pip install -r requirements.txt
   ```

---

## 🏃 **Exécution**

### Lancer l'agent :
```bash
python agent_recherche.py
```

### Exemple de sortie :
```
🚀 Agent IA de recherche et analyse - Démarrage...

🔍 Question: Quelles sont les dernières tendances IA en 2025 ?

--- Réponse de l'agent ---

## 🔮 Tendances IA en 2025

### 📈 Modèles Multi-Modaux
- **Intégration profonde** : Les modèles comme **Mistral-3** ou **GPT-5** combinent texte, image, audio et vidéo en temps réel.
- **Applications** : Génération de vidéos à partir de texte (ex: Sora 2.0), assistants vocaux hyper-personnalisés.

### 🤖 Agents Autonomes
- **Auto-amélioration** : Les agents peuvent **modifier leur propre code** (ex: AutoGen de Microsoft).
- **Collaboration** : Des équipes d’agents spécialisés (ex: un agent pour la recherche, un autre pour l’analyse).

### 🧠 IA Éthique et Régulation
- **Loi européenne sur l’IA** : Obligation de transparence pour les modèles à haut risque.
- **Outils de détection** : Détection des deepfakes (ex: Hugging Face Detect).

### 🔗 Sources
1. [Article sur Mistral-3 (Tavily)](https://...)
2. [Rapport de l’UE sur l’IA (2025)](https://...)
```

---

## 📂 **Structure du Projet**
```
python/
├── agent_recherche.py   # Code principal de l'agent
├── requirements.txt     # Dépendances Python
├── .env.example         # Exemple de fichier d'environnement
└── README.md            # Documentation
```

---

## 🔧 **Personnalisation**

### 1. **Ajouter un nouvel outil**
Pour ajouter un outil personnalisé, utilisez le décorateur `@tool` :
```python
from langchain_core.tools import tool

@tool
def mon_outil(param1: str) -> str:
    """Description de mon outil."""
    # Logique de l'outil
    return "Résultat"
```

Puis ajoutez-le à la liste `tools` avant de créer l'agent :
```python
tools = [recherche_web, analyser_tendances, mon_outil]
```

### 2. **Changer le modèle LLM**
Pour utiliser un autre modèle (ex: OpenAI) :
```python
from langchain_openai import ChatOpenAI

llm = ChatOpenAI(
    api_key=os.getenv("OPENAI_API_KEY"),
    model="gpt-4",
    temperature=0.7
)
```

### 3. **Améliorer la mémoire**
Pour une mémoire plus avancée (ex: stockage en base de données) :
```python
from langchain.memory import PostgresChatMessageHistory

# Configuration pour PostgreSQL
memory = ConversationBufferMemory(
    chat_memory=PostgresChatMessageHistory(
        connection_string="postgresql://user:password@localhost:5432/db"
    )
)
```

---

## 🌍 **Cas d'Usage**

| Domaine          | Exemple d’Utilisation                          | Adaptations Nécessaires               |
|------------------|-----------------------------------------------|----------------------------------------|
| **E-commerce**   | Recommandation de produits basée sur les tendances | Ajouter un outil d'accès à une base de produits |
| **Santé**        | Analyse de symptômes (avec prudence)          | Ajouter une base de connaissances médicales |
| **Finance**      | Suivi des marchés boursiers                   | Intégrer une API financière (ex: Alpha Vantage) |
| **Éducation**    | Tuteur virtuel pour expliquer des concepts    | Ajouter une base de connaissances éducatives |
| **DevOps**       | Automatisation de tâches (déploiement, logs)   | Intégrer GitHub API, Docker, etc.       |

---

## 📚 **Ressources**
- [Documentation LangChain](https://python.langchain.com/docs/get_started/introduction)
- [API Tavily](https://docs.tavily.com/)
- [API Mistral AI](https://docs.mistral.ai/)
- [LangChain Tools](https://python.langchain.com/docs/modules/tools/)
- [LangChain Agents](https://python.langchain.com/docs/modules/agents/)

---

## ⚠ **Bonnes Pratiques**
1. **Sécurité** :
   - Ne jamais commiter vos clés API dans le code.
   - Utilisez toujours des variables d'environnement (`.env`).
   - Validez les entrées/sorties pour éviter les injections de prompts.

2. **Performance** :
   - Limitez le nombre d’appels aux LLM (coûteux).
   - Utilisez des caches pour les résultats fréquents.

3. **Éthique** :
   - Respectez le RGPD et les lois locales sur l’IA.
   - Ajoutez des garde-fous contre les biais.

4. **Maintenabilité** :
   - Structurez votre code en modules.
   - Documentez chaque outil et agent.

---

## 🎯 **Prochaines Étapes**
1. **Tester l’exemple** avec vos propres clés API.
2. **Ajouter un outil personnalisé** (ex: appel à une API interne).
3. **Déployer l’agent** en tant qu’API avec FastAPI :
   ```python
   from fastapi import FastAPI
   app = FastAPI()
   
   @app.post("/ask")
   def ask_question(question: str):
       return {"answer": poser_question(question)}
   ```
4. **Explorer d’autres frameworks** :
   - [AutoGen](https://github.com/microsoft/autogen) (Microsoft)
   - [CrewAI](https://github.com/joaomdmoura/crewAI) (pour les équipes d’agents)
   - [LlamaIndex](https://github.com/run-llama/llama_index) (pour les bases de connaissances)

---

## 💬 **Auteur**
Ce projet a été généré par **Vibe Code** (Mistral AI) pour illustrer le développement agentique en IA.

Pour toute question ou suggestion, n'hésitez pas à contribuer ! 🚀
