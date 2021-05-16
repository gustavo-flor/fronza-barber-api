package com.github.gustavoflor.fronzabarberapi.core;

import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.PasswordHelper;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class UserTestHelper {

    public User dummy() {
        return User.builder()
                .id(1L)
                .name("name")
                .email("email")
                .password(PasswordHelper.encode("password"))
                .roles(Set.of(User.Role.MANAGER, User.Role.BARBER))
                .build();
    }

}
