"""
Agent IA en Python pour la recherche web et l'analyse de tendances.
Utilise LangChain, Tavily (recherche web) et Mistral AI (LLM).

Auteur: Vibe Code (Mistral AI)
Date: 2025
"""

from typing import List, Dict, Optional
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.tools import tool
from langchain_community.tools.tavily_search import TavilySearchResults
from langchain_community.chat_models import ChatMistralAI
from langchain.agents import AgentExecutor, create_tool_calling_agent
from langchain.memory import ConversationBufferMemory
import os
from dotenv import load_dotenv

# --- 1. Chargement des variables d'environnement ---
load_dotenv()


# --- 2. Définition des outils (Tools) ---
@tool
def analyser_tendances(donnees: List[Dict]) -> str:
    """
    Analyse une liste de résultats de recherche pour en extraire les tendances.
    
    Args:
        donnees: Liste de dictionnaires contenant les résultats de recherche.
        
    Returns:
        str: Résumé des tendances extraites.
    """
    tendances = []
    for item in donnees:
        content = item.get("content", "")
        if "2025" in content:
            tendances.append(content[:200] + "...")  # Troncature pour éviter les réponses trop longues
    return "\n\n".join(tendances) if tendances else "Aucune tendance trouvée pour 2025."


# Outils externes (Tavily pour la recherche web)
recherche_web = TavilySearchResults(
    api_key=os.getenv("TAVILY_API_KEY"),
    max_results=3
)


# --- 3. Initialisation du LLM (Mistral via API) ---
llm = ChatMistralAI(
    api_key=os.getenv("MISTRAL_API_KEY"),
    model="mistral-tiny",  # Modèles disponibles: mistral-tiny, mistral-small, mistral-medium
    temperature=0.7
)


# --- 4. Définition du prompt de l'agent ---
prompt = ChatPromptTemplate.from_template(
    """Tu es un assistant IA spécialisé dans l'analyse de tendances technologiques.
    Utilise les outils disponibles pour répondre précisément à la question de l'utilisateur.
    Si tu n'as pas assez d'informations, fais une recherche web.

    **Règles :**
    - Réponds en français.
    - Structure ta réponse en sections (ex: Tendances, Sources, Recommandations).
    - Cite toujours tes sources.

    **Contexte actuel :**
    {context}

    **Question :** {input}
    """
)


# --- 5. Mémoire pour le suivi de la conversation ---
memory = ConversationBufferMemory(
    memory_key="context",
    return_messages=True
)


# --- 6. Création de l'agent ---
tools = [recherche_web, analyser_tendances]
agent = create_tool_calling_agent(
    llm=llm,
    tools=tools,
    prompt=prompt
)


# --- 7. Exécuteur de l'agent ---
agent_executor = AgentExecutor(
    agent=agent,
    tools=tools,
    memory=memory,
    verbose=True  # Affiche les étapes de raisonnement
)


def poser_question(question: str, session_id: Optional[str] = None) -> str:
    """
    Pose une question à l'agent et retourne sa réponse.
    
    Args:
        question: La question à poser à l'agent.
        session_id: ID de session pour la mémoire (optionnel).
        
    Returns:
        str: Réponse de l'agent.
    """
    # Si un session_id est fourni, on peut l'utiliser pour gérer la mémoire
    # (Note: ConversationBufferMemory gère automatiquement le contexte)
    return agent_executor.invoke({"input": question})["output"]


# --- 8. Exemple d'utilisation ---
if __name__ == "__main__":
    print("🚀 Agent IA de recherche et analyse - Démarrage...\n")
    
    # Exemple 1: Question simple
    question = "Quelles sont les dernières tendances IA en 2025 ?"
    print(f"🔍 Question: {question}\n")
    
    reponse = poser_question(question)
    print("\n--- Réponse de l'agent ---\n")
    print(reponse)
    
    # Exemple 2: Suite de la conversation (la mémoire conserve le contexte)
    print("\n" + "="*50)
    question_suite = "Peux-tu me donner plus de détails sur les agents autonomes ?"
    print(f"\n🔍 Question suivante: {question_suite}\n")
    
    reponse_suite = poser_question(question_suite)
    print("\n--- Réponse de l'agent ---\n")
    print(reponse_suite)
