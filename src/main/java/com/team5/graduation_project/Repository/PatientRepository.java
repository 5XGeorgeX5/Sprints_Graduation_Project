package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
