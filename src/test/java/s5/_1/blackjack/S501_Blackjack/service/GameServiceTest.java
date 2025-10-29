package s5._1.blackjack.S501_Blackjack.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import s5._1.blackjack.S501_Blackjack.dto.CreateGameRequest;
import s5._1.blackjack.S501_Blackjack.dto.PlayRequest;
import s5._1.blackjack.S501_Blackjack.dto.PlayerDTO;
import s5._1.blackjack.S501_Blackjack.exception.NotFoundException;
import s5._1.blackjack.S501_Blackjack.model.r2dbc.Player;
import s5._1.blackjack.S501_Blackjack.model.mongo.Game;
import s5._1.blackjack.S501_Blackjack.repository.mongo.GameRepository;
import s5._1.blackjack.S501_Blackjack.repository.r2dbc.PlayerRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class GameServiceTest {

    private final GameRepository gameRepo = Mockito.mock(GameRepository.class);
    private final PlayerRepository playerRepo = Mockito.mock(PlayerRepository.class);
    private final GameService service = new GameService(gameRepo, playerRepo);

    @Test
    public void createGame_createsNewPlayerAndGame() {
        CreateGameRequest req = new CreateGameRequest("Alice");
        Player savedPlayer = new Player(1L, "Alice", 0, 0, 0);
        Game savedGame = new Game("id1", 1L, "Alice", List.of("A♠", "10♠"), List.of("K♣", "5♦"), 21, 15, false, "IN_PROGRESS", null);

        Mockito.when(playerRepo.findAll()).thenReturn(Flux.empty());
        Mockito.when(playerRepo.save(any(Player.class))).thenReturn(Mono.just(savedPlayer));
        Mockito.when(gameRepo.save(any(Game.class))).thenReturn(Mono.just(savedGame));

        StepVerifier.create(service.createGame(req))
                .expectNextMatches(dto -> dto.playerName().equals("Alice") && dto.result().equals("IN_PROGRESS"))
                .verifyComplete();

        Mockito.verify(playerRepo).save(any(Player.class));
        Mockito.verify(gameRepo).save(any(Game.class));
    }

    @Test
    public void getGame_returnsExistingGame() {
        Game game = new Game("id1", 1L, "Ray", List.of("A♠", "10♠"), List.of("9♣"), 21, 9, true, "WIN", null);
        Mockito.when(gameRepo.findById("id1")).thenReturn(Mono.just(game));

        StepVerifier.create(service.getGame("id1"))
                .expectNextMatches(dto -> dto.id().equals("id1") && dto.result().equals("WIN"))
                .verifyComplete();
    }

    @Test
    public void getGame_notFound_throwsError() {
        Mockito.when(gameRepo.findById("bad-id")).thenReturn(Mono.empty());

        StepVerifier.create(service.getGame("bad-id"))
                .expectErrorMatches(err -> err instanceof NotFoundException)
                .verify();
    }

    @Test
    public void deleteGame_removesExistingGame() {
        Game game = new Game("id1", 1L, "Ray", List.of(), List.of(), 0, 0, false, "IN_PROGRESS", null);
        Mockito.when(gameRepo.findById("id1")).thenReturn(Mono.just(game));
        Mockito.when(gameRepo.deleteById("id1")).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteGame("id1"))
                .verifyComplete();

        Mockito.verify(gameRepo).deleteById("id1");
    }

    @Test
    public void deleteGame_notFound_throwsError() {
        Mockito.when(gameRepo.findById("bad-id")).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteGame("bad-id"))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    public void play_hit_updatesPlayerHand() {
        Game game = new Game(
                "id1", 1L, "Alice",
                new ArrayList<>(List.of("A♠")),
                new ArrayList<>(List.of("10♣")),
                11, 10, false, "IN_PROGRESS", null
        );
        Mockito.when(gameRepo.findById("id1")).thenReturn(Mono.just(game));
        Mockito.when(gameRepo.save(any(Game.class))).thenReturn(Mono.just(game));

        StepVerifier.create(service.play("id1", new PlayRequest("hit", 0)))
                .expectNextMatches(dto -> dto.playerName().equals("Alice"))
                .verifyComplete();

        Mockito.verify(gameRepo).save(any(Game.class));
    }

    @Test
    public void play_stand_finishesGame() {
        Game game = new Game(
                "id2", 1L, "Bob",
                new ArrayList<>(List.of("10♠", "9♣")),
                new ArrayList<>(List.of("8♣", "7♦")),
                19, 15, false, "IN_PROGRESS", null
        );
        Mockito.when(gameRepo.findById("id2")).thenReturn(Mono.just(game));
        Mockito.when(gameRepo.save(any(Game.class))).thenReturn(Mono.just(game));
        Mockito.when(playerRepo.findById(1L)).thenReturn(Mono.just(new Player(1L, "Bob", 0, 0, 0)));
        Mockito.when(playerRepo.save(any(Player.class))).thenReturn(Mono.just(new Player(1L, "Bob", 1, 0, 0)));

        StepVerifier.create(service.play("id2", new PlayRequest("stand", 0)))
                .expectNextMatches(dto -> dto.finished() && dto.playerName().equals("Bob"))
                .verifyComplete();
    }

    @Test
    public void ranking_returnsPlayersOrdered() {
        Player p1 = new Player(1L, "Alice", 5, 1, 0);
        Player p2 = new Player(2L, "Bob", 3, 3, 0);

        Mockito.when(playerRepo.findAllByOrderByWinsDesc()).thenReturn(Flux.just(p1, p2));

        StepVerifier.create(service.ranking())
                .expectNext(new PlayerDTO(1L, "Alice", 5, 1, 0))
                .expectNext(new PlayerDTO(2L, "Bob", 3, 3, 0))
                .verifyComplete();
    }

    @Test
    public void updatePlayerName_changesName() {
        Player existing = new Player(1L, "OldName", 2, 1, 0);
        Player updated = new Player(1L, "NewName", 2, 1, 0);

        Mockito.when(playerRepo.findById(1L)).thenReturn(Mono.just(existing));
        Mockito.when(playerRepo.save(any(Player.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(service.updatePlayerName(1L, "NewName"))
                .expectNextMatches(p -> p.name().equals("NewName"))
                .verifyComplete();
    }

    @Test
    public void updatePlayerName_playerNotFound_throwsError() {
        Mockito.when(playerRepo.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(service.updatePlayerName(99L, "Ghost"))
                .expectError(NotFoundException.class)
                .verify();
    }
}
