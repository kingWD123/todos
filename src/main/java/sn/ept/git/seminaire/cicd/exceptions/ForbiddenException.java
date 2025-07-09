package sn.ept.git.seminaire.cicd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author ISENE
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = ForbiddenException.DEFAULT_MSG)
public class ForbiddenException extends RuntimeException {
    public static final String DEFAULT_MSG ="Vous n'êtes pas authorisés à effectuer cette action";

    public ForbiddenException() {
        super(DEFAULT_MSG);
    }

  public ForbiddenException(String message) {
      super(message);
  }
}