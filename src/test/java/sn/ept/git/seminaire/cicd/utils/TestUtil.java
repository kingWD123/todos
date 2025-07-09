package sn.ept.git.seminaire.cicd.utils;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import sn.ept.git.seminaire.cicd.entities.Tag;
import sn.ept.git.seminaire.cicd.entities.Todo;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.models.TodoDTO;

import java.io.IOException;

/**
 * @author ISENE
 */
@UtilityClass
public final class TestUtil {
    private static final ObjectMapper mapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }


    public TagDTO toTagDTO(Tag tag) {
        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .version(tag.getVersion())
                .createdDate(tag.getCreatedDate())
                .lastModifiedDate(tag.getLastModifiedDate())
                .build();
    }

    public Tag toTagEntity(TagDTO dto) {
        return Tag.builder()
                .id(dto.getId())
                .name(dto.getName())
                .version(dto.getVersion())
                .createdDate(dto.getCreatedDate())
                .lastModifiedDate(dto.getLastModifiedDate())
                .build();
    }


    public TodoDTO toTodoDTO(Todo todo) {
        return TodoDTO.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .version(todo.getVersion())
                .createdDate(todo.getCreatedDate())
                .dateDebut(todo.getDateDebut())
                .dateFin(todo.getDateFin())
                .lastModifiedDate(todo.getLastModifiedDate())
                .build();
    }

    public Todo toTodoEntity(TodoDTO dto) {
        return Todo.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .completed(dto.isCompleted())
                .version(dto.getVersion())
                .createdDate(dto.getCreatedDate())
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .lastModifiedDate(dto.getLastModifiedDate())
                .build();
    }
}
