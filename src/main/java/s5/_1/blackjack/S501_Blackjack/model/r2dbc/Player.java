package s5._1.blackjack.S501_Blackjack.model.r2dbc;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("player")
public class Player {

    @Id
    private Long id;

    private String name;
    private int wins;
    private int losses;
    private int ties;
}