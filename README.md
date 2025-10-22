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

## Notes
- Simplified Blackjack logic; pots extend handing (split, double, insurance).
- To deploy to Render: create Dockerfile (incl.) and push the image to the container registry or use Docker Compose both services.
