package com.github.gustavoflor.fronzabarberapi.infrastructure.persistence;

import com.github.gustavoflor.fronzabarberapi.core.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
