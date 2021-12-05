package pl.lodz.chinczyk.playergame.model;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.chinczyk.game.model.Game;
import pl.lodz.chinczyk.pawn.model.Pawn;
import pl.lodz.chinczyk.player.model.Player;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class PlayerGame {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private UUID id;
    @ManyToOne(fetch = LAZY)
    private Player player;
    @ManyToOne(fetch = LAZY)
    private Game game;
    @OneToMany(mappedBy = "playerGame")
    private Set<Pawn> pawns;
}
