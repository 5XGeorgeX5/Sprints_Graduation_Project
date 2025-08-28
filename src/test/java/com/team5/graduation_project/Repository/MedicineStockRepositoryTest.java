package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MedicineStockRepositoryTest {

    @Autowired
    private MedicineStockRepository stockRepo;

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private PharmacyRepository pharmacyRepo;

    @Test
    void testFindByMedicineIdAndPharmacyId() {
        // create valid account
        Account account = new Account();
        account.setUsername("pharmacy_user");
        account.setPassword("secret");
        account.setEmail("pharma@test.com");  // required by schema
        account.setName("Test Pharmacy");
        account.setRole(Role.PHARMACY);

        // pharmacy shares PK with account
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setAccount(account);
        pharmacy.setAddress("NOOOOOOOOOOOOOOOO");

        pharmacy = pharmacyRepo.save(pharmacy);

        Medicine medicine = new Medicine();
        medicine.setName("TestMed");
        medicine = medicineRepo.save(medicine);

        MedicineStock stock = new MedicineStock();
        stock.setPharmacy(pharmacy);
        stock.setMedicine(medicine);
        stock.setQuantity(5);
        stockRepo.save(stock);

        Optional<MedicineStock> found =
                stockRepo.findByMedicineIdAndPharmacyId(medicine.getId(), pharmacy.getId());

        assertTrue(found.isPresent());
        assertEquals(5, found.get().getQuantity());
    }

    @Test
    void testFindAvailableStocks() {
        Account account = new Account();
        account.setUsername("user2");
        account.setPassword("pass");
        account.setEmail("pharmacy2@test.com");
        account.setName("Another Pharmacy");
        account.setRole(Role.PHARMACY);

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setAccount(account);
        pharmacy.setAddress("Street 123");
        pharmacy = pharmacyRepo.save(pharmacy);

        Medicine medicine = new Medicine();
        medicine.setName("Med2");
        medicine = medicineRepo.save(medicine);

        MedicineStock stock = new MedicineStock();
        stock.setPharmacy(pharmacy);
        stock.setMedicine(medicine);
        stock.setQuantity(3);
        stockRepo.save(stock);

        List<MedicineStock> available = stockRepo.findAvailableStocks();

        assertFalse(available.isEmpty());
    }
}
