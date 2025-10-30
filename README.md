# ♦️♣️ Blackjack WebFlux API  ️♠️♥️

Reactive **Java Spring Boot (WebFlux)** application for a simplified Blackjack game.
- Databases: 
  - **MongoDB**: collection `games` (stores game sessions).
  - **MySQL**: table `player` (stores players, stats, and balance).
- **Swagger UI**: /swagger-ui.html (interactive documentation)
- **Docker** + **docker-compose** included

## ⚙️ Requirements

- Java 21
- Maven 3.9+
- Docker & Docker ComposeS

---

## Project structure
```
blackjack-webflux/
├─ src/main/java/com/example/blackjack/
│  ├─ BlackjackApplication.java
│  ├─ config/
│  │  └─ OpenApiConfig.java
│  │  
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

---

### API ENDPOINTS REFERENCE
The base URL for the API is http://localhost:8080/swagger-ui.html

| Operation | HTTP Method | Endpoint | Description                                   | Status Codes                      |
|------------|-------------|-----------|-----------------------------------------------|-----------------------------------|
| **Create** | POST        | `/game/new` | Creates a new game.                           | `201 Created`, `400 Bad Request`  |
| **Read One** | GET         | `/game/{id}` | Retrieves a single game by ID.                | `200 OK`, `404 Not Found`         |
| **Read One** | POST        | `/game/{id}/play` | Make a move (`HIT`, `STAND`, `DOUBLE`).                      | `201 Created`, `400 Bad Request`  | `200 OK`, `404 Not Found` |
| **Read All** | GET         | `/game/ranking` | Retrieves a ranked list of all players.       | `200 OK`                          |
| **Update** | PUT         | `/game/player/{playerId}` | Updates an existing Player (body: "newName"). | `200 OK`, `404 Not Found`         |
| **Delete** | DELETE      | `/game/{id}/delete` | Deletes a game by ID.                         | `204 No Content`, `404 Not Found` |
---

## Local execution (maven)
1. `mvn clean package`
2. `docker-compose up --build`
3. Acces: http://localhost:8080

---

## Testing
`mvn test`

---

## Swagger UI access
http://localhost:8080/swagger-ui.html

0. **Compile the project (generate JAR file):**
```
mvn clean install
```

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
---

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

---

### Notes (feautures to beimplemented)
- Logic for handling bets during the game
- Deploy to Render: create Dockerfile (incl.) and push the image to the container registry or use Docker Compose both services.
