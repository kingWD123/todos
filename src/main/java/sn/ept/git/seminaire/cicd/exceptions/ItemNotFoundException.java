package sn.ept.git.seminaire.cicd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author ISENE
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "Can not find requested resource";
    public static final String TAG_BY_ID = "Can not find tag with id=%s";
    public static final String TODO_BY_ID = "Can not find todo with id=%s";

    public ItemNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ItemNotFoundException(String message) {
        super(message);
    }

    public static String format(String template, String... args) {
        return String.format(template, args);
    }

}
