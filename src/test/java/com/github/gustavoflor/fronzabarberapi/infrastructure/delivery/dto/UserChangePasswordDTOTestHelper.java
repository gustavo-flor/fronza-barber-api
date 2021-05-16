package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserChangePasswordDTOTestHelper {

    public UserChangePasswordDTO dummy() {
        return UserChangePasswordDTO.builder()
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();
    }

}
