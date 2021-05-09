package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto;

import com.github.gustavoflor.fronzabarberapi.core.User;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class UserCreateDTOTestHelper {

    public UserCreateDTO dummy() {
        return UserCreateDTO.builder()
                .name("Gustavo Flôr")
                .email("gustavo.flor@mail.co")
                .roles(Set.of(User.Role.MANAGER, User.Role.BARBER))
                .build();
    }

}
