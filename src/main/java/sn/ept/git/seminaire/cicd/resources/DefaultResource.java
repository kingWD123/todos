package sn.ept.git.seminaire.cicd.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ept.git.seminaire.cicd.utils.UrlMapping;

@RestController
public class DefaultResource {
    public static final String  WELCOME_MSG="Welcome to my Todo app";
    public static final String  HEALTH_MSG="UP";


    @GetMapping(UrlMapping.Default.INDEX)
    public ResponseEntity<String> index() {
        return ResponseEntity
                .ok()
                .body(WELCOME_MSG);
    }

    @GetMapping(UrlMapping.Default.HEALTH)
    public ResponseEntity<String> health() {
        return ResponseEntity
                .ok()
                .body(HEALTH_MSG);
    }
}
