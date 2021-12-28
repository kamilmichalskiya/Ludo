package pl.lodz.chinczyk.player.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.chinczyk.game.model.dto.GameDTO;
import pl.lodz.chinczyk.pawn.model.dto.PawnDTO;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class PlayerDTO {
    private UUID id;
    private String nick;
    private Set<PawnDTO> pawns = new HashSet<>();
    private Set<GameDTO> games = new HashSet<>();
}
