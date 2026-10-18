# Frontend Deployment

This directory contains the React frontend for the Agentic AI application.

## Deployment Options

### Option 1: Netlify

1. **Install Netlify CLI:**
   ```bash
   npm install -g netlify-cli
   ```

2. **Login to Netlify:**
   ```bash
   netlify login
   ```

3. **Deploy:**
   ```bash
   netlify deploy --prod
   ```

4. **Or use the GitHub Action:**
   - Set `NETLIFY_AUTH_TOKEN` and `NETLIFY_SITE_ID` secrets in GitHub
   - Push to main branch to trigger automatic deployment

### Option 2: Vercel

1. **Install Vercel CLI:**
   ```bash
   npm install -g vercel
   ```

2. **Login to Vercel:**
   ```bash
   vercel login
   ```

3. **Deploy:**
   ```bash
   vercel --prod
   ```

4. **Or use the GitHub Action:**
   - Set `VERCEL_TOKEN`, `VERCEL_ORG_ID`, and `VERCEL_PROJECT_ID` secrets
   - Push to main branch to trigger automatic deployment

### Option 3: Docker

1. **Build the image:**
   ```bash
   docker build -t agentic-ai-frontend .
   ```

2. **Run the container:**
   ```bash
   docker run -p 80:80 agentic-ai-frontend
   ```

3. **Or use Docker Compose:**
   ```yaml
   version: '3.8'
   services:
     frontend:
       build: ./frontend
       ports:
         - "80:80"
       depends_on:
         - backend
   ```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `REACT_APP_API_URL` | Backend API URL | `http://localhost:8080` |
| `REACT_APP_MISTRAL_API_KEY` | Mistral API key | - |
| `REACT_APP_TAVILY_API_KEY` | Tavily API key | - |

## Configuration

### API Endpoints

The frontend expects the following API endpoints:
- `GET /api/agents` - List all agents
- `POST /api/agents` - Create a new agent
- `GET /api/agents/{id}` - Get agent details
- `POST /api/agents/{id}/execute` - Execute an agent
- `GET /api/documents` - List all documents
- `POST /api/documents` - Upload a document
- `POST /api/documents/search` - Search documents

### Proxy Configuration

For development, add this to `package.json`:
```json
"proxy": "http://localhost:8080"
```

## Scripts

| Script | Description |
|--------|-------------|
| `npm start` | Start development server |
| `npm run build` | Build for production |
| `npm test` | Run tests |
| `npm run eject` | Eject from create-react-app |
