package pl.lodz.chinczyk.move.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.chinczyk.pawn.model.Location;
import pl.lodz.chinczyk.pawn.model.dto.PawnDTO;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class MoveDTO {
    private UUID id;
    private int number;
    private int distance;
    private Location oldLocation;
    private Location newLocation;
    private PawnDTO pawn;
}
