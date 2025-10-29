package s5._1.blackjack.S501_Blackjack.model.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "games")
public class Game {

    @Id
    private String id;

    private Long playerId;
    private String playerName;

    @Builder.Default
    private List<String> playerHand = new ArrayList<>();

    @Builder.Default
    private List<String> dealerHand = new ArrayList<>();

    private int playerScore;
    private int dealerScore;
    private boolean finished;
    private String result;
//    private Instant createdAt;
//
//    @Builder.Default
//    private Instant createdAtDefault = Instant.now();

    @Builder.Default
    private Instant createdAt = Instant.now();
}
