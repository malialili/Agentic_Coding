import axios from 'axios';

// Configuration de base pour axios
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Intercepteur pour ajouter le sessionId aux requêtes
api.interceptors.request.use((config) => {
  const sessionId = localStorage.getItem('sessionId');
  if (sessionId) {
    config.headers['X-Session-ID'] = sessionId;
  }
  return config;
});

// Intercepteur pour gérer les erreurs
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      // Erreur avec réponse du serveur
      console.error('API Error:', error.response.data);
      return Promise.reject(error.response.data);
    } else if (error.request) {
      // Erreur sans réponse (timeout, etc.)
      console.error('API Error: No response received');
      return Promise.reject({ message: 'Pas de réponse du serveur' });
    } else {
      // Erreur de configuration
      console.error('API Error:', error.message);
      return Promise.reject({ message: error.message });
    }
  }
);

// ============================================================================
// Services pour l'Agent IA
// ============================================================================

export const agentService = {
  /**
   * Pose une question à l'agent IA
   * @param {string} question - La question à poser
   * @param {string} sessionId - ID de session optionnel
   * @returns {Promise} - La réponse de l'agent
   */
  askQuestion: (question, sessionId = null) => {
    return api.post('/agent/ask', { question, sessionId });
  },

  /**
   * Pose une question simple (sans session)
   * @param {string} question - La question à poser
   * @returns {Promise} - La réponse de l'agent
   */
  askSimpleQuestion: (question) => {
    return api.get('/agent/ask', { params: { question } });
  },

  /**
   * Récupère la liste des outils disponibles
   * @returns {Promise} - Liste des outils
   */
  getAvailableTools: () => {
    return api.get('/agent/tools');
  },

  /**
   * Efface la mémoire d'une session
   * @param {string} sessionId - ID de la session à effacer
   * @returns {Promise} - Confirmation
   */
  clearSessionMemory: (sessionId) => {
    return api.delete(`/agent/sessions/${sessionId}`);
  },

  /**
   * Efface toute la mémoire
   * @returns {Promise} - Confirmation
   */
  clearAllMemory: () => {
    return api.delete('/agent/memory');
  },

  /**
   * Récupère les statistiques des sessions
   * @returns {Promise} - Statistiques
   */
  getSessionStats: () => {
    return api.get('/agent/stats');
  },
};

// ============================================================================
// Services pour la Base de Connaissances
// ============================================================================

export const knowledgeService = {
  /**
   * Récupère tous les documents de la base de connaissances
   * @returns {Promise} - Liste des documents
   */
  getAllDocuments: () => {
    return api.get('/knowledge/documents');
  },

  /**
   * Récupère un document par ID
   * @param {string} id - ID du document
   * @returns {Promise} - Le document
   */
  getDocumentById: (id) => {
    return api.get(`/knowledge/documents/${id}`);
  },

  /**
   * Crée un nouveau document
   * @param {Object} document - Le document à créer
   * @returns {Promise} - Le document créé
   */
  createDocument: (document) => {
    return api.post('/knowledge/documents', document);
  },

  /**
   * Met à jour un document
   * @param {string} id - ID du document
   * @param {Object} document - Les données mises à jour
   * @returns {Promise} - Le document mis à jour
   */
  updateDocument: (id, document) => {
    return api.put(`/knowledge/documents/${id}`, document);
  },

  /**
   * Supprime un document
   * @param {string} id - ID du document
   * @returns {Promise} - Confirmation
   */
  deleteDocument: (id) => {
    return api.delete(`/knowledge/documents/${id}`);
  },

  /**
   * Effectue une recherche dans la base de connaissances
   * @param {string} query - La requête de recherche
   * @returns {Promise} - Résultats de la recherche
   */
  searchDocuments: (query) => {
    return api.get('/knowledge/search', { params: { q: query } });
  },

  /**
   * Récupère les catégories disponibles
   * @returns {Promise} - Liste des catégories
   */
  getCategories: () => {
    return api.get('/knowledge/categories');
  },

  /**
   * Importe des documents en masse
   * @param {File} file - Fichier à importer
   * @returns {Promise} - Résultat de l'import
   */
  importDocuments: (file) => {
    const formData = new FormData();
    formData.append('file', file);
    return api.post('/knowledge/import', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },

  /**
   * Exporte les documents
   * @returns {Promise} - Fichier exporté
   */
  exportDocuments: () => {
    return api.get('/knowledge/export', { responseType: 'blob' });
  },
};

// ============================================================================
// Services pour les Paramètres
// ============================================================================

export const settingsService = {
  /**
   * Récupère les paramètres de l'application
   * @returns {Promise} - Les paramètres
   */
  getSettings: () => {
    return api.get('/settings');
  },

  /**
   * Met à jour les paramètres
   * @param {Object} settings - Les paramètres à mettre à jour
   * @returns {Promise} - Les paramètres mis à jour
   */
  updateSettings: (settings) => {
    return api.put('/settings', settings);
  },

  /**
   * Teste la connexion à l'API Mistral
   * @returns {Promise} - Résultat du test
   */
  testMistralConnection: () => {
    return api.get('/settings/test-mistral');
  },

  /**
   * Teste la connexion à l'API Tavily
   * @returns {Promise} - Résultat du test
   */
  testTavilyConnection: () => {
    return api.get('/settings/test-tavily');
  },
};

export default api;
