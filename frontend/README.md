# 🖥️ Frontend React pour l'Agent IA

Ce projet est le **frontend React** pour interagir avec l'agent IA et la base de connaissances. Il utilise :
- **React 18** pour la gestion de l'interface utilisateur
- **Chakra UI** pour les composants UI et le design
- **React Query** pour la gestion des requêtes API
- **React Router** pour la navigation
- **React Markdown** pour l'affichage des réponses formatées

---

## 📌 **Fonctionnalités**

✅ **Interface de chat** moderne et intuitive
✅ **Base de connaissances** avec gestion des documents
✅ **Recherche avancée** (textuelle et sémantique)
✅ **Gestion des sessions** (mémoire conversationnelle)
✅ **Paramètres** pour configurer les connexions API
✅ **Design responsive** (mobile, tablette, desktop)
✅ **Thème clair/sombre** (via Chakra UI)

---

## 🛠 **Prérequis**

### 1. **Node.js 18+**
Vérifiez votre version de Node.js :
```bash
node -v
```

### 2. **npm 9+** ou **yarn 1.22+**
Vérifiez votre version de npm :
```bash
npm -v
```

---

## 🚀 **Installation et Exécution**

### 1. **Installer les dépendances**
```bash
cd frontend
npm install
```

### 2. **Configurer l'URL de l'API**

#### Option 1 : Modifier `.env` (recommandé)
Créez un fichier `.env` dans le dossier `frontend/` :
```bash
cp .env.example .env
```

Éditez `.env` :
```env
REACT_APP_API_URL=http://localhost:8081/api
```

#### Option 2 : Utiliser le proxy intégré
Par défaut, le frontend est configuré pour utiliser un proxy vers `/api` (voir `package.json`).
Si votre backend tourne sur `http://localhost:8081`, aucune configuration supplémentaire n'est nécessaire.

---

### 3. **Démarrer le frontend**
```bash
npm start
```

Le frontend démarrera sur `http://localhost:3000`.

---

## 🌐 **Pages Disponibles**

| URL | Description |
|-----|-------------|
| `/` ou `/chat` | Page de chat avec l'agent IA |
| `/knowledge` | Gestion de la base de connaissances |
| `/settings` | Configuration des paramètres |

---

## 📂 **Structure du Projet**

```
frontend/
├── public/                          # Fichiers statiques
│   └── index.html                  # Point d'entrée HTML
│
├── src/
│   ├── index.jsx                   # Point d'entrée de l'application
│   ├── index.css                   # Styles globaux
│   ├── App.jsx                     # Composant principal avec routage
│   │
│   ├── components/                 # Composants réutilisables
│   │   ├── Sidebar.jsx             # Barre latérale de navigation
│   │   ├── ChatMessage.jsx         # Affichage d'un message de chat
│   │   ├── ChatInput.jsx           # Champ de saisie du chat
│   │   └── KnowledgeCard.jsx        # Carte pour un document de la base de connaissances
│   │
│   ├── pages/                      # Pages de l'application
│   │   ├── ChatPage.jsx            # Page de chat
│   │   ├── KnowledgeBasePage.jsx   # Page de gestion de la base de connaissances
│   │   └── SettingsPage.jsx        # Page des paramètres
│   │
│   └── services/                   # Services API
│       └── api.js                  # Configuration des appels API
│
├── package.json                    # Dépendances et scripts
└── README.md                       # Documentation
```

---

## 🔌 **Intégration avec le Backend**

### **Endpoints Utilisés**

#### **Agent IA**
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/agent/ask` | Pose une question à l'agent |
| GET | `/agent/ask` | Pose une question simple |
| GET | `/agent/tools` | Liste les outils disponibles |
| DELETE | `/agent/sessions/{id}` | Efface une session |
| DELETE | `/agent/memory` | Efface toute la mémoire |
| GET | `/agent/stats` | Statistiques des sessions |

#### **Base de Connaissances**
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/knowledge/documents` | Liste tous les documents |
| GET | `/knowledge/documents/{id}` | Récupère un document |
| POST | `/knowledge/documents` | Crée un document |
| PUT | `/knowledge/documents/{id}` | Met à jour un document |
| DELETE | `/knowledge/documents/{id}` | Supprime un document |
| POST | `/knowledge/search` | Recherche dans les documents |
| GET | `/knowledge/categories` | Liste les catégories |
| POST | `/knowledge/import` | Importe des documents |
| GET | `/knowledge/export` | Exporte les documents |
| GET | `/knowledge/stats` | Statistiques des documents |

