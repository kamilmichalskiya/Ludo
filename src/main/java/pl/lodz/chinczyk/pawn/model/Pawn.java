package pl.lodz.chinczyk.pawn.model;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.chinczyk.move.model.Move;

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
public class Pawn {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private UUID id;
    @Enumerated(STRING)
    private Color color;
    @Enumerated(STRING)
    private Location location;
    @OneToMany
    private Set<Move> moves;

}
