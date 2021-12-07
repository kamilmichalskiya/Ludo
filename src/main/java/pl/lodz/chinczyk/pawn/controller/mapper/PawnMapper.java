package pl.lodz.chinczyk.pawn.controller.mapper;

import org.mapstruct.Mapper;
import pl.lodz.chinczyk.generic.mapper.ModelMapper;
import pl.lodz.chinczyk.pawn.model.dto.PawnDTO;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;

@Mapper(componentModel = "spring")
public interface PawnMapper extends ModelMapper<Pawn, PawnDTO> {
}
