package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
}
