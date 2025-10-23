package s5._1.blackjack.S501_Blackjack.mapper;

import s5._1.blackjack.S501_Blackjack.dto.GameDTO;
import s5._1.blackjack.S501_Blackjack.model.r2dbc.Game;

public class GameMapper {
    public static GameDTO toDTO(Game g) {
        if (g == null) return null;
        return new GameDTO(
                g.getId(),
                g.getPlayerId(),
                g.getPlayerName(),
                g.getPlayerHand(),
                g.getDealerHand(),
                g.getPlayerScore(),
                g.getDealerScore(),
                g.isFinished(),
                g.getResult(),
                g.getCreatedAt()
        );
    }
}
