package sn.ept.git.seminaire.cicd.utils;

import org.junit.jupiter.api.Test;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionUtilsTest {

    Optional<String> present = Optional.ofNullable("empty");
    Optional<String> absent = Optional.empty();
    String template ="%s";
    String param =" msg";


    @Test
    void presentOrThrowShouldThrow() {
        assertThrows(
                ItemNotFoundException.class,
                () -> ExceptionUtils.presentOrThrow(absent,template,param)
        );
    }

    @Test
    void presentOrThrowShouldNotThrow() {
        assertDoesNotThrow(
                () -> ExceptionUtils.presentOrThrow(present,template,param)
        );
    }

    @Test
    void absentOrThrowShouldThrow() {
        assertThrows(
                ItemNotFoundException.class,
                () -> ExceptionUtils.presentOrThrow(absent,template,param)
        );
    }

    @Test
    void absentOrThrowShouldNotThrow() {
        assertDoesNotThrow(
                () -> ExceptionUtils.presentOrThrow(present,template,param)
        );
    }

    @Test
    void throwNotFound() {
        assertThrows(
                ItemNotFoundException.class,
                () -> ExceptionUtils.throwNotFound(template,param)
        );
    }
}