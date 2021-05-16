package com.github.gustavoflor.fronzabarberapi.infrastructure.business.service;

import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.infrastructure.business.provider.MailProvider;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserChangePasswordDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserCreateDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.persistence.UserRepository;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.UserEmailAlreadyExistsException;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.UserHasNotPermissionException;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.PasswordHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Value("${application.mail.sender}")
    private String sender;

    private final UserRepository userRepository;
    private final MailProvider mailProvider;

    public UserService(UserRepository userRepository, MailProvider mailProvider) {
        this.userRepository = userRepository;
        this.mailProvider = mailProvider;
    }

    public User insert(UserCreateDTO userCreateDTO) {
        User user = userCreateDTO.transform();
        if (existsByEmail(user.getEmail())) {
            throw new UserEmailAlreadyExistsException();
        }
        String password = UUID.randomUUID().toString();
        user.setPassword(encodePassword(password));
        userRepository.saveAndFlush(user);
        mailProvider.send(createWelcomeMailMessage(user, password));
        return user;
    }

    private SimpleMailMessage createWelcomeMailMessage(User user, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom(sender);
        message.setSubject(String.format("Bem vindo a aplicação %s!!!", user.getName()));
        message.setSentDate(new Date());
        message.setText(String.format("Sua senha secreta é \"%s\", lembre-se de alterar quando possível.", password));
        return message;
    }

    private String encodePassword(String password) {
        Objects.requireNonNull(password);
        return PasswordHelper.encode(password);
    }

    public Optional<User> findById(Long id) {
        User currentUser = getCurrentUser().orElseThrow(() -> new AccessDeniedException("Acesso negado"));
        if (id.equals(currentUser.getId())) {
            return Optional.of(currentUser);
        }
        if (currentUser.hasRole(User.Role.MANAGER)) {
            return userRepository.findById(id);
        }
        throw new UserHasNotPermissionException();
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
                .map(Object::toString)
                .flatMap(userRepository::findByEmail);
    }

    public void changePassword(UserChangePasswordDTO userChangePasswordDTO) {
        User currentUser = getCurrentUser().orElseThrow(() -> new AccessDeniedException("Acesso negado"));
        boolean match = PasswordHelper.matches(userChangePasswordDTO.getOldPassword(), currentUser.getPassword());
        if (!match) {
            throw new UserHasNotPermissionException();
        }
        currentUser.setPassword(encodePassword(userChangePasswordDTO.getNewPassword()));
        userRepository.saveAndFlush(currentUser);
    }

}
