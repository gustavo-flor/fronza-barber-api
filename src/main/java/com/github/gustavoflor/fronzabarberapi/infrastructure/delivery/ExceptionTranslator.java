package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery;

import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ExceptionTranslator {

    public ExceptionDetail getResponse(Exception exception, WebRequest request, Integer status) {
        return getResponse(exception, request, status, false);
    }

    public ExceptionDetail getResponseAndLogTrace(Exception exception, WebRequest request, Integer status) {
        return getResponse(exception, request, status, true);
    }

    private ExceptionDetail getResponse(Exception exception, WebRequest request, Integer status, boolean logFullyException) {
        ExceptionDetail detail = ExceptionDetail.builder()
                .error(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .status(status)
                .path(getPath(request))
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

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDetail handleException(Exception exception, WebRequest request) {
        return getResponseAndLogTrace(exception, request, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleBusinessException(BusinessException exception, WebRequest request) {
        return getResponse(exception, request, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(AppointmentDateInPastException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleAppointmentDateInPastException(AppointmentDateInPastException exception, WebRequest request) {
        return getResponse(exception, request, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request) {
        return getResponse(exception, request, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(NotAllowedToChangeAppointmentStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleNotAllowedToChangeAppointmentStatus(NotAllowedToChangeAppointmentStatusException exception, WebRequest request) {
        return getResponse(exception, request, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleUserEmailAlreadyExistsException(UserEmailAlreadyExistsException exception, WebRequest request) {
        return getResponse(exception, request, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(UserIsNotBarberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleUserIsNotBarberException(UserIsNotBarberException exception, WebRequest request) {
        return getResponse(exception, request, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request) {
        return getResponse(exception, request, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionDetail handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        return getResponse(exception, request, HttpStatus.UNAUTHORIZED.value());
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ExceptionDetail {
        private LocalDateTime timestamp;
        private Integer status;
        private String error;
        private String message;
        private String path;
    }

}
