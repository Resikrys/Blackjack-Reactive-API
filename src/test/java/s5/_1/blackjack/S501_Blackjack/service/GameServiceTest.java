package s5._1.blackjack.S501_Blackjack.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import s5._1.blackjack.S501_Blackjack.dto.CreateGameRequest;
import s5._1.blackjack.S501_Blackjack.model.mongo.Player;
import s5._1.blackjack.S501_Blackjack.model.r2dbc.Game;
import s5._1.blackjack.S501_Blackjack.repository.mongo.GameRepository;
import s5._1.blackjack.S501_Blackjack.repository.r2dbc.PlayerRepository;

import static org.mockito.ArgumentMatchers.any;

public class GameServiceTest {

    @Test
    public void createGame_createsGameAndPlayerIfNotExists() {
        GameRepository gameRepo = Mockito.mock(GameRepository.class);
        PlayerRepository playerRepo = Mockito.mock(PlayerRepository.class);

        Player savedPlayer = new Player(1L, "Alice",0,0,0);
        Mockito.when(playerRepo.findAll()).thenReturn(reactor.core.publisher.Flux.empty());
        Mockito.when(playerRepo.save(any(Player.class))).thenReturn(Mono.just(savedPlayer));
        Mockito.when(gameRepo.save(any(Game.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        GameService service = new GameService(gameRepo, playerRepo);

        StepVerifier.create(service.createGame(new CreateGameRequest("Alice")))
                .expectNextMatches(dto -> dto.playerName().equals("Alice") && dto.playerHand().size() >= 2)
                .verifyComplete();

        Mockito.verify(playerRepo).save(any(Player.class));
        Mockito.verify(gameRepo).save(any(Game.class));
    }
}