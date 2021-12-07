package pl.lodz.chinczyk.player.controller.mapper;

import org.mapstruct.Mapper;
import pl.lodz.chinczyk.generic.mapper.ModelMapper;
import pl.lodz.chinczyk.player.model.dto.PlayerDTO;
import pl.lodz.chinczyk.player.model.entity.Player;

@Mapper(componentModel = "spring")
public interface PlayerMapper extends ModelMapper<Player, PlayerDTO> {
}
