package pl.lodz.chinczyk.move.controller.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lodz.chinczyk.generic.mapper.ModelMapper;
import pl.lodz.chinczyk.move.model.dto.MoveDTO;
import pl.lodz.chinczyk.move.model.entity.Move;
import pl.lodz.chinczyk.pawn.model.dto.PawnDTO;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;

@Mapper(componentModel = "spring")
public interface MoveMapper extends ModelMapper<Move, MoveDTO> {
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "oldLocation", ignore = true)
    @Mapping(target = "newLocation", ignore = true)
    @Override
    Move mapToModel(MoveDTO dto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    PawnDTO pawnToPawnDTO(Pawn pawn);
}
