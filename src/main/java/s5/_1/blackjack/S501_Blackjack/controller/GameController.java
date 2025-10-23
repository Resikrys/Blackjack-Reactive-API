package s5._1.blackjack.S501_Blackjack.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import s5._1.blackjack.S501_Blackjack.dto.*;
import s5._1.blackjack.S501_Blackjack.service.GameService;

@RestController
@RequestMapping("/game")
@Tag(name = "Blackjack Game", description = "API for managing Blackjack games and players (WebFlux))")
public class GameController {

    private final GameService service;

    public GameController(GameService service) { this.service = service; }

    @Operation(summary = "Create game", description = "Create a new Blackjack game and the starting player.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Game created successfully.",
                            content = @Content(schema = @Schema(implementation = GameDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request (e.g., empty name).",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
            }
    )
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GameDTO> createGame(
            @RequestBody(description = "Player name.", required = true) CreateGameRequest request) {
        return service.createGame(request);
    }

    @Operation(summary = "Get match details", description = "Gets the current status of a specific game.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Game details.",
                            content = @Content(schema = @Schema(implementation = GameDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Game not found.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
            }
    )
    @GetMapping("/{id}")
    public Mono<GameDTO> getGame(
            @Parameter(description = "Unique item identifier (MongoDB ObjectId).", required = true)
            @PathVariable String id) {
        return service.getGame(id);
    }

    @Operation(summary = "Make move", description = "Make a move (Hit, Stand, Double Down, etc.) in an existing game.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Result of the move and new state of the game.",
                            content = @Content(schema = @Schema(implementation = GameDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Match not found.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Play not allowed in the current state.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
            }
    )
    @PostMapping("/{id}/play")
    public Mono<GameDTO> play(
            @Parameter(description = "Party identifier.", required = true) @PathVariable String id,
            @RequestBody(description = "Type of play (e.g., HIT, STAND) and, optionally, bet.") PlayRequest request) {
        return service.play(id, request);
    }

    @Operation(summary = "Delete game", description = "Delete an existing Blackjack game.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Match successfully deleted."),
                    @ApiResponse(responseCode = "404", description = "Match not found.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
            }
    )
    @DeleteMapping("/{id}/delete")
    public Mono<Void> delete(
            @Parameter(description = "Identifier of the item to be deleted.", required = true)
            @PathVariable String id) {
        return service.deleteGame(id);
    }

    @Operation(summary = "Get player rankings", description = "Gets the list of players sorted by their historical performance (MySQL connection).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of players in ranking order.",
                            content = @Content(schema = @Schema(implementation = PlayerDTO.class)))
            }
    )
    @GetMapping("/ranking")
    public Flux<PlayerDTO> ranking() {
        return service.ranking();
    }

    @Operation(summary = "Change Player Name", description = "Updates the name of an existing player (MySQL connection).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Player information updated.",
                            content = @Content(schema = @Schema(implementation = PlayerDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Player not found.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid new name (e.g., empty).",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
            }
    )
    @PutMapping("/player/{playerId}")
    public Mono<PlayerDTO> changePlayerName(
            @Parameter(description = "Unique player identifier (MySQL ID).", required = true)
            @PathVariable Long playerId,
            @RequestBody(description = "The new player name.", required = true) String newName) {

        return service.updatePlayerName(playerId, newName);
    }
}
