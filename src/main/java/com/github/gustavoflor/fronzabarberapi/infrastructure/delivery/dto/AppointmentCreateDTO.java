package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto;

import com.github.gustavoflor.fronzabarberapi.core.Appointment;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.ModelParser;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentCreateDTO {

    @NotNull
    private LocalDateTime date;

    @NotNull
    private PersistentEntityDTO<Long> client;

    public Appointment transform() {
        return ModelParser.transform(this, Appointment.class);
    }

}
