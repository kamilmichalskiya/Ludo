package pl.lodz.chinczyk.game.model;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.chinczyk.playergame.model.PlayerGame;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private UUID id;
    @Enumerated(STRING)
    private GameStatus status;
    @OneToMany(mappedBy = "game")
    private Set<PlayerGame> playerGameSet;
}
