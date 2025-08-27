package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
