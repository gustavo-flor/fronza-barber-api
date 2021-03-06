package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.controller;

import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.core.UserTestHelper;
import com.github.gustavoflor.fronzabarberapi.infrastructure.business.service.UserService;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.ExceptionTranslator;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserChangePasswordDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserChangePasswordDTOTestHelper;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserCreateDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserCreateDTOTestHelper;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private static final String ENDPOINT = UserController.ENDPOINT;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userService))
                .setControllerAdvice(new ExceptionTranslator())
                .build();
    }

    @Test
    void shouldCreate() throws Exception {
        User user = UserTestHelper.dummy();
        UserCreateDTO userCreateDTO = UserCreateDTOTestHelper.dummy();
        Mockito.doReturn(user).when(userService).insert(Mockito.any());
        doCreateRequest(userCreateDTO)
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, ENDPOINT + "/" + user.getId()));
    }

    @Test
    void shouldNotCreateWhenNameIsNull() throws Exception {
        shouldNotCreateWhenNameIsInvalid(null);
    }

    @Test
    void shouldNotCreateWhenNameIsEmpty() throws Exception {
        shouldNotCreateWhenNameIsInvalid("");
    }

    @Test
    void shouldNotCreateWhenNameIsBlank() throws Exception {
        shouldNotCreateWhenNameIsInvalid(" ");
    }

    private void shouldNotCreateWhenNameIsInvalid(String invalidName) throws Exception {
        UserCreateDTO userCreateDTO = UserCreateDTOTestHelper.dummy();
        userCreateDTO.setName(invalidName);
        doCreateRequest(userCreateDTO).andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotCreateWhenEmailIsNull() throws Exception {
        shouldNotCreateWhenEmailIsInvalid(null);
    }

    @Test
    void shouldNotCreateWhenEmailIsEmpty() throws Exception {
        shouldNotCreateWhenEmailIsInvalid("");
    }

    @Test
    void shouldNotCreateWhenEmailIsBlank() throws Exception {
        shouldNotCreateWhenEmailIsInvalid(" ");
    }

    @Test
    void shouldNotCreateWhenEmailHasNotAtSign() throws Exception {
        shouldNotCreateWhenEmailIsInvalid("mail-without.at.sign");
    }

    private void shouldNotCreateWhenEmailIsInvalid(String invalidEmail) throws Exception {
        UserCreateDTO userCreateDTO = UserCreateDTOTestHelper.dummy();
        userCreateDTO.setEmail(invalidEmail);
        doCreateRequest(userCreateDTO).andExpect(status().isBadRequest());
    }

    private ResultActions doCreateRequest(UserCreateDTO userCreateDTO) throws Exception {
        return mockMvc.perform(post(ENDPOINT).contentType(CONTENT_TYPE).content(TestUtil.toByte(userCreateDTO)));
    }

    @Test
    void shouldShow() throws Exception {
        Long id = 1L;
        User user = UserTestHelper.dummy();
        user.setId(id);
        Mockito.doReturn(Optional.of(user)).when(userService).findById(id);
        doShowRequest(user.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void shouldNotShowWhenUserNotFound() throws Exception {
        Long id = 1L;
        Mockito.doReturn(Optional.empty()).when(userService).findById(id);
        doShowRequest(id).andExpect(status().isNoContent());
    }

    private ResultActions doShowRequest(Long id) throws Exception {
        return mockMvc.perform(get(ENDPOINT + "/" + id));
    }

    @Test
    void shouldNotChangePasswordWhenNewPasswordIsNull() throws Exception {
        shouldNotChangePasswordWhenNewPasswordIsInvalid(null);
    }

    @Test
    void shouldNotChangePasswordWhenNewPasswordIsEmpty() throws Exception {
        shouldNotChangePasswordWhenNewPasswordIsInvalid("");
    }

    @Test
    void shouldNotChangePasswordWhenNewPasswordIsBlank() throws Exception {
        shouldNotChangePasswordWhenNewPasswordIsInvalid(" ");
    }

    private void shouldNotChangePasswordWhenNewPasswordIsInvalid(String invalidNewPassword) throws Exception {
        UserChangePasswordDTO userChangePasswordDTO = UserChangePasswordDTOTestHelper.dummy();
        userChangePasswordDTO.setNewPassword(invalidNewPassword);
        doChangePasswordRequest(userChangePasswordDTO).andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotChangePasswordWhenOldPasswordIsNull() throws Exception {
        shouldNotChangePasswordWhenOldPasswordIsInvalid(null);
    }

    @Test
    void shouldNotChangePasswordWhenOldPasswordIsEmpty() throws Exception {
        shouldNotChangePasswordWhenOldPasswordIsInvalid("");
    }

    @Test
    void shouldNotChangePasswordWhenOldPasswordIsBlank() throws Exception {
        shouldNotChangePasswordWhenOldPasswordIsInvalid(" ");
    }

    private void shouldNotChangePasswordWhenOldPasswordIsInvalid(String invalidOldPassword) throws Exception {
        UserChangePasswordDTO userChangePasswordDTO = UserChangePasswordDTOTestHelper.dummy();
        userChangePasswordDTO.setNewPassword(invalidOldPassword);
        doChangePasswordRequest(userChangePasswordDTO).andExpect(status().isBadRequest());
    }

    private ResultActions doChangePasswordRequest(UserChangePasswordDTO userChangePasswordDTO) throws Exception {
        MockHttpServletRequestBuilder request = patch(ENDPOINT + "/settings/change-password")
                .contentType(CONTENT_TYPE)
                .content(TestUtil.toByte(userChangePasswordDTO));
        return mockMvc.perform(request);
    }

}
