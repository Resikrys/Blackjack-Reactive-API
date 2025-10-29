package s5._1.blackjack.S501_Blackjack.repository.r2dbc;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import s5._1.blackjack.S501_Blackjack.model.r2dbc.Player;

public interface PlayerRepository extends ReactiveCrudRepository<Player, Long> {
    Flux<Player> findAllByOrderByWinsDesc();
}
