package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception;

public class UserHasNotPermissionException extends BusinessException {

    private static final String MESSAGE = "User has not permission.";

    public UserHasNotPermissionException() {
        super(MESSAGE);
    }

}
