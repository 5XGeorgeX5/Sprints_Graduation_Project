package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByAccountEmail(String email);

    List<Patient> findByAccountNameContainingIgnoreCase(String name);
}
