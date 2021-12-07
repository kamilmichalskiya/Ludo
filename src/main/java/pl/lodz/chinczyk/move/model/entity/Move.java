package pl.lodz.chinczyk.move.model.entity;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.chinczyk.pawn.model.Location;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class Move {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private UUID id;
    private int number;
    private int distance;
    private Location oldLocation;
    private Location newLocation;
    @ManyToOne(fetch = LAZY)
    private Pawn pawn;

}
