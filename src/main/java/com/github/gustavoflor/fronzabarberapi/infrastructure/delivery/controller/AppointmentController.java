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
    public ResponseEntity<AppointmentShowDTO> create(@Valid @RequestBody AppointmentCreateDTO appointmentCreateDTO) {
        Appointment appointment = appointmentService.insert(appointmentCreateDTO);
        URI location = URI.create(String.format(ENDPOINT + "/%s", appointment.getId()));
        return ResponseEntity.created(location).body(AppointmentShowDTO.of(appointment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentShowDTO> show(@PathVariable Long id) {
        return appointmentService.findById(id)
                .map(AppointmentShowDTO::of)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping
    public ResponseEntity<Page<AppointmentShowDTO>> index(@Valid Pageable pageable) {
        return ResponseEntity.ok(appointmentService.paginate(pageable.get()).map(AppointmentShowDTO::of));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        appointmentService.cancelById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<Void> accept(@PathVariable Long id, @RequestParam(name = "barberId") Long barberId) {
        appointmentService.acceptById(id, barberId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/refuse")
    public ResponseEntity<Void> refuse(@PathVariable Long id) {
        appointmentService.refuseById(id);
        return ResponseEntity.noContent().build();
    }

}
