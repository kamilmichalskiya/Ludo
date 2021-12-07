package pl.lodz.chinczyk.pawn.model.entity;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.move.model.entity.Move;
import pl.lodz.chinczyk.pawn.model.Color;
import pl.lodz.chinczyk.pawn.model.Location;
import pl.lodz.chinczyk.player.model.entity.Player;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class Pawn {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private UUID id;
    @Enumerated(STRING)
    private Color color;
    @Enumerated(STRING)
    private Location location;
    @OneToMany(mappedBy = "pawn")
    private Set<Move> moves = new HashSet<>();
    @ManyToOne(fetch = LAZY)
    private Game game;
    @ManyToOne(fetch = LAZY)
    private Player player;
}
