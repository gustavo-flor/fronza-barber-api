package com.github.gustavoflor.fronzabarberapi.core;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class AppointmentTestHelper {

    public Appointment dummy() {
        return Appointment.builder()
                .id(1L)
                .status(Appointment.Status.PENDING)
                .date(LocalDateTime.now().plusDays(3L))
                .client(UserTestHelper.dummy())
                .build();
    }

}
