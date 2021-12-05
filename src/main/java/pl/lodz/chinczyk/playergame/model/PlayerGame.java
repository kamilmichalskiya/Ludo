package pl.lodz.chinczyk.playergame.model;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.chinczyk.game.model.Game;
import pl.lodz.chinczyk.player.model.Player;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class PlayerGame {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private UUID id;
    @ManyToOne
    private Player player;
    @ManyToOne
    private Game game;
}
