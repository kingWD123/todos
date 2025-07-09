package sn.ept.git.seminaire.cicd.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sn.ept.git.seminaire.cicd.data.TodoDTOTestData;
import sn.ept.git.seminaire.cicd.entities.Todo;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.mappers.TodoMapper;
import sn.ept.git.seminaire.cicd.utils.TestUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TodoDTOMapperTest {

    TodoDTO dto;
    Todo entity;

    private final TodoMapper mapper=  Mappers.getMapper(TodoMapper.class);

    @BeforeEach
    void setUp() {
        dto = TodoDTOTestData.defaultDTO();
        entity = mapper.toEntity(dto);
    }

    @Test
    void toEntityShouldReturnCorrectEntity() {
        entity = mapper.toEntity(dto);
        assertThat(entity)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("tags")
                .isEqualTo(dto);
    }

    @Test
    void toDTOShouldReturnCorrectDTO() {
        entity = mapper.toEntity(dto);
        dto = mapper.toDTO(entity);
        assertThat(dto)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .ignoringFields("tags")
                .isEqualTo(entity);

    }

    @Test
    void toEntitiesShouldReturnCorrectEntities() {
        List<TodoDTO> dtoList = List.of(dto);
        List<Todo> entitiesList = mapper.toEntitiesList(dtoList);
        assertThat(entitiesList)
                .hasSameSizeAs(dtoList);

        List<TodoDTO> converted = entitiesList.stream().map(TestUtil::toTodoDTO).toList();
        assertThat(converted)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("tags")
                .containsExactlyInAnyOrderElementsOf(dtoList);

    }

    @Test
    void toDTOsShouldReturnCorrectDTOs() {
        List<Todo> entitiesList = List.of(entity);
        List<TodoDTO> dtoList = mapper.toDTOlist(entitiesList);
        assertThat(entitiesList)
                .hasSameSizeAs(dtoList);
        List<Todo> converted = dtoList.stream().map(TestUtil::toTodoEntity).toList();
        assertThat(converted)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("tags")
                .containsExactlyInAnyOrderElementsOf(entitiesList);
    }
}
