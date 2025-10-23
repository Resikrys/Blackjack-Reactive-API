package s5._1.blackjack.S501_Blackjack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;
import s5._1.blackjack.S501_Blackjack.dto.ErrorDTO;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<ErrorDTO>> handleNotFound(NotFoundException ex) {
        ErrorDTO dto = new ErrorDTO(404, "Not Found", ex.getMessage(), Instant.now());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorDTO>> handleGeneric(Exception ex) {
        ErrorDTO dto = new ErrorDTO(500, "Internal Server Error", ex.getMessage(), Instant.now());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto));
    }
}
