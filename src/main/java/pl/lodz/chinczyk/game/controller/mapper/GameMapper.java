package pl.lodz.chinczyk.game.controller.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.lodz.chinczyk.game.model.dto.GameDTO;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.pawn.model.dto.PawnDTO;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;
import pl.lodz.chinczyk.player.model.dto.PlayerDTO;
import pl.lodz.chinczyk.player.model.entity.Player;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameMapper {
    @IterableMapping(qualifiedByName = "gameToGameDTO")
    List<GameDTO> mapToDTOList(List<Game> model);

    @Named("gameToGameDTO")
    @Mapping(target = "pawns", ignore = true)
    GameDTO gameToGameDTO(Game game);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "nick")
    PlayerDTO playerToPlayerDTO(Player player);

    GameDTO mapToDTO(Game model);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "color")
    @Mapping(target = "location")
    PawnDTO pawnToPawnDTO(Pawn pawn);

}
