package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.controller;

import com.github.gustavoflor.fronzabarberapi.core.Appointment;
import com.github.gustavoflor.fronzabarberapi.core.AppointmentTestHelper;
import com.github.gustavoflor.fronzabarberapi.infrastructure.business.service.AppointmentService;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.ExceptionTranslator;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.AppointmentCreateDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.AppointmentCreateDTOTestHelper;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AppointmentControllerTest {

    private static final String ENDPOINT = AppointmentController.ENDPOINT;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON;

    @Mock
    private AppointmentService appointmentService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AppointmentController(appointmentService))
                .setControllerAdvice(new ExceptionTranslator())
                .build();
    }

    @Test
    void shouldCreate() throws Exception {
        Appointment appointment = AppointmentTestHelper.dummy();
        AppointmentCreateDTO appointmentCreateDTO = AppointmentCreateDTOTestHelper.dummy();
        Mockito.doReturn(appointment).when(appointmentService).insert(Mockito.any());
        doCreateRequest(appointmentCreateDTO)
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, ENDPOINT + "/" + appointment.getId()));
    }

    @Test
    void shouldNotCreateWhenDateIsNull() throws Exception {
        AppointmentCreateDTO appointmentCreateDTO = AppointmentCreateDTOTestHelper.dummy();
        appointmentCreateDTO.setDate(null);
        doCreateRequest(appointmentCreateDTO).andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotCreateWhenDateInPast() throws Exception {
        AppointmentCreateDTO appointmentCreateDTO = AppointmentCreateDTOTestHelper.dummy();
        appointmentCreateDTO.setDate(LocalDateTime.now().minusDays(5L));
        doCreateRequest(appointmentCreateDTO).andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotCreateWhenDateInPresent() throws Exception {
        AppointmentCreateDTO appointmentCreateDTO = AppointmentCreateDTOTestHelper.dummy();
        appointmentCreateDTO.setDate(LocalDateTime.now());
        doCreateRequest(appointmentCreateDTO).andExpect(status().isBadRequest());
    }

    private ResultActions doCreateRequest(AppointmentCreateDTO appointmentCreateDTO) throws Exception {
        return mockMvc.perform(post(ENDPOINT).contentType(CONTENT_TYPE).content(TestUtil.toByte(appointmentCreateDTO)));
    }

    @Test
    void shouldShow() throws Exception {
        Appointment appointment = AppointmentTestHelper.dummy();
        Mockito.doReturn(Optional.of(appointment)).when(appointmentService).findById(appointment.getId());
        doShowRequest(appointment.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(appointment.getStatus().toString())));
    }

    @Test
    void shouldNotShowWhenUserNotFound() throws Exception {
        Long id = 1L;
        Mockito.doReturn(Optional.empty()).when(appointmentService).findById(id);
        doShowRequest(id).andExpect(status().isNoContent());
    }

    private ResultActions doShowRequest(Long id) throws Exception {
        return mockMvc.perform(get(ENDPOINT + "/" + id));
    }

}
