package com.github.gustavoflor.fronzabarberapi.infrastructure.business;

import com.github.gustavoflor.fronzabarberapi.core.Appointment;
import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.AppointmentCreateDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.persistence.AppointmentRepository;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final UserService userService;

    public Appointment insert(AppointmentCreateDTO appointmentCreateDTO) {
        Appointment appointment = appointmentCreateDTO.transform();
        if (appointment.getDate().isBefore(LocalDateTime.now())) {
            throw new AppointmentDateInPastException();
        }
        appointment.setPending();
        return appointmentRepository.saveAndFlush(appointment);
    }

    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Page<Appointment> paginate(PageRequest pageRequest) {
        return appointmentRepository.findAll(pageRequest);
    }

    public void cancelById(Long id) {
        Appointment appointment = findById(id).orElseThrow(() -> new EntityNotFoundException(Appointment.class));
        if (!appointment.canCancel()) {
            throw new NotAllowedToChangeAppointmentStatusException(appointment.getStatus(), Appointment.Status.CANCELED);
        }
        appointment.setCanceled();
        appointmentRepository.saveAndFlush(appointment);
    }

    public void acceptById(Long id, Long barberId) {
        Appointment appointment = findById(id).orElseThrow(() -> new EntityNotFoundException(Appointment.class));
        if (!appointment.canAccept()) {
            throw new NotAllowedToChangeAppointmentStatusException(appointment.getStatus(), Appointment.Status.ACCEPTED);
        }
        User user = userService.findById(barberId).orElseThrow(() -> new EntityNotFoundException(User.class));
        if (!user.isBarber()) {
            throw new UserIsNotBarberException();
        }
        appointment.setAccepted();
        appointment.setBarber(user);
        appointmentRepository.saveAndFlush(appointment);
    }

    public void refuseById(Long id) {
        Appointment appointment = findById(id).orElseThrow(() -> new EntityNotFoundException(Appointment.class));
        if (!appointment.canRefuse()) {
            throw new NotAllowedToChangeAppointmentStatusException(appointment.getStatus(), Appointment.Status.REFUSED);
        }
        appointment.setRefused();
        appointmentRepository.saveAndFlush(appointment);
    }

}
