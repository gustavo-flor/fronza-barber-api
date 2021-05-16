package com.github.gustavoflor.fronzabarberapi.infrastructure.business;

import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.core.UserTestHelper;
import com.github.gustavoflor.fronzabarberapi.infrastructure.business.provider.MockMailProvider;
import com.github.gustavoflor.fronzabarberapi.infrastructure.business.service.UserService;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserChangePasswordDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserChangePasswordDTOTestHelper;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserCreateDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserCreateDTOTestHelper;
import com.github.gustavoflor.fronzabarberapi.infrastructure.persistence.UserRepository;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.UserEmailAlreadyExistsException;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.UserHasNotPermissionException;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.PasswordHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;
import java.util.UUID;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = Mockito.spy(new UserService(userRepository, new MockMailProvider()));
    }

    @Test
    void shouldInsert() {
        UserCreateDTO userCreateDTO = UserCreateDTOTestHelper.dummy();
        Mockito.doReturn(false).when(userRepository).existsByEmail(userCreateDTO.getEmail());
        Mockito.doAnswer(answer -> answer.getArgument(0, User.class)).when(userRepository).saveAndFlush(Mockito.any());
        User user = userService.insert(userCreateDTO);
        Assertions.assertEquals(userCreateDTO.getName(), user.getName());
        Assertions.assertEquals(userCreateDTO.getEmail(), user.getEmail());
        Assertions.assertEquals(userCreateDTO.getRoles(), user.getRoles());
    }

    @Test
    void shouldNotInsertWhenEmailsAlreadyExists() {
        UserCreateDTO userCreateDTO = UserCreateDTOTestHelper.dummy();
        Mockito.doReturn(true).when(userRepository).existsByEmail(userCreateDTO.getEmail());
        Assertions.assertThrows(UserEmailAlreadyExistsException.class, () -> userService.insert(userCreateDTO));
    }

    @Test
    void shouldNotChangePasswordWhenPasswordDoNotMatch() {
        UserChangePasswordDTO userChangePasswordDTO = UserChangePasswordDTOTestHelper.dummy();
        User user = UserTestHelper.dummy();
        user.setPassword(PasswordHelper.encode(UUID.randomUUID().toString()));
        Mockito.doReturn(Optional.of(user)).when(userService).getCurrentUser();
        Assertions.assertThrows(UserHasNotPermissionException.class, () -> userService.changePassword(userChangePasswordDTO));
    }

    @Test
    void shouldNotChangePasswordWhenHasNotCurrentUser() {
        UserChangePasswordDTO userChangePasswordDTO = UserChangePasswordDTOTestHelper.dummy();
        Mockito.doReturn(Optional.empty()).when(userService).getCurrentUser();
        Assertions.assertThrows(AccessDeniedException.class, () -> userService.changePassword(userChangePasswordDTO));
    }

    @Test
    void shouldChangePassword() {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        UserChangePasswordDTO userChangePasswordDTO = UserChangePasswordDTO.builder()
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .build();
        User user = UserTestHelper.dummy();
        user.setPassword(PasswordHelper.encode(oldPassword));
        Mockito.doReturn(Optional.of(user)).when(userService).getCurrentUser();
        Mockito.doAnswer(answer -> answer.getArgument(0, User.class)).when(userRepository).saveAndFlush(Mockito.any());
        userService.changePassword(userChangePasswordDTO);
        Assertions.assertTrue(PasswordHelper.matches(newPassword, user.getPassword()));
    }

}
