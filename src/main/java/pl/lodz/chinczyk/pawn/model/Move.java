package pl.lodz.chinczyk.pawn.model;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;

@Getter
@Setter
public class Move {
    private int distance;
    private Location oldLocation;
    private Location newLocation;
    private Pawn pawn;
}