---

## 🎨 **Composants Clés**

### **1. `ChatPage.jsx`**
- **Fonctionnalités** :
  - Affichage des messages de chat (utilisateur et agent)
  - Saisie et envoi des messages
  - Gestion des sessions
  - Affichage des erreurs
  - Scroll automatique

- **Exemple d'utilisation** :
  ```jsx
  <ChatPage />
  ```

---

### **2. `KnowledgeBasePage.jsx`**
- **Fonctionnalités** :
  - Liste des documents avec pagination
  - Recherche textuelle et par catégorie
  - Ajout/Modification/Suppression de documents
  - Import/Export de documents
  - Affichage des métadonnées

- **Exemple d'utilisation** :
  ```jsx
  <KnowledgeBasePage />
  ```

---

### **3. `api.js`**
Service centralisé pour tous les appels API. Utilise **axios** avec :
- **Intercepteurs** pour ajouter le `sessionId` aux requêtes
- **Gestion des erreurs** centralisée
- **Configuration de base** (URL, timeout, headers)

**Exemple d'utilisation** :
```jsx
import { agentService, knowledgeService } from './services/api';

// Pose une question à l'agent
const response = await agentService.askQuestion("Quelles sont les tendances IA ?");

// Recherche dans la base de connaissances
const results = await knowledgeService.searchDocuments({ query: "IA" });
```

---

## 🎨 **Personnalisation du Design**

### **1. Thème Chakra UI**
Le thème est configuré dans `src/index.jsx` :
```jsx
const theme = extendTheme({
  colors: {
    primary: {
      500: '#3182ce', // Bleu principal
    },
  },
  fonts: {
    heading: 'Inter, sans-serif',
    body: 'Inter, sans-serif',
  },
});
```

Pour modifier les couleurs :
- **Couleur principale** : Modifiez `primary.500`
- **Couleurs secondaires** : Ajoutez d'autres palettes dans `colors`

---

### **2. Styles Globaux**
Les styles globaux sont définis dans `src/index.css` :
```css
/* Exemple : modifier la couleur de fond */
body {
  background-color: #f7fafc;
}

/* Exemple : modifier la police */
html {
  font-family: 'Inter', sans-serif;
}
```

---

### **3. Composants Personnalisés**
Pour ajouter un nouveau composant :
1. Créez un fichier dans `src/components/` (ex: `MyComponent.jsx`)
2. Importez-le dans la page souhaitée
3. Utilisez les composants Chakra UI pour un design cohérent

**Exemple** :
```jsx
// src/components/MyComponent.jsx
import { Box, Text } from '@chakra-ui/react';

const MyComponent = ({ title, content }) => {
  return (
    <Box p={4} borderRadius="lg" boxShadow="sm" bg="white">
      <Text fontSize="xl" fontWeight="bold" mb={2}>{title}</Text>
      <Text color="gray.600">{content}</Text>
    </Box>
  );
};

export default MyComponent;
```

---

## 🌍 **Internationalisation (i18n)**

Actuellement, l'interface est en **français**. Pour ajouter le support de l'anglais :

1. **Installer react-i18next** :
   ```bash
   npm install react-i18next i18next
   ```

2. **Configurer i18n** :
   Créez un fichier `src/i18n.js` :
   ```jsx
   import i18n from 'i18next';
   import { initReactI18next } from 'react-i18next';
   
   i18n.use(initReactI18next).init({
     resources: {
       fr: { translation: require('./locales/fr/translation.json') },
       en: { translation: require('./locales/en/translation.json') },
     },
     lng: 'fr',
     fallbackLng: 'fr',
     interpolation: { escapeValue: false },
   });
   
   export default i18n;
   ```

