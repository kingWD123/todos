package sn.ept.git.seminaire.cicd.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sn.ept.git.seminaire.cicd.data.TagDTOTestData;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.mappers.TagMapper;
import sn.ept.git.seminaire.cicd.entities.Tag;
import sn.ept.git.seminaire.cicd.utils.TestUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TagDTOMapperTest {

    TagDTO dto;
    Tag entity;

    private final TagMapper mapper = Mappers.getMapper(TagMapper.class);

    @BeforeEach
    void setUp() {
        dto = TagDTOTestData.defaultDTO();
        entity = mapper.toEntity(dto);
    }

    @Test
    void toEntityShouldReturnCorrectEntity() {
        entity = mapper.toEntity(dto);
        assertThat(dto)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .ignoringFields("todos")
                .isEqualTo(entity);
    }

    @Test
    void toDTOShouldReturnCorrectDTO() {
        entity = mapper.toEntity(dto);
        dto = mapper.toDTO(entity);
        assertThat(dto)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .ignoringFields("todos")
                .isEqualTo(entity);
    }



    @Test
    void toEntitiesShouldReturnCorrectEntities() {
        List<TagDTO> dtoList = List.of(dto);
        List<Tag> entitiesList = mapper.toEntitiesList(dtoList);
        assertThat(entitiesList)
                .hasSameSizeAs(dtoList);
        List<TagDTO> converted = entitiesList.stream().map(TestUtil::toTagDTO).toList();
        assertThat(converted).containsExactlyInAnyOrderElementsOf(dtoList);

    }

    @Test
    void toDTOsShouldReturnCorrectDTOs() {
        List<Tag> entitiesList = List.of(entity);
        List<TagDTO> dtoList = mapper.toDTOlist(entitiesList);
        assertThat(dtoList)
                .hasSameSizeAs(entitiesList);
        List<Tag> converted = dtoList.stream().map(TestUtil::toTagEntity).toList();
        assertThat(converted).containsExactlyInAnyOrderElementsOf(entitiesList);
    }

}