package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception;

public class UserEmailAlreadyExistsException extends BusinessException {

    private static final String MESSAGE = "User email already exists.";

    public UserEmailAlreadyExistsException() {
        super(MESSAGE);
    }

}
