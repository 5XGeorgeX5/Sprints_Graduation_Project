package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query(value = "SELECT d.* FROM doctor d " +
            "WHERE d.appointment_duration > 0 " +
            "AND (ABS(TIMESTAMPDIFF(MINUTE, d.start_shift, d.end_shift)) / d.appointment_duration) > " +
            "(SELECT COUNT(*) FROM appointment a " +
            " WHERE a.doctor_id = d.id AND DATE(a.appointment_time) = :date)",
            nativeQuery = true)
    List<Doctor> findAvailableDoctors(@Param("date") LocalDate date);


}
