package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
