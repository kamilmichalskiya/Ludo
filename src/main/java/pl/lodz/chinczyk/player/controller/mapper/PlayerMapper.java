package pl.lodz.chinczyk.player.controller.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lodz.chinczyk.generic.mapper.ModelMapper;
import pl.lodz.chinczyk.pawn.model.dto.PawnDTO;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;
import pl.lodz.chinczyk.player.model.dto.PlayerDTO;
import pl.lodz.chinczyk.player.model.entity.Player;

@Mapper(componentModel = "spring")
public interface PlayerMapper extends ModelMapper<Player, PlayerDTO> {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "color")
    @Mapping(target = "location")
    PawnDTO pawnToPawnDTO(Pawn pawn);
}
