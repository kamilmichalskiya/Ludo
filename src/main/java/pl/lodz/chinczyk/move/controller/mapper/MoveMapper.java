package pl.lodz.chinczyk.move.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lodz.chinczyk.generic.mapper.ModelMapper;
import pl.lodz.chinczyk.move.model.dto.MoveDTO;
import pl.lodz.chinczyk.move.model.entity.Move;

@Mapper(componentModel = "spring")
public interface MoveMapper extends ModelMapper<Move, MoveDTO> {
    @Override
    @Mapping(target = "number", ignore = true)
    Move mapToModel(MoveDTO dto);
}
