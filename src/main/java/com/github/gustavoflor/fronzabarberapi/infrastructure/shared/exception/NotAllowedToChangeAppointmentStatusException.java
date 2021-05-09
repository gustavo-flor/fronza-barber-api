package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception;

import com.github.gustavoflor.fronzabarberapi.core.Appointment;

public class NotAllowedToChangeAppointmentStatusException extends BusinessException {

    private static final String MESSAGE = "Not allowed to change appointment status (To: %s, For: %s).";

    public NotAllowedToChangeAppointmentStatusException(Appointment.Status currentStatus, Appointment.Status receivedStatus) {
        super(String.format(MESSAGE, currentStatus.name(), receivedStatus.name()));
    }

}
