package pl.lodz.chinczyk.game.controller.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.lodz.chinczyk.game.model.dto.GameDTO;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.generic.mapper.ModelMapper;
import pl.lodz.chinczyk.pawn.model.dto.PawnDTO;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;
import pl.lodz.chinczyk.player.model.dto.PlayerDTO;
import pl.lodz.chinczyk.player.model.entity.Player;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameMapper extends ModelMapper<Game, GameDTO> {
    @Named("gameToGameDTO")
    @Mapping(target = "pawns", ignore = true)
    GameDTO gameToGameDTO(Game game);

    @IterableMapping(qualifiedByName = "gameToGameDTO")
    @Override
    List<GameDTO> mapToDTOList(List<Game> model);

    @Mapping(target = "pawns", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Override
    Game mapToModel(GameDTO dto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "color")
    @Mapping(target = "location")
    @Mapping(target = "player")
    PawnDTO pawnToPawnDTO(Pawn pawn);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "nick")
    PlayerDTO playerToPlayerDTO(Player player);

}
