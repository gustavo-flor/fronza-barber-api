package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery;

import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.BusinessException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionTranslator {

    public ExceptionDetail getResponse(Exception exception) {
        String exceptionName = exception.getClass().getName();
        String exceptionMessage = exception.getMessage();
        log.error("Exception \"{}\" , with message \"{}\"", exceptionName, exceptionMessage);
        return ExceptionDetail.builder().title(exceptionName).message(exceptionMessage).build();
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetail handleEntityNotFoundException(BusinessException exception) {
        return getResponse(exception);
    }

    @Builder
    private static class ExceptionDetail {
        private final String title;
        private final String message;
    }

}
