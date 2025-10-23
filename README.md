# Blackjack WebFlux API

Reactive Java Spring Boot **(WebFlux)** application for a simplified Blackjack game.
- Databases: **MongoDB** (games), **MySQL** (players via R2DBC)
- **Swagger UI**: /swagger-ui.html
- Docker + docker-compose included

## Project structure
```
blackjack-webflux/
├─ src/main/java/com/example/blackjack/
│  ├─ BlackjackApplication.java
│  ├─ config/
│  │  ├─ SwaggerConfig.java
│  │  └─ DatabaseConfig.java
│  ├─ controller/
│  │  └─ GameController.java
│  ├─ service/
│  │  └─ GameService.java
│  ├─ repository/
│  │  ├─ mongo/
│  │  │  └─ GameRepository.java
│  │  └─ r2dbc/
│  │     └─ PlayerRepository.java
│  ├─ model/
│  │  ├─ mongo/
│  │  │  └─ Game.java
│  │  └─ r2dbc/
│  │     └─ Player.java
│  ├─ dto/
│  │  ├─ GameDTO.java
│  │  ├─ CreateGameRequest.java
│  │  ├─ PlayRequest.java
│  │  ├─ PlayerDTO.java
│  │  └─ ErrorDTO.java
│  ├─ mapper/
│  │  └─ GameMapper.java
│  ├─ exception/
│  │  ├─ GlobalExceptionHandler.java
│  │  └─ NotFoundException.java
│  └─ util/
│     └─ BlackjackLogic.java
├─ src/main/resources/
│  ├─ application.yml
│  ├─ schema.sql
│  └─ logback-spring.xml
├─ src/test/java/...
│  ├─ service/GameServiceTest.java
│  └─ controller/GameControllerTest.java
├─ Dockerfile
├─ docker-compose.yml
├─ pom.xml
└─ README.md
```

## Principal ENDPOINTS
- POST /game/new  -> create game
- GET /game/{id}
- POST /game/{id}/play  -> body: {"action": "hit"|"stand", "bet":0}
- DELETE /game/{id}/delete
- GET /game/ranking
- PUT /game/player/{playerId}  -> body: "NouNom"

## Local execution (maven)
1. `mvn clean package`
2. `docker-compose up --build`
3. Acces: http://localhost:8080

## Testing
`mvn test`

## Swagger UI access
http://localhost:8080/swagger-ui.html
or
http://localhost:8080/swagger-ui/index.html

1. **Launch the Containers:**

   Run the following command in the directory where you have 
   docker-compose.yml and your Dockerfile:
```
docker compose up --build -d
```
2. **Check Status:**

   Make sure all services are running or healthy:
```
docker compose ps
```
3. **Access to Swagger UI:**
   - STEP 1: Open your web browser
   - STEP 2: Navigate to the following URL:
```
http://localhost:8080/swagger-ui.html
```

## Docker steps
1. **Build the application**: ./mvnw package (Generate the JAR to target/)

2. **Build the image**: docker build -t blackjack-api:latest .

3. **Execute the image**:
   - You will need MySQL and MongoDB instances separately 
   (typically via docker-compose).

   - docker run -p 8080:8080 --name blackjack-app blackjack-api:latest

4. **Tag the image**: 
   - docker tag blackjack-api:latest 
   - dockerfile/blackjack-api:1.0

5. **Bid the image**: docker push dockerfile/blackjack-api:1.0

## Notes
- Simplified Blackjack logic; pots extend handing (split, double, insurance).
- To deploy to Render: create Dockerfile (incl.) and push the image to the container registry or use Docker Compose both services.
