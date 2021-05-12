package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.controller;

import com.github.gustavoflor.fronzabarberapi.core.Appointment;
import com.github.gustavoflor.fronzabarberapi.infrastructure.business.AppointmentService;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.Pageable;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.AppointmentCreateDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.AppointmentShowDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(AppointmentController.ENDPOINT)
@AllArgsConstructor
public class AppointmentController {

    public static final String ENDPOINT = "/appointments";

    private final AppointmentService appointmentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Transactional
    public ResponseEntity<AppointmentShowDTO> create(@Valid @RequestBody AppointmentCreateDTO appointmentCreateDTO) {
        Appointment appointment = appointmentService.insert(appointmentCreateDTO);
        URI location = URI.create(ENDPOINT + "/" + appointment.getId());
        return ResponseEntity.created(location).body(AppointmentShowDTO.of(appointment));
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'BARBER')")
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<AppointmentShowDTO> show(@PathVariable Long id) {
        return appointmentService.findById(id)
                .map(AppointmentShowDTO::of)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'BARBER')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Page<AppointmentShowDTO>> index(@Valid Pageable pageable) {
        return ResponseEntity.ok(appointmentService.paginate(pageable.get()).map(AppointmentShowDTO::of));
    }

    @PatchMapping("/{id}/cancel")
    @Transactional
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        appointmentService.cancelById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'BARBER')")
    @PatchMapping("/{id}/accept")
    @Transactional
    public ResponseEntity<Void> accept(@PathVariable Long id, @RequestParam(name = "barberId") Long barberId) {
        appointmentService.acceptById(id, barberId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'BARBER')")
    @PatchMapping("/{id}/refuse")
    @Transactional
    public ResponseEntity<Void> refuse(@PathVariable Long id) {
        appointmentService.refuseById(id);
        return ResponseEntity.noContent().build();
    }

}
