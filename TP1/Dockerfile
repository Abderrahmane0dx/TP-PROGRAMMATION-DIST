# Image de base : Java 17 (JRE suffit pour exécuter le jar)
FROM eclipse-temurin:17-jre

# Dossier de travail dans le conteneur
WORKDIR /app

# Copie du jar produit par : ./gradlew bootJar
COPY build/libs/rental-0.0.1-SNAPSHOT.jar app.jar

# Port utilisé par Spring Boot
EXPOSE 8080

# Commande de lancement
ENTRYPOINT ["java", "-jar", "app.jar"]
