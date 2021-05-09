package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class AppointmentCreateDTOTestHelper {

    public AppointmentCreateDTO dummy() {
        return AppointmentCreateDTO.builder()
                .date(LocalDateTime.now().plusDays(3L))
                .client(PersistentEntityDTO.of(1L))
                .build();
    }

}
