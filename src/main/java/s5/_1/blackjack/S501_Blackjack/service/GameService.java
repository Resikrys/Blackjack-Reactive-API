package s5._1.blackjack.S501_Blackjack.service;

import s5._1.blackjack.S501_Blackjack.dto.CreateGameRequest;
import s5._1.blackjack.S501_Blackjack.dto.GameDTO;
import s5._1.blackjack.S501_Blackjack.dto.PlayRequest;
import s5._1.blackjack.S501_Blackjack.dto.PlayerDTO;
import s5._1.blackjack.S501_Blackjack.exception.NotFoundException;
import s5._1.blackjack.S501_Blackjack.mapper.GameMapper;
import s5._1.blackjack.S501_Blackjack.model.r2dbc.Player;
import s5._1.blackjack.S501_Blackjack.model.mongo.Game;
import s5._1.blackjack.S501_Blackjack.repository.mongo.GameRepository;
import s5._1.blackjack.S501_Blackjack.repository.r2dbc.PlayerRepository;
import s5._1.blackjack.S501_Blackjack.util.BlackjackLogic;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public GameService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public Mono<GameDTO> createGame(CreateGameRequest request) {
        Mono<Player> playerMono = playerRepository.findAll()
                .filter(p -> p.getName().equalsIgnoreCase(request.playerName()))
                .next()
                .switchIfEmpty(playerRepository.save(new Player(null, request.playerName(), 0,0,0)));

        return playerMono.flatMap(player -> {
            Game game = new Game();
            game.setPlayerId(player.getId());
            game.setPlayerName(player.getName());
            game.getPlayerHand().addAll(Arrays.asList(BlackjackLogic.drawCard(), BlackjackLogic.drawCard()));
            game.getDealerHand().addAll(Arrays.asList(BlackjackLogic.drawCard(), BlackjackLogic.drawCard()));
            game.setPlayerScore(BlackjackLogic.computeScore(game.getPlayerHand()));
            game.setDealerScore(BlackjackLogic.computeScore(game.getDealerHand()));
            game.setFinished(false);
            game.setResult("IN_PROGRESS");
            return gameRepository.save(game);
        }).map(GameMapper::toDTO);
    }

    public Mono<GameDTO> getGame(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Game not found: " + id)))
                .map(GameMapper::toDTO);
    }

    public Mono<Void> deleteGame(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Game not found: " + id)))
                .flatMap(g -> gameRepository.deleteById(id));
    }

    public Mono<GameDTO> play(String id, PlayRequest request) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Game not found: " + id)))
                .flatMap(game -> {
                    if (game.isFinished()) {
                        return Mono.just(game);
                    }
                    if ("hit".equalsIgnoreCase(request.action())) {
                        game.getPlayerHand().add(BlackjackLogic.drawCard());
                        game.setPlayerScore(BlackjackLogic.computeScore(game.getPlayerHand()));
                        if (game.getPlayerScore() > 21) {
                            game.setFinished(true);
                            game.setResult("LOSE");
                            return gameRepository.save(game)
                                    .flatMap(g -> updatePlayerStats(g.getPlayerId(), "loss").thenReturn(g));
                        }
                        return gameRepository.save(game);
                    } else if ("stand".equalsIgnoreCase(request.action())) {
                        while (game.getDealerScore() < 17) {
                            game.getDealerHand().add(BlackjackLogic.drawCard());
                            game.setDealerScore(BlackjackLogic.computeScore(game.getDealerHand()));
                        }
                        game.setFinished(true);
                        if (game.getPlayerScore() > 21) game.setResult("LOSE");
                        else if (game.getDealerScore() > 21 || game.getPlayerScore() > game.getDealerScore()) game.setResult("WIN");
                        else if (game.getPlayerScore() == game.getDealerScore()) game.setResult("TIE");
                        else game.setResult("LOSE");

                        return gameRepository.save(game)
                                .flatMap(g -> {
                                    String res = g.getResult();
                                    String stat = switch (res) {
                                        case "WIN" -> "win";
                                        case "TIE" -> "tie";
                                        default -> "loss";
                                    };
                                    return updatePlayerStats(g.getPlayerId(), stat).thenReturn(g);
                                });
                    } else {
                        return Mono.error(new IllegalArgumentException("Unknown action: " + request.action()));
                    }
                }).map(GameMapper::toDTO);
    }

    private Mono<Void> updatePlayerStats(Long playerId, String stat) {
        if (playerId == null) return Mono.empty();
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException("Player not found: " + playerId))))
                .flatMap(p -> {
                    switch (stat) {
                        case "win": p.setWins(p.getWins()+1); break;
                        case "loss": p.setLosses(p.getLosses()+1); break;
                        case "tie": p.setTies(p.getTies()+1); break;
                    }
                    return playerRepository.save(p);
                }).then();
    }

    public Flux<PlayerDTO> ranking() {
        return playerRepository.findAllByOrderByWinsDesc()
                .map(p -> new PlayerDTO(p.getId(), p.getName(), p.getWins(), p.getLosses(), p.getTies()));
    }

    public Mono<PlayerDTO> updatePlayerName(Long playerId, String newName) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new NotFoundException("Player not found: " + playerId)))
                .flatMap(p -> {
                    p.setName(newName);
                    return playerRepository.save(p);
                })
                .map(p -> new PlayerDTO(p.getId(), p.getName(), p.getWins(), p.getLosses(), p.getTies()));
    }
}
