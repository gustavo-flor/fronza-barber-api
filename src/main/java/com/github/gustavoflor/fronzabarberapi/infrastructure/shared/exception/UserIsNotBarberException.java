package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception;

public class UserIsNotBarberException extends BusinessException {

    private static final String MESSAGE = "User is not barber";

    public UserIsNotBarberException() {
        super(MESSAGE);
    }

}
