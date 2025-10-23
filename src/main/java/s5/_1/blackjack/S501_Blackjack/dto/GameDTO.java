package s5._1.blackjack.S501_Blackjack.dto;

import java.time.Instant;
import java.util.List;

public record GameDTO(
        String id,
        Long playerId,
        String playerName,
        List<String> playerHand,
        List<String> dealerHand,
        int playerScore,
        int dealerScore,
        boolean finished,
        String result,
        Instant createdAt
) {}
