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
import sn.ept.git.seminaire.cicd.data.TodoTestData;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.mappers.TodoMapper;
import sn.ept.git.seminaire.cicd.entities.Todo;
import sn.ept.git.seminaire.cicd.repositories.TodoRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    TodoRepository todoRepository;
    @InjectMocks
    TodoService service;

    private static TodoMapper mapper = Mappers.getMapper(TodoMapper.class);

    Todo todo;
    TodoDTO dto;
    String newTitle = "newTitle";
    String newDescription = "newDescription";
    int page = 0;
    int size = 10;

    @BeforeEach
    void beforeEach() {
        log.info(" before each");
        ReflectionTestUtils.setField(service, "mapper", mapper);
        todo = TodoTestData.defaultTodo();
        dto = mapper.toDTO(todo);
    }

    @Test
    void save_shouldSaveTodo() {
        Mockito.when(todoRepository.saveAndFlush(Mockito.any()))
                .thenReturn(todo);
        dto = service.save(dto);
        assertThat(dto)
                .isNotNull()
                .hasNoNullFieldsOrPropertiesExcept("tags");
    }


    @Test
    void update_shouldSucceed() {
        dto.setTitle(newTitle);
        dto.setDescription(newDescription);
        Mockito.when(todoRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(todo));
        dto = service.update(dto.getId(), dto);
        assertThat(todo)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", newTitle)
                .hasFieldOrPropertyWithValue("description", newDescription);
    }


    @Test
    void update_withBadId_shouldThrowException() {
        Mockito.when(todoRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());
        assertThrows(
                ItemNotFoundException.class,
                () -> service.update(UUID.randomUUID().toString(), dto)
        );
    }


    @Test
    void findAllPageable_shouldReturnResult() {
        Mockito.when(todoRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(
                        new PageImpl<>(
                                List.of(todo),
                                PageRequest.of(page, size),
                                1
                        )
                );
        final Page<TodoDTO> all = service.findAll(PageRequest.of(page, size));
        assertThat(all)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void findById_shouldReturnResult() {
        Mockito.when(todoRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(todo));
        dto = service.findById(todo.getId());
        assertThat(dto)
                .isNotNull()
                .hasNoNullFieldsOrPropertiesExcept("tags");
    }

    @Test
    void findById_withBadId_ShouldReturnNoResult() {
        Mockito.when(todoRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());
        assertThrows(
                ItemNotFoundException.class,
                () -> service.findById(UUID.randomUUID().toString())
        );
    }

    @Test
    void delete_shouldDeleteTodo() {
        Mockito.when(todoRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(todo));
        Mockito.doNothing().when(todoRepository).deleteById(Mockito.anyString());
        assertDoesNotThrow(() -> service.delete(dto.getId()));
    }


    @Test
    void delete_withBadId_ShouldThrowException() {
        Mockito.when(todoRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());
        assertThrows(
                ItemNotFoundException.class,
                () -> service.delete(dto.getId())
        );
    }

    @Test
    void deleteAll_shouldDeleteTodo() {
        Mockito.doNothing().when(todoRepository).deleteAll();
        assertDoesNotThrow(() -> service.deleteAll());
    }

    @Test
    void complete_shouldCompleteTodo() {
        Mockito.when(todoRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(todo));
        Mockito.when(todoRepository.saveAndFlush(Mockito.any()))
                .thenReturn(todo);
        assertDoesNotThrow(() -> service.complete(todo.getId()));
    }

    @Test
    void completeWithBadId_shouldThrowException() {
        Mockito.when(todoRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());
        assertThrows(
                ItemNotFoundException.class,
                () -> service.complete(dto.getId())
        );
    }

}