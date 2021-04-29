package com.github.gustavoflor.fronzabarberapi.infrastructure;

import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.infrastructure.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User insert(User user) {
        return userRepository.saveAndFlush(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Page<User> paginate(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
