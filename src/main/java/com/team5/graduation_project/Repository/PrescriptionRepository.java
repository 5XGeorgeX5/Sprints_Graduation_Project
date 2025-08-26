package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
