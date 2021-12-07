package pl.lodz.chinczyk.pawn.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.chinczyk.game.model.dto.GameDTO;
import pl.lodz.chinczyk.move.model.dto.MoveDTO;
import pl.lodz.chinczyk.pawn.model.Color;
import pl.lodz.chinczyk.pawn.model.Location;
import pl.lodz.chinczyk.player.model.dto.PlayerDTO;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class PawnDTO {
    private UUID id;
    private Color color;
    private Location location;
    private Set<MoveDTO> moves = new HashSet<>();
    private GameDTO game;
    private PlayerDTO player;
}
