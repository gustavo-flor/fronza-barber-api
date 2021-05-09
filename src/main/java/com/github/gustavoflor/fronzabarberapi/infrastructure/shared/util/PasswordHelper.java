package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@UtilityClass
public class PasswordHelper {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encode(String decodedPassword) {
        return passwordEncoder.encode(decodedPassword);
    }

}
