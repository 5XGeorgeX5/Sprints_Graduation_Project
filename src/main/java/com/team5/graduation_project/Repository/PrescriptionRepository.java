package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByPatientId(Long patientId);

    List<Prescription> findByDoctorId(Long doctorId);

    @Query("SELECT p FROM Prescription p WHERE p.patient.id = :patientId AND p.doctor.id = :doctorId")
    List<Prescription> findByPatientIdAndDoctorId(@Param("patientId") Long patientId, @Param("doctorId") Long doctorId);

    List<Prescription> findByMedicineId(Long medicineId);
}