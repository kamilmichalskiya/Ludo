package pl.lodz.chinczyk.pawn.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.pawn.model.Color;
import pl.lodz.chinczyk.pawn.model.Location;
import pl.lodz.chinczyk.player.model.entity.Player;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
public class Pawn {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    private UUID id;
    @Enumerated(STRING)
    private Color color;
    @Enumerated(STRING)
    private Location location;
    @ManyToOne(fetch = LAZY)
    private Game game;
    @ManyToOne(fetch = LAZY)
    private Player player;
}
