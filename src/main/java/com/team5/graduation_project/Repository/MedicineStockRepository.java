package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.MedicineStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineStockRepository extends JpaRepository<MedicineStock, Long> {
    List<MedicineStock> findByPharmacyId(Long pharmacyId);

    List<MedicineStock> findByMedicineId(Long medicineId);

    @Query("SELECT ms FROM MedicineStock ms WHERE ms.medicine.id = :medicineId AND ms.pharmacy.id = :pharmacyId")
    Optional<MedicineStock> findByMedicineIdAndPharmacyId(@Param("medicineId") Long medicineId, @Param("pharmacyId") Long pharmacyId);

    @Query("SELECT ms FROM MedicineStock ms WHERE ms.quantity > 0")
    List<MedicineStock> findAvailableStocks();

    @Query("SELECT ms FROM MedicineStock ms WHERE ms.medicine.id = :medicineId AND ms.quantity >= :quantity")
    List<MedicineStock> findAvailableStocksByMedicineAndQuantity(@Param("medicineId") Long medicineId, @Param("quantity") Integer quantity);
}