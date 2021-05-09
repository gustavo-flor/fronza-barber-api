package com.github.gustavoflor.fronzabarberapi.infrastructure.business;

import com.github.gustavoflor.fronzabarberapi.core.Appointment;
import com.github.gustavoflor.fronzabarberapi.core.AppointmentTestHelper;
import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.core.UserTestHelper;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.AppointmentCreateDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.AppointmentCreateDTOTestHelper;
import com.github.gustavoflor.fronzabarberapi.infrastructure.persistence.AppointmentRepository;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.AppointmentDateInPastException;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.EntityNotFoundException;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.NotAllowedToChangeAppointmentStatusException;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.UserIsNotBarberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserService userService;

    private AppointmentService appointmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        appointmentService = new AppointmentService(appointmentRepository, userService);
    }

    @Test
    void shouldInsert() {
        AppointmentCreateDTO appointmentCreateDTO = AppointmentCreateDTOTestHelper.dummy();
        Mockito.doAnswer(answer -> answer.getArgument(0, Appointment.class)).when(appointmentRepository).saveAndFlush(Mockito.any());
        Appointment appointment = appointmentService.insert(appointmentCreateDTO);
        Assertions.assertEquals(appointmentCreateDTO.getDate(), appointment.getDate());
        Assertions.assertEquals(appointmentCreateDTO.getClient().getId(), appointment.getClient().getId());
        Assertions.assertEquals(Appointment.Status.PENDING, appointment.getStatus());
    }

    @Test
    void shouldNotInsertWhenDateInPast() {
        AppointmentCreateDTO appointmentCreateDTO = AppointmentCreateDTOTestHelper.dummy();
        appointmentCreateDTO.setDate(LocalDateTime.now().minusDays(5L));
        Assertions.assertThrows(AppointmentDateInPastException.class, () -> appointmentService.insert(appointmentCreateDTO));
    }

    @Test
    void shouldNotCancelWhenAppointmentDoNotExists() {
        Long id = 1L;
        Mockito.doReturn(Optional.empty()).when(appointmentRepository).findById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> appointmentService.cancelById(id));
    }

    @Test
    void shouldNotRefuseWhenAppointmentDoNotExists() {
        Long id = 1L;
        Mockito.doReturn(Optional.empty()).when(appointmentRepository).findById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> appointmentService.refuseById(id));
    }

    @Test
    void shouldNotAcceptWhenAppointmentDoNotExists() {
        Long id = 1L;
        Long barberId = 1L;
        Mockito.doReturn(Optional.empty()).when(appointmentRepository).findById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> appointmentService.acceptById(id, barberId));
    }

    @Test
    void shouldNotCancelWhenStatusIsCanceled() {
        shouldNotCancelWhenStatusIsNotCancelable(Appointment.Status.CANCELED);
    }

    @Test
    void shouldNotCancelWhenStatusIsRefused() {
        shouldNotCancelWhenStatusIsNotCancelable(Appointment.Status.REFUSED);
    }

    private void shouldNotCancelWhenStatusIsNotCancelable(Appointment.Status notCancelableStatus) {
        Long id = 1L;
        Appointment appointment = AppointmentTestHelper.dummy();
        appointment.setStatus(notCancelableStatus);
        Mockito.doReturn(Optional.of(appointment)).when(appointmentRepository).findById(id);
        Assertions.assertThrows(NotAllowedToChangeAppointmentStatusException.class, () -> appointmentService.cancelById(id));
    }

    @Test
    void shouldNotAcceptWhenStatusIsAccepted() {
        shouldNotAcceptWhenStatusIsNotAcceptable(Appointment.Status.ACCEPTED);
    }

    @Test
    void shouldNotAcceptWhenStatusIsRefused() {
        shouldNotAcceptWhenStatusIsNotAcceptable(Appointment.Status.REFUSED);
    }

    @Test
    void shouldNotAcceptWhenStatusIsCanceled() {
        shouldNotAcceptWhenStatusIsNotAcceptable(Appointment.Status.CANCELED);
    }

    private void shouldNotAcceptWhenStatusIsNotAcceptable(Appointment.Status notAcceptableStatus) {
        Long id = 1L;
        Long barberId = 1L;
        Appointment appointment = AppointmentTestHelper.dummy();
        appointment.setStatus(notAcceptableStatus);
        Mockito.doReturn(Optional.of(appointment)).when(appointmentRepository).findById(id);
        Assertions.assertThrows(NotAllowedToChangeAppointmentStatusException.class, () -> appointmentService.acceptById(id, barberId));
    }

    @Test
    void shouldNotRefuseWhenStatusIsRefused() {
        shouldNotRefuseWhenStatusIsNotRefusable(Appointment.Status.REFUSED);
    }

    @Test
    void shouldNotRefuseWhenStatusIsCanceled() {
        shouldNotRefuseWhenStatusIsNotRefusable(Appointment.Status.CANCELED);
    }

    private void shouldNotRefuseWhenStatusIsNotRefusable(Appointment.Status notRefusableStatus) {
        Long id = 1L;
        Appointment appointment = AppointmentTestHelper.dummy();
        appointment.setStatus(notRefusableStatus);
        Mockito.doReturn(Optional.of(appointment)).when(appointmentRepository).findById(id);
        Assertions.assertThrows(NotAllowedToChangeAppointmentStatusException.class, () -> appointmentService.refuseById(id));
    }

    @Test
    void shouldNotAcceptWhenBarberIsNotFound() {
        Long id = 1L;
        Long barberId = 2L;
        Mockito.doReturn(Optional.of(AppointmentTestHelper.dummy())).when(appointmentRepository).findById(id);
        Mockito.doReturn(Optional.empty()).when(userService).findById(barberId);
        Assertions.assertThrows(EntityNotFoundException.class, () -> appointmentService.acceptById(id, barberId));
    }

    @Test
    void shouldNotAcceptWhenUserHasNotBarberRole() {
        Long id = 1L;
        User barber = UserTestHelper.dummy();
        Long barberId = barber.getId();
        barber.setRoles(Set.of());
        Mockito.doReturn(Optional.of(AppointmentTestHelper.dummy())).when(appointmentRepository).findById(id);
        Mockito.doReturn(Optional.of(barber)).when(userService).findById(barberId);
        Assertions.assertThrows(UserIsNotBarberException.class, () -> appointmentService.acceptById(id, barberId));
    }

}
