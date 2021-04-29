package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery;

import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.infrastructure.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    public Optional<User> findBySubject(String subject) {
        return userRepository.findByEmail(subject);
    }

}
