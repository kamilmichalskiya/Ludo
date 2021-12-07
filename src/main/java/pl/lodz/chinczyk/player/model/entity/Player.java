package pl.lodz.chinczyk.player.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String nick;
    @OneToMany(mappedBy = "player")
    private Set<Pawn> pawns = new HashSet<>();

    public Player(String nick) {
        this.nick = nick;
    }
}