3. **Utiliser les traductions** :
   ```jsx
   import { useTranslation } from 'react-i18next';
   
   const MyComponent = () => {
     const { t } = useTranslation();
     return <Text>{t('welcome')}</Text>;
   };
   ```

---

## ⚠ **Bonnes Pratiques**

### **1. Gestion des États**
- **Utilisez React Query** pour les requêtes API (déjà configuré)
- **Évitez useState** pour les données complexes (préférez useReducer)
- **Séparez la logique** : Utilisez des hooks personnalisés pour la logique réutilisable

---

### **2. Performances**
- **Memoization** : Utilisez `React.memo` pour les composants purs
- **Lazy Loading** : Chargez les pages de manière paresseuse
  ```jsx
  const ChatPage = React.lazy(() => import('./pages/ChatPage'));
  ```
- **Virtualization** : Pour les longues listes, utilisez `react-window` ou `react-virtualized`

---

### **3. Sécurité**
- **Ne stockez pas de données sensibles** dans le localStorage
- **Validez les entrées** côté frontend et backend
- **Utilisez HTTPS** en production

---

### **4. Accessibilité**
- **Utilisez les composants Chakra UI** (ils sont accessibles par défaut)
- **Ajoutez des labels** à tous les champs de formulaire
- **Gérez le focus** pour les modals et menus déroulants

---

## 🚀 **Déploiement**

### **1. Build pour la Production**
```bash
npm run build
```

Cela génère un dossier `build/` avec les fichiers optimisés.

---

### **2. Déploiement sur Netlify**
1. Poussez votre code sur GitHub
2. Connectez votre dépôt à Netlify
3. Configurez les variables d'environnement (`REACT_APP_API_URL`)
4. Déployez !

---

### **3. Déploiement sur Vercel**
```bash
npm install -g vercel
vercel
```

---

### **4. Déploiement avec Docker**
Créez un `Dockerfile` :
```dockerfile
# Build stage
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Production stage
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

Construisez et exécutez :
```bash
docker build -t agentic-ai-frontend .
docker run -p 80:80 agentic-ai-frontend
```

---

## 📚 **Ressources**

### **Documentations**
- [React Documentation](https://react.dev/)
- [Chakra UI Documentation](https://chakra-ui.com/docs)
- [React Query Documentation](https://tanstack.com/query/latest/docs/react/overview)
- [React Router Documentation](https://reactrouter.com/en/main)
- [React Markdown](https://github.com/remarkjs/react-markdown)

### **Tutoriels**
- [Créer une app React moderne](https://react.dev/learn)
- [Chakra UI Crash Course](https://chakra-ui.com/docs/getting-started)
- [React Query Tutorial](https://tanstack.com/query/latest/docs/react/quick-start)

---

## 🎯 **Prochaines Étapes**

1. **Tester le frontend** avec votre backend :
   - Démarrez le backend (`java-springboot-springai`)
   - Démarrez le frontend (`npm start`)
   - Ouvrez `http://localhost:3000`

2. **Personnaliser le design** :
   - Modifier le thème dans `src/index.jsx`
   - Ajouter des composants personnalisés
   - Adapter les couleurs à votre charte graphique

3. **Ajouter des fonctionnalités** :
   - **Authentification** : Ajouter un système de login (JWT)
   - **Historique des conversations** : Stocker les conversations passées
   - **Partage de documents** : Permettre le partage entre utilisateurs
   - **Collaboration en temps réel** : Utiliser WebSockets

4. **Améliorer les performances** :
   - Implémenter le lazy loading des pages
   - Ajouter un cache pour les requêtes fréquentes
   - Optimiser les images

5. **Déployer en production** :
   - Configurer CI/CD avec GitHub Actions
   - Déployer sur Netlify, Vercel ou AWS
   - Configurer le monitoring

---

## 💬 **Auteur**

Ce frontend a été généré par **Vibe Code** (Mistral AI) pour compléter les exemples d'agents IA.

Pour toute question ou suggestion, n'hésitez pas à contribuer ! 🚀

---

## 📜 **Licence**

Ce projet est sous licence **MIT**. Vous êtes libre de l'utiliser, le modifier et le distribuer selon vos besoins.
