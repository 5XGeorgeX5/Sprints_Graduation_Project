package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.MedicineOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineOrderRepository extends JpaRepository<MedicineOrder, Long> {
}
