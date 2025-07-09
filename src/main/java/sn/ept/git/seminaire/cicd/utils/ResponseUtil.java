package sn.ept.git.seminaire.cicd.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

/**
 *
 * @author ISENE
 */
public final class ResponseUtil {


    private ResponseUtil(){
        super();
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(final Optional<X> maybeResponse) {
        return wrapOrNotFound(maybeResponse, null,HttpStatus.OK,"");
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(final Optional<X> maybeResponse,final HttpStatus status) {
        return wrapOrNotFound(maybeResponse, null,status,"");
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(final Optional<X> maybeResponse,  final HttpStatus status, final String reason) {
        return wrapOrNotFound(maybeResponse, null,status,reason);
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(final Optional<X> maybeResponse,  final String reason) {
        return wrapOrNotFound(maybeResponse, null,HttpStatus.OK,reason);
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(final Optional<X> maybeResponse,final HttpHeaders header, final HttpStatus status) {
        return wrapOrNotFound(maybeResponse, header,status,"");
    }


    public static <X> ResponseEntity<X> wrapOrNotFound(final Optional<X> maybeResponse,final HttpHeaders header, final HttpStatus status, final String reason) {
        final HttpStatus finalStatus =  Optional.ofNullable(status).orElse(HttpStatus.OK);
        final String finalMessage =  Optional.ofNullable(reason).orElse("");
        return maybeResponse
                .map(data ->  ResponseEntity.status(finalStatus).headers(header).body(data))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, finalMessage));
    }
}
