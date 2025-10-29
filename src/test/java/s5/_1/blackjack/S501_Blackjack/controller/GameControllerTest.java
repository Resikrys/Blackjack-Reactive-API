package s5._1.blackjack.S501_Blackjack.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import s5._1.blackjack.S501_Blackjack.dto.GameDTO;
import s5._1.blackjack.S501_Blackjack.service.GameService;

public class GameControllerTest {

    @Test
    public void getGame_returnsGameDTO() {
        GameService service = Mockito.mock(GameService.class);
        GameDTO dto = new GameDTO("id1", 1L, "Ray", java.util.List.of("A♠","10♠"), java.util.List.of("9♣"), 21, 9, true, "WIN", java.time.Instant.now());
        Mockito.when(service.getGame("id1")).thenReturn(Mono.just(dto));

        GameController controller = new GameController(service);

        StepVerifier.create(controller.getGame("id1"))
                .expectNextMatches(g -> g.id().equals("id1") && g.playerName().equals("Ray"))
                .verifyComplete();
    }
}