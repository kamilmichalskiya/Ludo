package pl.lodz.chinczyk.game.controller.mapper;

import org.mapstruct.Mapper;
import pl.lodz.chinczyk.game.model.dto.GameDTO;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.generic.mapper.ModelMapper;

@Mapper(componentModel = "spring")
public interface GameMapper extends ModelMapper<Game, GameDTO> {
}
