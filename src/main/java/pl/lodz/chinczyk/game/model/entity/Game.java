package pl.lodz.chinczyk.game.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.chinczyk.game.model.GameStatus;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private UUID id;
    @Enumerated(STRING)
    private GameStatus status;
    @OneToMany(mappedBy = "game")
    private Set<Pawn> pawns = new HashSet<>();

    public Game(UUID gameId) {
        id = gameId;
    }
}
