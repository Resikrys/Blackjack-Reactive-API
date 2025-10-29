package s5._1.blackjack.S501_Blackjack.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import s5._1.blackjack.S501_Blackjack.dto.*;
import s5._1.blackjack.S501_Blackjack.service.GameService;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class GameControllerTest {

    private final GameService service = Mockito.mock(GameService.class);
    private final GameController controller = new GameController(service);

    @Test
    public void createGame_returnsGameDTO() {
        CreateGameRequest request = new CreateGameRequest("Alice");
        GameDTO dto = new GameDTO(
                "id1", 1L, "Alice",
                List.of("A♠", "10♠"),
                List.of("9♣"),
                21, 9, false, "IN_PROGRESS", Instant.now()
        );

        Mockito.when(service.createGame(request)).thenReturn(Mono.just(dto));

        StepVerifier.create(controller.createGame(request))
                .expectNextMatches(g -> g.playerName().equals("Alice") && g.playerHand().size() == 2)
                .verifyComplete();

        Mockito.verify(service).createGame(request);
    }

    @Test
    public void getGame_returnsGameDTO() {
        GameDTO dto = new GameDTO(
                "id1", 1L, "Ray",
                List.of("A♠", "10♠"),
                List.of("9♣"),
                21, 9, true, "WIN", Instant.now()
        );
        Mockito.when(service.getGame("id1")).thenReturn(Mono.just(dto));

        StepVerifier.create(controller.getGame("id1"))
                .expectNextMatches(g -> g.id().equals("id1") && g.result().equals("WIN"))
                .verifyComplete();
    }

    @Test
    public void playMove_returnsUpdatedGame() {
        PlayRequest playRequest = new PlayRequest("hit", 0);
        GameDTO updated = new GameDTO(
                "id1", 1L, "Ray",
                List.of("A♠", "10♠", "Q♣"),
                List.of("9♣"),
                21, 9, true, "WIN", Instant.now()
        );

        Mockito.when(service.play(eq("id1"), eq(playRequest))).thenReturn(Mono.just(updated));

        StepVerifier.create(controller.play("id1", playRequest))
                .expectNextMatches(g -> g.result().equals("WIN") && g.finished())
                .verifyComplete();
    }

    @Test
    public void deleteGame_completesWithoutError() {
        Mockito.when(service.deleteGame("id1")).thenReturn(Mono.empty());

        StepVerifier.create(controller.delete("id1"))
                .verifyComplete();

        Mockito.verify(service).deleteGame("id1");
    }

    @Test
    public void getRanking_returnsFluxOfPlayers() {
        PlayerDTO p1 = new PlayerDTO(1L, "Alice", 5, 2, 1);
        PlayerDTO p2 = new PlayerDTO(2L, "Bob", 3, 3, 0);

        Mockito.when(service.ranking()).thenReturn(Flux.just(p1, p2));

        StepVerifier.create(controller.ranking())
                .expectNext(p1)
                .expectNext(p2)
                .verifyComplete();
    }

    @Test
    public void changePlayerName_returnsUpdatedPlayer() {
        PlayerDTO updated = new PlayerDTO(1L, "Eve", 5, 2, 1);

        Mockito.when(service.updatePlayerName(1L, "Eve")).thenReturn(Mono.just(updated));

        StepVerifier.create(controller.changePlayerName(1L, "Eve"))
                .expectNextMatches(p -> p.name().equals("Eve"))
                .verifyComplete();

        Mockito.verify(service).updatePlayerName(1L, "Eve");
    }
}
