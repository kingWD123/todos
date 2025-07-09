package sn.ept.git.seminaire.cicd.handler;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import sn.ept.git.seminaire.cicd.exceptions.ForbiddenException;
import sn.ept.git.seminaire.cicd.exceptions.InvalidException;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.models.ErrorModel;
import sn.ept.git.seminaire.cicd.utils.Constantes;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TodoExceptionHandlerTest {

    @Mock
    private WebRequest request;
    private static final String desc = "request description";
    private static final String msg = "error message";
    private ResponseEntity<ErrorModel> response;
    private final TodoExceptionHandler handler = new TodoExceptionHandler();

    @BeforeEach
    void setUp() {
        response = null;
        Mockito.when(request.getDescription(Mockito.anyBoolean()))
                .thenReturn(desc);
    }

    @Test
    void testHandleNotFound() {
        ItemNotFoundException exception = new ItemNotFoundException(msg);
        response = handler.notFound(exception, request);
        verifyFormattedError(Constantes.TYPE_VALIDATION,response, HttpStatus.NOT_FOUND, msg, desc);
    }

    @Test
    void testHandleConflict() {
        ItemExistsException exception = new ItemExistsException(msg);
        response = handler.conflict(exception, request);
        verifyFormattedError(Constantes.TYPE_VALIDATION,response, HttpStatus.CONFLICT, msg, desc);
    }

    @Test
    void testHandleBadRequest() {
        InvalidException exception = new InvalidException(msg);
        response = handler.badRequest(exception, request);
        verifyFormattedError(Constantes.TYPE_VALIDATION,response, HttpStatus.BAD_REQUEST, msg, desc);
    }

    @Test
    void testHandlePermissionDenied() {
        ForbiddenException exception = new ForbiddenException(msg);
        response = handler.permissionDenied(exception, request);
        verifyFormattedError(Constantes.TYPE_PERMISSION,response, HttpStatus.FORBIDDEN, msg, desc);
    }

    @ParameterizedTest
    @MethodSource("othersExceptionsTestData")
    void testHandleInternalError(Exception exception) {
        response = handler.internalError(exception, request);
        verifyFormattedError(Constantes.TYPE_SYSTEM,response, HttpStatus.INTERNAL_SERVER_ERROR, msg, desc);
    }

    @ParameterizedTest
    @MethodSource("responseStatusErrorTestData")
    void testHandleResponseStatus(HttpStatusCode statusCode) {
        ResponseStatusException exception = new ResponseStatusException(statusCode, msg);
        response = handler.responseStatus(exception, request);
        verifyFormattedError(Constantes.TYPE_SYSTEM,response, HttpStatus.valueOf(statusCode.value()), msg, desc);
    }

    private void verifyFormattedError(String type, final ResponseEntity<ErrorModel> response, final HttpStatus httpStatus, final String message, final String description) {
        Predicate<? super Object> messagePredicate = msg -> msg.toString().contains(message);
        Predicate<? super Object> datePredicate = date -> {
            LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
            LocalDateTime errorDate = (LocalDateTime) date;
            return Math.abs(now.toEpochSecond(ZoneOffset.UTC) - errorDate.toEpochSecond(ZoneOffset.UTC)) <= 10;
        };

   assertThat(response)
           .isNotNull()
           .extracting("status")
           .isExactlyInstanceOf(HttpStatus.class)
           .isEqualTo(httpStatus);

        ErrorModel body=response.getBody();

        assertThat(body)
                .isNotNull();

        assertThat(body)
                .extracting("status")
                .isExactlyInstanceOf(Integer.class)
                .isEqualTo(httpStatus.value());

        assertThat(body)
                .extracting("description")
                .isExactlyInstanceOf(String.class)
                .isEqualTo(description);

        assertThat(body)
                .extracting("message")
                .isExactlyInstanceOf(String.class)
                .matches(messagePredicate);

        assertThat(body)
                .extracting("date")
                .isExactlyInstanceOf(LocalDateTime.class)
                .matches(datePredicate);

        assertThat(body)
                .extracting("systemId")
                .isExactlyInstanceOf(String.class)
                .isEqualTo(Constantes.SYSTEM_ID);

        assertThat(body)
                .extracting("systemName")
                .isExactlyInstanceOf(String.class)
                .isEqualTo(Constantes.SYSTEM_NAME);

        assertThat(body)
                .extracting("type")
                .isExactlyInstanceOf(String.class)
                .isEqualTo(type);

    }

    public static Stream<Arguments> responseStatusErrorTestData() {
        return Stream.of(
                of(HttpStatusCode.valueOf(403)),
                of(HttpStatusCode.valueOf(501)),
                of(HttpStatusCode.valueOf(503)),
                of(HttpStatusCode.valueOf(303)),
                of(HttpStatusCode.valueOf(302))
        );
    }

    public static Stream<Arguments> othersExceptionsTestData() {
        return Stream.of(
                of(new Exception(msg)),
                of(new RuntimeException(msg))
        );
    }
}