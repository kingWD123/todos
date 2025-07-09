package sn.ept.git.seminaire.cicd.mappers;

import sn.ept.git.seminaire.cicd.entities.Todo;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    Todo toEntity(TodoDTO dto);

    TodoDTO toDTO(Todo entity);

    default List<Todo> toEntitiesList(List<TodoDTO> dtoList){
        return Optional.ofNullable(dtoList)
                .orElse(List.of())
                .stream()
                .map(this::toEntity)
                .toList();
    }

    default List<TodoDTO> toDTOlist(List<Todo> entitiesList){
        return Optional.ofNullable(entitiesList)
                .orElse(List.of())
                .stream().map(this::toDTO).toList();
    }

}