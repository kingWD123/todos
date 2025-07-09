package sn.ept.git.seminaire.cicd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author ISENE
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ItemExistsException extends RuntimeException {

    public static final String DEFAUL_MESSAGE="Un des éléments que vous tentez d'jouter existe déjà ";
    public static final String NAME_EXISTS="Le nom  %s  existe déjà ";
    public static final String TITLE_EXISTS="Le titre  %s  existe déjà ";

    public ItemExistsException() {
        super(DEFAUL_MESSAGE);
    }
    public ItemExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    public ItemExistsException(String message) {
        super(message);
    }
    public ItemExistsException(Throwable cause) {
        super(cause);
    }

    public static String format(String template, String ...args) {
        return String.format(template,args);
    }
}
