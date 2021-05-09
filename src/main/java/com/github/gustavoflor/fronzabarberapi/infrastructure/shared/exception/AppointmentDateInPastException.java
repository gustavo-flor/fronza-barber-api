package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception;

public class AppointmentDateInPastException extends BusinessException {

    private static final String MESSAGE = "Appointment date in past.";

    public AppointmentDateInPastException() {
        super(MESSAGE);
    }

}
