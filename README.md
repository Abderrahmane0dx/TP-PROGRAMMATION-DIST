# TP Programmation Distribuée – Service REST de location de voitures

Master MLSD – Abderrahmane BENAMARA

Mini application **Spring Boot** (Java 17, Gradle) exposant un web service REST pour louer des voitures, conteneurisée avec **Docker** et publiée sur **Docker Hub**.

- **Code source** : [TP1/](TP1/)
- **Image Docker Hub** : https://hub.docker.com/r/benamaraabderrahmane/rental

---

## 1. Fonctionnalités (API REST)

| Fonctionnalité | Méthode | URI | Corps (JSON) | Réponse |
|---|---|---|---|---|
| Liste des voitures **non louées** | `GET` | `/cars` | – | `200` + liste JSON |
| Caractéristiques d'une voiture | `GET` | `/cars/{plateNumber}` | – | `200` + voiture, `404` si inconnue |
| Louer une voiture | `PUT` | `/cars/{plateNumber}?rent=true` | `{"begin":"11/11/2017","end":"1/1/2018"}` | `200`, `409` si déjà louée, `400` si dates absentes |
| Rendre une voiture | `PUT` | `/cars/{plateNumber}?rent=false` | – | `200` |

Voitures disponibles au démarrage : `11AA22` (Ferrari, 100), `33BB44` (Porsche, 80), `55CC66` (Renault, 30).

### Structure du code

```
TP1/src/main/java/com/example/rental/
├── RentalApplication.java        # point d'entrée Spring Boot
├── entity/
│   ├── Car.java                  # une voiture (plaque, marque, prix, état de location)
│   └── Dates.java                # dates de location reçues dans le corps du PUT
└── web/
    └── RentalWebService.java     # @RestController : les endpoints ci-dessus
```

---

## 2. Lancer l'application

### Option A – avec l'image Docker Hub (le plus simple)

```bash
docker run -p 8080:8080 benamaraabderrahmane/rental:1.0
```

### Option B – depuis le code source

```bash
cd TP1
./gradlew bootRun
```

L'application écoute sur http://localhost:8080.

---

## 3. Tester l'API

```bash
# 1. Liste des voitures disponibles
curl http://localhost:8080/cars

# 2. Détail d'une voiture
curl http://localhost:8080/cars/11AA22

# 3. Louer la Ferrari
curl -X PUT "http://localhost:8080/cars/11AA22?rent=true" \
     -H "Content-Type: application/json" \
     -d '{"begin":"11/11/2017","end":"1/1/2018"}'

# 4. La Ferrari n'apparaît plus dans la liste
curl http://localhost:8080/cars

# 5. La relouer renvoie 409 Conflict
curl -i -X PUT "http://localhost:8080/cars/11AA22?rent=true" \
     -H "Content-Type: application/json" \
     -d '{"begin":"1/1/2019","end":"2/2/2019"}'

# 6. Rendre la Ferrari
curl -X PUT "http://localhost:8080/cars/11AA22?rent=false"

# 7. Voiture inconnue : 404
curl -i http://localhost:8080/cars/ZZ99ZZ
```

<!-- Captures d'écran : ajouter ici les résultats des commandes ci-dessus -->

---

## 4. Docker

### Dockerfile

```dockerfile
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY build/libs/rental-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Construire l'image

```bash
cd TP1
./gradlew bootJar                                   # produit build/libs/rental-0.0.1-SNAPSHOT.jar
docker build -t benamaraabderrahmane/rental:1.0 .
```

### Lancer le conteneur

```bash
docker run -p 8080:8080 benamaraabderrahmane/rental:1.0
```

### Publier sur Docker Hub

```bash
docker login
docker push benamaraabderrahmane/rental:1.0
docker tag benamaraabderrahmane/rental:1.0 benamaraabderrahmane/rental:latest
docker push benamaraabderrahmane/rental:latest
```

Image publiée : https://hub.docker.com/r/benamaraabderrahmane/rental
