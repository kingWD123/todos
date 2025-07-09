package sn.ept.git.seminaire.cicd.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.mappers.TodoMapper;
import sn.ept.git.seminaire.cicd.entities.Todo;
import sn.ept.git.seminaire.cicd.repositories.TodoRepository;
import sn.ept.git.seminaire.cicd.utils.ExceptionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sn.ept.git.seminaire.cicd.utils.LogUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository repository;
    private final TodoMapper mapper;
    public static final String CLASS_NAME = "TodoServiceImpl";

    @Transactional
    public TodoDTO save(TodoDTO dto) {
        log.info(LogUtils.LOG_START, CLASS_NAME, "save");
        Todo todo = mapper.toEntity(dto);
        todo.setId(UUID.randomUUID().toString());
        todo.setCompleted(false);
        todo = repository.saveAndFlush(todo);
        return mapper.toDTO(todo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String uuid) {
        log.info(LogUtils.LOG_START, CLASS_NAME, "delete");
        final Optional<Todo> optional = repository.findById(uuid);
        ExceptionUtils.presentOrThrow(optional, ItemNotFoundException.TODO_BY_ID, uuid);
        repository.deleteById(optional.get().getId());
    }

    public TodoDTO findById(String uuid) {
        log.info(LogUtils.LOG_START, CLASS_NAME, "findById");
        final Optional<Todo> optional = repository.findById(uuid);
        ExceptionUtils.presentOrThrow(optional, ItemNotFoundException.TODO_BY_ID, uuid);
        return mapper.toDTO(optional.get());
    }


    public Page<TodoDTO> findAll(Pageable pageable) {
        log.info(LogUtils.LOG_START, CLASS_NAME, "findAll[Pageable]");
        return repository
                .findAll(pageable)
                .map(mapper::toDTO);
    }

    @Transactional
    public TodoDTO update(String uuid, TodoDTO dto) {
        log.info(LogUtils.LOG_START, CLASS_NAME, "update");
        Optional<Todo> optional = repository.findById(uuid);
        ExceptionUtils.presentOrThrow(optional, ItemNotFoundException.TODO_BY_ID, uuid);
        Todo todo = optional.get();
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());
        todo.setDateDebut(dto.getDateDebut());
        todo.setDateFin(dto.getDateFin());
        todo = repository.saveAndFlush(todo);
        return mapper.toDTO(todo);
    }

    @Transactional
    public void deleteAll() {
        log.info(LogUtils.LOG_START, CLASS_NAME, "deleteAll");
        repository.deleteAll();
    }

    public TodoDTO complete(String uuid) {
        log.info(LogUtils.LOG_START, CLASS_NAME, "complete");
        final Optional<Todo> optional = repository.findById(uuid);
        ExceptionUtils.presentOrThrow(optional, ItemNotFoundException.TODO_BY_ID, uuid);
        //todo dates check
        final Todo todo = optional.get();
        todo.setCompleted(true);
        return mapper.toDTO(repository.saveAndFlush(todo));
    }
}
