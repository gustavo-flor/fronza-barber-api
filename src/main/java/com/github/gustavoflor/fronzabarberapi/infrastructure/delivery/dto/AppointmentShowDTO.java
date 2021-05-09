package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto;

import com.github.gustavoflor.fronzabarberapi.core.Appointment;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.ModelParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentShowDTO {

    @Positive
    private Long id;

    @NotNull
    private Appointment.Status status;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private UserShowDTO client;

    private UserShowDTO barber;

    public static AppointmentShowDTO of(Appointment appointment) {
        return ModelParser.transform(appointment, AppointmentShowDTO.class);
    }

}
