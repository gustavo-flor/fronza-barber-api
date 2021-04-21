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

    private final UserRepository repository;

    public User insert(User user) {
        return repository.saveAndFlush(user);
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public Page<User> paginate(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
