package com.github.gustavoflor.fronzabarberapi.infrastructure.business;

import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.infrastructure.business.provider.MockMailProvider;
import com.github.gustavoflor.fronzabarberapi.infrastructure.business.service.UserService;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserCreateDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserCreateDTOTestHelper;
import com.github.gustavoflor.fronzabarberapi.infrastructure.persistence.UserRepository;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.UserEmailAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, new MockMailProvider());
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

}
