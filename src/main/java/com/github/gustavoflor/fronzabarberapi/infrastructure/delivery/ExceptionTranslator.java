package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery;

import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class ExceptionTranslator {

    public ExceptionDetail getResponse(Exception exception, WebRequest request) {
        return getResponse(exception, request, false);
    }

    public ExceptionDetail getResponseAndLogTrace(Exception exception, WebRequest request) {
        return getResponse(exception, request, true);
    }

    private ExceptionDetail getResponse(Exception exception, WebRequest request, boolean logFullyException) {
        ExceptionDetail detail = ExceptionDetail.builder()
                .message(exception.getMessage())
                .timestamp(System.currentTimeMillis())
                .path(getPath(request))
                .method(getMethod(request))
                .build();
        log.error("{} with detail \"{}\"", exception.getClass().getSimpleName(), detail);
        if (logFullyException) {
            log.error(":: Log Fully Exception ::", exception);
        }
        return detail;
    }

    private String getPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }

    private String getMethod(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getMethod();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDetail handleException(Exception exception, WebRequest request) {
        return getResponseAndLogTrace(exception, request);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleBusinessException(BusinessException exception, WebRequest request) {
        return getResponse(exception, request);
    }

    @ExceptionHandler(AppointmentDateInPastException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleAppointmentDateInPastException(AppointmentDateInPastException exception, WebRequest request) {
        return getResponse(exception, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request) {
        return getResponse(exception, request);
    }

    @ExceptionHandler(NotAllowedToChangeAppointmentStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleNotAllowedToChangeAppointmentStatus(NotAllowedToChangeAppointmentStatusException exception, WebRequest request) {
        return getResponse(exception, request);
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleUserEmailAlreadyExistsException(UserEmailAlreadyExistsException exception, WebRequest request) {
        return getResponse(exception, request);
    }

    @ExceptionHandler(UserIsNotBarberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleUserIsNotBarberException(UserIsNotBarberException exception, WebRequest request) {
        return getResponse(exception, request);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ExceptionDetail {
        private String message;
        private String path;
        private String method;
        private Long timestamp;
    }

}
