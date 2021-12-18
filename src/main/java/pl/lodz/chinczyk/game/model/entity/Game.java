package pl.lodz.chinczyk.game.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import pl.lodz.chinczyk.game.model.GameStatus;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;
import pl.lodz.chinczyk.player.model.entity.Player;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@Entity
public class Game {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    private UUID id;
    @Enumerated(STRING)
    private GameStatus status;
    @ManyToMany
    private Set<Player> players = new HashSet<>();
    @OneToMany(mappedBy = "game")
    private Set<Pawn> pawns = new HashSet<>();
    @Type(type = "uuid-char")
    private UUID nextPlayerId;
}
