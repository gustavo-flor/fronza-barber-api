package com.github.gustavoflor.fronzabarberapi.core;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;

public abstract class AbstractPersistableEntity<I extends Serializable> implements Persistable<I> {

    @Override
    public boolean isNew() {
        return getId() == null;
    }

}
