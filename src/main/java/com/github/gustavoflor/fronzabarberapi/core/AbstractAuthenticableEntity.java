package com.github.gustavoflor.fronzabarberapi.core;

import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;

public abstract class AbstractAuthenticableEntity<I extends Serializable> extends AbstractPersistableEntity<I> implements UserDetails {

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
