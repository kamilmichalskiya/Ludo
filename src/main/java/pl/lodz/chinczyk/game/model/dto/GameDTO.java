package pl.lodz.chinczyk.game.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.chinczyk.game.model.GameStatus;
import pl.lodz.chinczyk.pawn.model.dto.PawnDTO;
import pl.lodz.chinczyk.player.model.dto.PlayerDTO;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class GameDTO {
    private UUID id;
    private GameStatus status;
    private Set<PlayerDTO> players = new HashSet<>();
    private Set<PawnDTO> pawns = new HashSet<>();
    private UUID nextPlayerId;
}
