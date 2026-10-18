# 🐘 Configuration PostgreSQL pour le Système Agentique

**Branche:** `feature/postgresql`

Cette branche ajoute la **configuration PostgreSQL** pour remplacer la base de données H2 par défaut. Elle inclut également un **workflow CI/CD** pour tester automatiquement les projets.

---

## 📌 **Modifications Apportées**

### 1. **Configuration PostgreSQL**
- **`java-springboot-springai/src/main/resources/application-postgresql.yml`** : Configuration complète pour PostgreSQL
- **`java-springboot-springai/pom.xml`** : Ajout du driver PostgreSQL
- **`java-springboot-springai/src/main/java/com/example/agent/config/PostgreSQLConfig.java`** : Configuration Java pour PostgreSQL

### 2. **CI/CD Pipeline**
- **`.github/workflows/ci.yml`** : Workflow GitHub Actions pour :
  - Builder les projets Java avec Maven
  - Exécuter les tests unitaires
  - Builder le frontend React
  - Tester le frontend

---

## 🛠 **Comment Utiliser PostgreSQL ?**

### 1. **Installer PostgreSQL**

#### Sur Ubuntu/Debian :
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
```

#### Sur Mac (Homebrew) :
```bash
brew install postgresql
brew services start postgresql
```

#### Sur Windows :
Téléchargez et installez depuis [https://www.postgresql.org/download/windows/](https://www.postgresql.org/download/windows/)

---

### 2. **Créer la Base de Données**

```sql
-- Se connecter à PostgreSQL
psql -U postgres

-- Créer la base de données
CREATE DATABASE agentdb;

-- Créer un utilisateur (optionnel)
CREATE USER agentuser WITH PASSWORD 'agentpass';
GRANT ALL PRIVILEGES ON DATABASE agentdb TO agentuser;
```

---

### 3. **Configurer l'Application**

#### Option 1 : Utiliser le profil `prod`
```bash
mvn spring-boot:run --spring.profiles.active=prod
```

#### Option 2 : Modifier `application.yml`
Remplacez la configuration H2 par :
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/agentdb
    driver-class-name: org.postgresql.Driver
    username: postgres
    password: postgres
  
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

---

### 4. **Variables d'Environnement**

Pour une configuration sécurisée, utilisez des variables d'environnement :

```bash
# Sur Linux/Mac
export DB_USERNAME=agentuser
export DB_PASSWORD=agentpass

# Dans application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/agentdb
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

---

## 🚀 **Exécution avec PostgreSQL**

1. **Démarrer PostgreSQL** :
   ```bash
   sudo systemctl start postgresql
   ```

2. **Créer la base de données** (voir section 2)

3. **Démarrer l'application** :
   ```bash
   cd java-springboot-springai
   mvn spring-boot:run --spring.profiles.active=prod
   ```

---

## 🔄 **Migration depuis H2**

### 1. **Exporter les données de H2**

Ajoutez cette dépendance temporaire dans `pom.xml` :
```xml
<dependency>
    <groupId>org.h2</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 2. **Utiliser Flyway ou Liquibase** (recommandé)

Pour une migration propre, utilisez **Flyway** :

1. Ajoutez Flyway dans `pom.xml` :
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

2. Créez un dossier `db/migration` avec des fichiers SQL de migration.

3. Configurez Flyway dans `application.yml` :
```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
```

---

## 🧪 **CI/CD Pipeline**

Le workflow `.github/workflows/ci.yml` :

- **Déclencheurs** :
  - Push sur `main` ou `feature/*`
  - Pull Request sur `main`

- **Services** :
  - PostgreSQL 15 en conteneur Docker

- **Étapes** :
  1. Checkout du code
  2. Configuration de JDK 21
  3. Build des projets Java (Spring Boot)
  4. Exécution des tests Java
  5. Configuration de Node.js 20
  6. Installation et test du frontend
  7. Build du frontend

### **Secrets GitHub requis** :
- `MISTRAL_API_KEY` : Clé API pour Mistral AI
- `TAVILY_API_KEY` : Clé API pour Tavily

---

## 📝 **Exemple de Fichier de Migration Flyway**

Créez `java-springboot-springai/src/main/resources/db/migration/V1__Create_tables.sql` :

```sql
-- Créer la table des documents
CREATE TABLE IF NOT EXISTS documents (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(100),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Créer la table des tags de documents
CREATE TABLE IF NOT EXISTS document_tags (
    document_id VARCHAR(36) NOT NULL,
    tag VARCHAR(255) NOT NULL,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);
```

---

## ⚠ **Bonnes Pratiques**

1. **Sauvegardes** : Effectuez des sauvegardes régulières de votre base PostgreSQL
   ```bash
   pg_dump -U postgres -d agentdb -F c -f agentdb_backup.dump
   ```

2. **Sécurité** :
   - Utilisez des mots de passe forts
   - Limitez les permissions des utilisateurs
   - Activez SSL pour les connexions distantes

3. **Performance** :
   - Créez des index sur les colonnes fréquemment interrogées
   - Utilisez `EXPLAIN ANALYZE` pour optimiser les requêtes
   - Configurez le pool de connexions

4. **Monitoring** :
   - Utilisez pgAdmin ou psql pour surveiller la base
   - Configurez des alertes pour les erreurs

---

## 🎯 **Prochaines Étapes**

1. **Configurer Flyway/Liquibase** pour les migrations
2. **Ajouter un Vector Store** (ChromaDB, FAISS) pour la recherche sémantique
3. **Déployer sur un cloud** (AWS RDS, Google Cloud SQL, Azure Database)
4. **Configurer un pool de connexions** (HikariCP)
5. **Ajouter des index** pour optimiser les performances

---

## 📚 **Ressources**

- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Spring Data JPA with PostgreSQL](https://spring.io/guides/gs/accessing-data-rest/)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP)

---

## 💬 **Auteur**

Configuration PostgreSQL ajoutée par **Vibe Code** (Mistral AI).

---

## 📜 **Licence**

MIT License
