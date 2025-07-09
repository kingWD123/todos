package sn.ept.git.seminaire.cicd.resources;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.services.TagService;
import sn.ept.git.seminaire.cicd.utils.LogUtils;
import sn.ept.git.seminaire.cicd.utils.ResponseUtil;
import sn.ept.git.seminaire.cicd.utils.UrlMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
public class TagResource {

    public static final String CLASS_NAME="TagResource";
    private final TagService service;

    @GetMapping(UrlMapping.Tag.ALL)
    public ResponseEntity<Page<TagDTO>> findAll(
            @PageableDefault Pageable page
    ) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "findAll");
        Page<TagDTO> result = service.findAll(page);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(UrlMapping.Tag.FIND_BY_ID)
    public ResponseEntity<TagDTO> findById(@PathVariable ("id") String id) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "findById");
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping(UrlMapping.Tag.ADD)
    public ResponseEntity<TagDTO> create(@RequestBody @Valid TagDTO dto) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "create");
        TagDTO created = service.save(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping(UrlMapping.Tag.UPDATE)
    public ResponseEntity<TagDTO> update(
            @PathVariable("id") String id,
            @RequestBody @Valid TagDTO dto) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "update");
        final TagDTO updatedDTO = service.update(id, dto);
        return ResponseEntity.accepted().body(updatedDTO);
    }

    @DeleteMapping(UrlMapping.Tag.DELETE)
    public ResponseEntity<TagDTO> delete(@PathVariable("id") String id) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "delete");
        service.delete(id);
        return ResponseEntity.noContent().build();
    }



}
