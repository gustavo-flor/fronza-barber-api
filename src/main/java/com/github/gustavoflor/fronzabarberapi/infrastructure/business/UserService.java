package com.github.gustavoflor.fronzabarberapi.infrastructure.business;

import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserCreateDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.persistence.UserRepository;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.UserEmailAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User insert(UserCreateDTO userCreateDTO) {
        User user = userCreateDTO.transform();
        if (existsByEmail(user.getEmail())) {
            throw new UserEmailAlreadyExistsException();
        }
        return userRepository.saveAndFlush(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<User> paginate(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
