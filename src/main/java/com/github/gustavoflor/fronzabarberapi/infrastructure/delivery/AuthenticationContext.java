package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery;

import com.github.gustavoflor.fronzabarberapi.core.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthenticationContext {

    private final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public User currentUser() {
        return currentUser.get();
    }

    public void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public void clear() {
        currentUser.remove();
    }

}
