package s5._1.blackjack.S501_Blackjack.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI blackjackOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Blackjack Reactive API")
                        .description("Reactive Blackjack game built with **Spring WebFlux**, **MongoDB** (for games/hands) " +
                                "and **MySQL R2DBC** (for players/ranking).\n\n" +
                                "This API supports game creation, play management (Hit/Stand), and global player ranking.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ResikrysDev <Junior developer/>")
                                .email("resikrysDev@developer.com")
                                .url("https://github.com/Resikrys/Blackjack-Reactive-API")));
    }
}