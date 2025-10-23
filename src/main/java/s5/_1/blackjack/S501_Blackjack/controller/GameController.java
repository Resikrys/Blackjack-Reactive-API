package s5._1.blackjack.S501_Blackjack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import s5._1.blackjack.S501_Blackjack.dto.CreateGameRequest;
import s5._1.blackjack.S501_Blackjack.dto.GameDTO;
import s5._1.blackjack.S501_Blackjack.dto.PlayRequest;
import s5._1.blackjack.S501_Blackjack.dto.PlayerDTO;
import s5._1.blackjack.S501_Blackjack.service.GameService;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService service;

    public GameController(GameService service) { this.service = service; }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GameDTO> createGame(@RequestBody CreateGameRequest request) {
        return service.createGame(request);
    }

    @GetMapping("/{id}")
    public Mono<GameDTO> getGame(@PathVariable String id) {
        return service.getGame(id);
    }

    @PostMapping("/{id}/play")
    public Mono<GameDTO> play(@PathVariable String id, @RequestBody PlayRequest request) {
        return service.play(id, request);
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return service.deleteGame(id);
    }

    @GetMapping("/ranking")
    public Flux<PlayerDTO> ranking() {
        return service.ranking();
    }

    @PutMapping("/player/{playerId}")
    public Mono<PlayerDTO> changePlayerName(@PathVariable Long playerId, @RequestBody String newName) {
        return service.updatePlayerName(playerId, newName);
    }
}
