package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class MedicineOrderRepositoryTest {

    @Autowired
    private MedicineOrderRepository orderRepo;

    @Autowired
    private PharmacyRepository pharmacyRepo;

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Test
    void testFindByPatientId() {
        // Create patient (with account if required by mapping)
        Account acc = new Account();
        acc.setUsername("patient1");
        acc.setPassword("123");
        acc.setEmail("p1@test.com");
        acc.setName("Patient One");
        acc.setRole(Role.PATIENT);
        Patient patient = new Patient();
        patient.setAccount(acc);
        patient = patientRepo.save(patient);

        // Create pharmacy (with account if required by mapping)
        Account accPh = new Account();
        accPh.setUsername("pharma1");
        accPh.setPassword("123");
        accPh.setEmail("ph@test.com");
        accPh.setName("Pharma One");
        accPh.setRole(Role.PHARMACY);
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setAccount(accPh);
        pharmacy.setAddress("Street 1");
        pharmacy = pharmacyRepo.save(pharmacy);

        // Create medicine
        Medicine medicine = new Medicine();
        medicine.setName("TestMed");
        medicine = medicineRepo.save(medicine);

        // Create order
        MedicineOrder order = new MedicineOrder();
        order.setPatient(patient);
        order.setPharmacy(pharmacy);
        order.setMedicine(medicine);
        order.setQuantity(2);
        orderRepo.save(order);

        // Query
        List<MedicineOrder> results = orderRepo.findByPatientId(patient.getId());
        assertEquals(1, results.size());
        assertEquals(2, results.get(0).getQuantity());
    }

    @Test
    void testFindByPharmacyIdAndMedicineId() {
        // create dummy patient
        Account accPat = new Account();
        accPat.setUsername("pat2");
        accPat.setPassword("123");
        accPat.setEmail("p2@test.com");
        accPat.setName("Patient Two");
        accPat.setRole(Role.PATIENT);
        Patient patient = new Patient();
        patient.setAccount(accPat);
        patient = patientRepo.save(patient);

        // pharmacy
        Account accPh = new Account();
        accPh.setUsername("ph2");
        accPh.setPassword("123");
        accPh.setEmail("ph2@test.com");
        accPh.setName("Pharma Two");
        accPh.setRole(Role.PHARMACY);
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setAccount(accPh);
        pharmacy.setAddress("Street 2");
        pharmacy = pharmacyRepo.save(pharmacy);

        // medicine
        Medicine med = new Medicine();
        med.setName("MedX");
        med = medicineRepo.save(med);

        // order
        MedicineOrder order = new MedicineOrder();
        order.setPatient(patient);
        order.setPharmacy(pharmacy);
        order.setMedicine(med);
        order.setQuantity(5);
        orderRepo.save(order);

        // queries
        assertFalse(orderRepo.findByPharmacyId(pharmacy.getId()).isEmpty());
        assertFalse(orderRepo.findByMedicineId(med.getId()).isEmpty());
    }
}
