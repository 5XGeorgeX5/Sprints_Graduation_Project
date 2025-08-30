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
    @Query("SELECT a.appointmentTime FROM Appointment a " +
            "WHERE a.doctor.id = :doctorId " +
            "AND a.appointmentTime >= :startOfDay " +
            "AND a.appointmentTime < :endOfDay " +
            "ORDER BY a.appointmentTime")
    List<LocalDateTime> getBookedSlots(@Param("doctorId") Long doctorId,
                                       @Param("startOfDay") LocalDateTime startOfDay,
                                       @Param("endOfDay") LocalDateTime endOfDay);


    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);
}
