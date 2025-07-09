package sn.ept.git.seminaire.cicd.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import sn.ept.git.seminaire.cicd.exceptions.ForbiddenException;
import sn.ept.git.seminaire.cicd.exceptions.InvalidException;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.models.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import sn.ept.git.seminaire.cicd.utils.Constantes;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ISENE
 */

@Slf4j
@RestControllerAdvice
public class TodoExceptionHandler {

    public final String TEMPLATE = "TodoExceptionHandler : methode : {}, exception :{}, message :{} ";

    @ExceptionHandler(value = {ItemNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorModel> notFound(
            ItemNotFoundException exception, WebRequest request) {
        logError(exception, "notFound");
        return buildResponse(Constantes.TYPE_VALIDATION, HttpStatus.NOT_FOUND.value(), exception, request);
    }


    @ExceptionHandler(value = {ItemExistsException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    protected ResponseEntity<ErrorModel> conflict(
            ItemExistsException exception, WebRequest request) {
        logError(exception, "conflict");
        return buildResponse(Constantes.TYPE_VALIDATION, HttpStatus.CONFLICT.value(), exception, request);
    }

    @ExceptionHandler(value = {InvalidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorModel> badRequest(
            InvalidException exception, WebRequest request) {
        logError(exception, "badRequest");
        return buildResponse(Constantes.TYPE_VALIDATION, HttpStatus.BAD_REQUEST.value(), exception, request);
    }


    @ExceptionHandler(value = {ForbiddenException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    protected ResponseEntity<ErrorModel> permissionDenied(
            ForbiddenException exception, WebRequest request) {
        logError(exception, "permissionDenied");
        return buildResponse(Constantes.TYPE_PERMISSION, HttpStatus.FORBIDDEN.value(), exception, request);
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ErrorModel> internalError(
            Exception exception, WebRequest request) {
        logError(exception, "internalError");
        return buildResponse(Constantes.TYPE_SYSTEM, HttpStatus.INTERNAL_SERVER_ERROR.value(), exception, request);
    }


    @ExceptionHandler(value = {ResponseStatusException.class})
    protected ResponseEntity<ErrorModel> responseStatus(
            ResponseStatusException exception, WebRequest request) {
        logError(exception, "responseStatus");
        return buildResponse(Constantes.TYPE_SYSTEM, exception.getStatusCode().value(), exception, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorModel> methodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request
    ) {
        logError(ex, "methodArgumentNotValidException");
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> "%s: %s".formatted(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        message = StringUtils.defaultIfBlank(
                message,
                ex.getBindingResult().getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining("; "))
        );
        return buildResponse(Constantes.TYPE_VALIDATION, HttpStatus.BAD_REQUEST.value(), message, ex, request);
    }

    private static ResponseEntity<ErrorModel> buildResponse(String type, int status, Exception exception, WebRequest request) {
        return buildResponse(type, status, null, exception, request);
    }

    private static ResponseEntity<ErrorModel> buildResponse(String type, int status, String message, Exception exception, WebRequest request) {
        ErrorModel errorModel = ErrorModel
                .builder()
                .status(status)
                .systemName(Constantes.SYSTEM_NAME)
                .systemId(Constantes.SYSTEM_ID)
                .type(Optional.ofNullable(type).orElse(Constantes.TYPE_SYSTEM))
                .date(LocalDateTime.now(ZoneOffset.UTC))
                .message(Optional.ofNullable(message).orElse(exception.getMessage()))
                .description(request.getDescription(false))
                .zone("UTC")
                .build();
        return new ResponseEntity<>(errorModel, HttpStatus.valueOf(status));
    }

    private void logError(Exception exception, String methodName) {
        log.error(TEMPLATE, methodName, exception.getClass().getName(), exception.getLocalizedMessage());
    }

}