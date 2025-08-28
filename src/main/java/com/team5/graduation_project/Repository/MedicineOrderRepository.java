package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.MedicineOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineOrderRepository extends JpaRepository<MedicineOrder, Long> {
    List<MedicineOrder> findByPatientId(Long patientId);
    List<MedicineOrder> findByPharmacyId(Long pharmacyId);
    List<MedicineOrder> findByMedicineId(Long medicineId);
}