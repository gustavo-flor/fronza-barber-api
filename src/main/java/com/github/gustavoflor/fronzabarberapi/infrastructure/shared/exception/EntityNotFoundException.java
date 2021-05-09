package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception;

import com.github.gustavoflor.fronzabarberapi.core.AbstractPersistableEntity;

public class EntityNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Entity not found, type: %s.";

    public EntityNotFoundException(Class<? extends AbstractPersistableEntity<?>> entityType) {
        super(String.format(MESSAGE, entityType.getName()));
    }

}
