package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT TIME(a.appointmentTime) FROM Appointment a " +
            "WHERE a.doctor.id = :doctorId AND DATE(a.appointmentTime) = :date " +
            "ORDER BY a.appointmentTime")
    List<LocalTime> getBookedSlots(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);
}
