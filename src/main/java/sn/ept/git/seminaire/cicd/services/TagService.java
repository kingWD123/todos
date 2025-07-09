package sn.ept.git.seminaire.cicd.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.mappers.TagMapper;
import sn.ept.git.seminaire.cicd.entities.Tag;
import sn.ept.git.seminaire.cicd.repositories.TagRepository;
import sn.ept.git.seminaire.cicd.utils.ExceptionUtils;
import sn.ept.git.seminaire.cicd.utils.LogUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository repository;
    private final TagMapper mapper;
    public static final String CLASS_NAME = "TagServiceImpl";


    @Transactional
    public TagDTO save(TagDTO dto) {
        log.info(LogUtils.LOG_START, CLASS_NAME, "save");
        final Optional<Tag> optional = repository.findByName(dto.getName());
        ExceptionUtils.absentOrThrow(optional, ItemExistsException.NAME_EXISTS, dto.getName());
        dto.setId(UUID.randomUUID().toString());
        Tag entity = mapper.toEntity(dto);
        entity = repository.saveAndFlush(entity);
        return mapper.toDTO(entity);
    }

    @Transactional
    public void delete(String uuid) {
        log.info(LogUtils.LOG_START, CLASS_NAME, "delete");
        final Optional<Tag> optional = repository.findById(uuid);
        ExceptionUtils.presentOrThrow(optional, ItemNotFoundException.TAG_BY_ID, uuid);
        repository.deleteById(uuid);
    }

    public TagDTO findById(String uuid) {
        log.info(LogUtils.LOG_START, CLASS_NAME, "findById");
        final Optional<Tag> optional = repository.findById(uuid);
        ExceptionUtils.presentOrThrow(optional, ItemNotFoundException.TAG_BY_ID, uuid);
        return mapper.toDTO(optional.get());
    }


    public Page<TagDTO> findAll(Pageable pageable) {
        log.info(LogUtils.LOG_START, CLASS_NAME, "findAll[Pageable]");
        return repository
                .findAll(pageable)
                .map(mapper::toDTO);
    }

    @Transactional
    public TagDTO update(String uuid, TagDTO dto) {
        log.info(LogUtils.LOG_START, CLASS_NAME, "update");
        Optional<Tag> optional = repository.findById(uuid);
        ExceptionUtils.presentOrThrow(optional, ItemNotFoundException.TODO_BY_ID, dto.getId());
        Optional<Tag> optionalTitle = repository.findByNameWithIdNotEquals(dto.getName(), uuid);
        ExceptionUtils.absentOrThrow(optionalTitle, ItemExistsException.TITLE_EXISTS, dto.getName());
        Tag tag = optional.get();
        tag.setName(dto.getName());
        tag = repository.saveAndFlush(tag);
        return mapper.toDTO(tag);
    }

    @Transactional
    public void deleteAll() {
        log.info(LogUtils.LOG_START, CLASS_NAME, "deleteAll");
        repository.deleteAll();
    }

}
