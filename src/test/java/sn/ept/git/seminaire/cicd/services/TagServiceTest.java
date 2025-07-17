package sn.ept.git.seminaire.cicd.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import sn.ept.git.seminaire.cicd.data.TagTestData;
import sn.ept.git.seminaire.cicd.entities.Tag;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.mappers.TagMapper;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.repositories.TagRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    TagRepository repository;

    @InjectMocks
    TagService service;

    private static final TagMapper mapper = Mappers.getMapper(TagMapper.class);

    Tag entity;
    TagDTO dto;

    int page = 0;
    int size = 10;

    @BeforeEach
    void setUp() {
        log.info("Before each");
        ReflectionTestUtils.setField(service, "mapper", mapper);
        entity = TagTestData.defaultTag();
        dto = mapper.toDTO(entity);
    }

    @Test
    void save_shouldSucceed() {
        Mockito.when(repository.findByName(dto.getName())).thenReturn(Optional.empty());
        Mockito.when(repository.saveAndFlush(Mockito.any())).thenReturn(entity);
        TagDTO result = service.save(dto);
        assertThat(result).isNotNull().hasFieldOrPropertyWithValue("name", dto.getName());
    }

    @Test
    void save_withExistingName_shouldThrowException() {
        Mockito.when(repository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(ItemExistsException.class, () -> service.save(dto));
    }

    @Test
    void findById_shouldReturnDTO() {
        Mockito.when(repository.findById(dto.getId())).thenReturn(Optional.of(entity));
        TagDTO result = service.findById(dto.getId());
        assertThat(result).isNotNull().hasFieldOrPropertyWithValue("name", dto.getName());
    }

    @Test
    void findById_withInvalidId_shouldThrowException() {
        Mockito.when(repository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> service.findById(UUID.randomUUID().toString()));
    }

    @Test
    void findAll_shouldReturnPage() {
        Pageable pageable = PageRequest.of(page, size);
        Mockito.when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(entity), pageable, 1));
        Page<TagDTO> result = service.findAll(pageable);
        assertThat(result).isNotEmpty().hasSize(1);
    }

    @Test
    void update_shouldSucceed() {
        String updatedName = "updatedTag";
        dto.setName(updatedName);
        Mockito.when(repository.findById(dto.getId())).thenReturn(Optional.of(entity));
        Mockito.when(repository.findByNameWithIdNotEquals(updatedName, dto.getId())).thenReturn(Optional.empty());
        Mockito.when(repository.saveAndFlush(Mockito.any())).thenReturn(entity);

        TagDTO result = service.update(dto.getId(), dto);
        assertThat(result).isNotNull();
    }

    @Test
    void update_withInvalidId_shouldThrowException() {
        Mockito.when(repository.findById(dto.getId())).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> service.update(dto.getId(), dto));
    }

    @Test
    void update_withDuplicateName_shouldThrowException() {
        Mockito.when(repository.findById(dto.getId())).thenReturn(Optional.of(entity));
        Mockito.when(repository.findByNameWithIdNotEquals(dto.getName(), dto.getId())).thenReturn(Optional.of(entity));
        assertThrows(ItemExistsException.class, () -> service.update(dto.getId(), dto));
    }

    @Test
    void delete_shouldSucceed() {
        Mockito.when(repository.findById(dto.getId())).thenReturn(Optional.of(entity));
        Mockito.doNothing().when(repository).deleteById(dto.getId());
        assertDoesNotThrow(() -> service.delete(dto.getId()));
    }

    @Test
    void delete_withInvalidId_shouldThrowException() {
        Mockito.when(repository.findById(dto.getId())).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> service.delete(dto.getId()));
    }

    @Test
    void deleteAll_shouldSucceed() {
        Mockito.doNothing().when(repository).deleteAll();
        assertDoesNotThrow(() -> service.deleteAll());
    }
}
