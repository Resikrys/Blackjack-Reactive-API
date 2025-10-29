package s5._1.blackjack.S501_Blackjack.repository.mongo;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import s5._1.blackjack.S501_Blackjack.model.mongo.Game;

public interface GameRepository extends ReactiveMongoRepository<Game, String> {
    Flux<Game> findByPlayerId(Long playerId);
}
