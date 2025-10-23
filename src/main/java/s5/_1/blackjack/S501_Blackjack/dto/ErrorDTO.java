package s5._1.blackjack.S501_Blackjack.dto;

import java.time.Instant;

public record ErrorDTO(int status, String error, String message, Instant timestamp) {}
