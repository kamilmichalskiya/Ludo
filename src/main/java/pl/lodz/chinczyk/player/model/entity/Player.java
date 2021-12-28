package pl.lodz.chinczyk.player.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Player {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    private UUID id;
    @Column(unique = true, nullable = false)
    private String nick;
    @OneToMany(mappedBy = "player")
    private Set<Pawn> pawns = new HashSet<>();
    @ManyToMany(mappedBy = "players")
    private Set<Game> games = new HashSet<>();

    public Player(String nick) {
        this.nick = nick;
    }
}
