package com.github.gustavoflor.fronzabarberapi.infrastructure.business;

import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserCreateDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.persistence.UserRepository;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.UserEmailAlreadyExistsException;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.PasswordHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public User insert(UserCreateDTO userCreateDTO) {
        User user = userCreateDTO.transform();
        if (existsByEmail(user.getEmail())) {
            throw new UserEmailAlreadyExistsException();
        }
        encodePassword(user);
        return userRepository.saveAndFlush(user);
    }

    private void encodePassword(User user) {
        Optional.ofNullable(user.getPassword()).ifPresent(password -> user.setPassword(PasswordHelper.encode(password)));
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Page<User> paginate(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    }

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .map(this::toUserDetails)
                .map(UserDetails::getUsername)
                .flatMap(userRepository::findByEmail);
    }

    private UserDetails toUserDetails(Object principal) {
        return (UserDetails) principal;
    }

}
