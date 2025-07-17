package sn.ept.git.seminaire.cicd.resources;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
import sn.ept.git.seminaire.cicd.data.TagDTOTestData;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.services.TagService;
import sn.ept.git.seminaire.cicd.utils.TestUtil;
import sn.ept.git.seminaire.cicd.utils.UrlMapping;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(TagResource.class)
class TagResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService service;

    private TagDTO dto;

    int page = 0;
    int size = 10;

    @BeforeEach
    void setUp() {
        log.info("Initialisation d’un TagDTO pour chaque test");
        dto = TagDTOTestData.defaultDTO(); // Tag fictif utilisé pour les tests
    }

    @SneakyThrows
    @Test
    void findAll_shouldReturnTags() {
        Mockito.when(service.findAll(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(dto), PageRequest.of(page, size), 1));

        mockMvc.perform(get(UrlMapping.Tag.ALL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(dto.getId()))
                .andExpect(jsonPath("$.content[0].name").value(dto.getName()));
    }

    @SneakyThrows
    @Test
    void findById_shouldReturnTag() {
        Mockito.when(service.findById(Mockito.eq(dto.getId())))
                .thenReturn(dto);

        mockMvc.perform(get(UrlMapping.Tag.FIND_BY_ID, dto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.name").value(dto.getName()));
    }

    @SneakyThrows
    @Test
    void findById_withBadId_shouldReturnNotFound() {
        Mockito.when(service.findById(Mockito.anyString()))
                .thenThrow(new ItemNotFoundException());

        mockMvc.perform(get(UrlMapping.Tag.FIND_BY_ID, UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void create_shouldCreateTag() {
        Mockito.when(service.save(Mockito.any(TagDTO.class)))
                .thenReturn(dto);

        mockMvc.perform(post(UrlMapping.Tag.ADD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.name").value(dto.getName()));
    }

    @SneakyThrows
    @Test
    void update_shouldUpdateTag() {
        Mockito.when(service.update(Mockito.any(), Mockito.any()))
                .thenReturn(dto);

        mockMvc.perform(put(UrlMapping.Tag.UPDATE, dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.name").value(dto.getName()));
    }

    @SneakyThrows
    @Test
    void delete_shouldDeleteTag() {
        Mockito.doNothing().when(service).delete(Mockito.any());

        mockMvc.perform(delete(UrlMapping.Tag.DELETE, dto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void delete_withBadId_shouldReturnNotFound() {
        Mockito.doThrow(new ItemNotFoundException())
                .when(service).delete(Mockito.anyString());

        mockMvc.perform(delete(UrlMapping.Tag.DELETE, UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
