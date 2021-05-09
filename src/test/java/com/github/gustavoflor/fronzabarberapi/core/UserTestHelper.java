package com.github.gustavoflor.fronzabarberapi.core;

import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class UserTestHelper {

    public User dummy() {
        return User.builder()
                .id(1L)
                .name("Gustavo Flôr")
                .email("gustavo.flor@mail.co")
                .roles(Set.of(User.Role.MANAGER, User.Role.BARBER))
                .build();
    }

}
