# -----------------------------
# Étape 1 : Build Gradle
# -----------------------------
FROM gradle:8.1.1-jdk17-alpine AS build

# On définit le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le contenu du dossier 'encheres' dans /app
# (Si ton Dockerfile est au même niveau que le dossier encheres/)
COPY encheres/ /app

# Lancer la construction du projet
# Si ton build Gradle est configuré pour générer un .jar exécutable (Spring Boot : bootJar)
RUN gradle clean build -x test

# -----------------------------
# Étape 2 : Runtime
# -----------------------------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copier le .jar généré depuis l'étape "build"
# ATTENTION : vérifie le nom du .jar dans /app/build/libs/
COPY --from=build /app/build/libs/*.jar app.jar

# Expose le port 8080 (par défaut si Spring Boot écoute sur 8080)
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
