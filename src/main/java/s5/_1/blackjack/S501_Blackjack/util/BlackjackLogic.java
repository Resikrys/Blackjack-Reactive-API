package s5._1.blackjack.S501_Blackjack.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlackjackLogic {
    private static final String[] DECK = {
            "A♠","2♠","3♠","4♠","5♠","6♠","7♠","8♠","9♠","10♠","J♠","Q♠","K♠",
            "A♥","2♥","3♥","4♥","5♥","6♥","7♥","8♥","9♥","10♥","J♥","Q♥","K♥",
            "A♦","2♦","3♦","4♦","5♦","6♦","7♦","8♦","9♦","10♦","J♦","Q♦","K♦",
            "A♣","2♣","3♣","4♣","5♣","6♣","7♣","8♣","9♣","10♣","J♣","Q♣","K♣"
    };
    private static final Random RAND = new Random();

    public static String drawCard() {
        return DECK[RAND.nextInt(DECK.length)];
    }

    public static int computeScore(List<String> hand) {
        int score = 0;
        int aces = 0;
        for (String c : hand) {
            String rank = c.replaceAll("[^A-Za-z0-9]","");
            if (rank.equals("A")) { aces++; score += 11; }
            else if ("KQJ".contains(rank)) score += 10;
            else {
                try { score += Integer.parseInt(rank); } catch (Exception e) {}
            }
        }
        while (score > 21 && aces > 0) { score -= 10; aces--; }
        return score;
    }
}
