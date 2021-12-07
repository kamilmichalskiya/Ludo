package pl.lodz.chinczyk.generic.mapper;

import java.util.List;

public interface ModelMapper<T, D> {
    T mapToModel(D dto);

    D mapToDTO(T Model);

    List<D> mapToDTOList(List<T> Model);
}
