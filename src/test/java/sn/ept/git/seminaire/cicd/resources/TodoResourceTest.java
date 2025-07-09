package sn.ept.git.seminaire.cicd.resources;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sn.ept.git.seminaire.cicd.data.TodoDTOTestData;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.services.TodoService;
import sn.ept.git.seminaire.cicd.utils.TestUtil;
import sn.ept.git.seminaire.cicd.utils.UrlMapping;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(TodoResource.class)
class TodoResourceTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private TodoService service;

    @Autowired
    private TodoResource todoResource;
    private TodoDTO dto;

    @BeforeAll
    static void beforeAll() {
        log.info(" before all ");
    }

    int page = 0;
    int size = 10;

    @BeforeEach
    void beforeEach() {
        log.info(" before each ");
        dto = TodoDTOTestData.defaultDTO();
    }

    @SneakyThrows
    @Test
    void findAll_shouldReturnTodos() {
        Mockito.when(service.findAll(Mockito.any(Pageable.class)))
                .thenReturn(
                        new PageImpl<>(
                                List.of(dto),
                                PageRequest.of(page, size),
                                1
                        )
                );

        mockMvc.perform(get(UrlMapping.Todo.ALL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content.[0].id").exists())
                .andExpect(jsonPath("$.content.[0].version").exists())
                .andExpect(jsonPath("$.content.[0].title", is(
                        dto.getTitle())))
                .andExpect(jsonPath("$.content.[0].description").value(
                        dto.getDescription()));
    }

    @SneakyThrows
    @Test
    void findById_shouldReturnTodo() {
        Mockito.when(service.findById(Mockito.eq(dto.getId())))
                .thenReturn(dto);
        mockMvc.perform(get(UrlMapping.Todo.FIND_BY_ID,
                        dto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.title", is(dto.getTitle())))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @SneakyThrows
    @Test
    void findById_withBadId_shouldReturnNotFound() {
        Mockito.when(service.findById(Mockito.anyString()))
                .thenThrow(new ItemNotFoundException());
        mockMvc.perform(get(UrlMapping.Todo.FIND_BY_ID, UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void add_shouldCreateTodo() {
        Mockito.when(service.save(Mockito.any(TodoDTO.class)))
                .thenReturn(dto);
        mockMvc.perform(post(UrlMapping.Todo.ADD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.title").value(dto.getTitle()))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @SneakyThrows
    @Test
    void update_shouldUpdateTodo() {
        Mockito.when(service.update(Mockito.any(), Mockito.any()))
                .thenReturn(dto);
        mockMvc.perform(
                        put(UrlMapping.Todo.UPDATE,
                                dto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(dto))
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.title").value(dto.getTitle()))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @SneakyThrows
    @Test
    void delete_shouldDeleteTodo() {
        Mockito.doNothing().when(service).delete(Mockito.any());
        mockMvc.perform(
                delete(UrlMapping.Todo.DELETE,
                        dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void delete_withBadId_shouldReturnNotFound() {
        Mockito.doThrow(
                new ItemNotFoundException(
                        ItemNotFoundException.format(ItemNotFoundException.TODO_BY_ID, dto.getId())
                )
        ).when(service).delete(Mockito.anyString());
        mockMvc.perform(
                delete(UrlMapping.Todo.DELETE, UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void complete_shouldCompleteTodo() {
        dto.setCompleted(true);
        Mockito.when(service.complete(Mockito.anyString()))
                .thenReturn(dto);
        mockMvc.perform(get(UrlMapping.Todo.COMPLETE,
                        dto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.completed").exists())
                .andExpect(jsonPath("$.completed", is(true)));
    }
}