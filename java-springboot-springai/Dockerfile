# Backend Dockerfile for Spring Boot application
FROM eclipse-temurin:21-jdk-jammy AS build

# Set working directory
WORKDIR /app

# Copy build files
COPY pom.xml ./
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Production stage
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Copy built JAR
COPY --from=build /app/target/*.jar app.jar

# Copy application properties
COPY --from=build /app/src/main/resources/application.yml .
COPY --from=build /app/src/main/resources/application-postgresql.yml .

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/agentic_db
ENV SPRING_DATASOURCE_USERNAME=agentic_user
ENV SPRING_DATASOURCE_PASSWORD=agentic_password

# Start the application
CMD ["java", "-jar", "app.jar"]
