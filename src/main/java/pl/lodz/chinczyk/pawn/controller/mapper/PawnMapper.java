package pl.lodz.chinczyk.pawn.controller.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lodz.chinczyk.game.model.dto.GameDTO;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.pawn.model.dto.PawnDTO;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;
import pl.lodz.chinczyk.player.model.dto.PlayerDTO;
import pl.lodz.chinczyk.player.model.entity.Player;

@Mapper(componentModel = "spring")
public interface PawnMapper {
    PawnDTO mapToDTO(Pawn pawn);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "nick")
    PlayerDTO playerToPlayerDTO(Player player);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "nextPlayerId")
    GameDTO gameToGameDTO(Game game);
}
