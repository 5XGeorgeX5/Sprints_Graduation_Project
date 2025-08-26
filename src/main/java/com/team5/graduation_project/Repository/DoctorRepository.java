package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
