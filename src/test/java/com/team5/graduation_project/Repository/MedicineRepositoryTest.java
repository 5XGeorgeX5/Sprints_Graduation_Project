package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Medicine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MedicineRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MedicineRepository medicineRepository;

    private Medicine medicine1;
    private Medicine medicine2;

    @BeforeEach
    void setUp() {
        medicine1 = Medicine.builder()
                .name("Paracetamol")
                .description("Pain reliever")
                .build();

        medicine2 = Medicine.builder()
                .name("Ibuprofen")
                .description("Anti-inflammatory")
                .build();

        entityManager.persistAndFlush(medicine1);
        entityManager.persistAndFlush(medicine2);
    }

    @Test
    void findByNameIgnoreCase_WithExistingName_ShouldReturnMedicine() {
        Optional<Medicine> result = medicineRepository.findByNameIgnoreCase("PARACETAMOL");

        assertTrue(result.isPresent());
        assertEquals("Paracetamol", result.get().getName());
    }

    @Test
    void findByNameIgnoreCase_WithNonExistingName_ShouldReturnEmpty() {
        Optional<Medicine> result = medicineRepository.findByNameIgnoreCase("Aspirin");

        assertFalse(result.isPresent());
    }

    @Test
    void findByNameContainingIgnoreCase_WithMatchingPattern_ShouldReturnMatchingMedicines() {
        List<Medicine> result = medicineRepository.findByNameContainingIgnoreCase("para");

        assertEquals(1, result.size());
        assertEquals("Paracetamol", result.get(0).getName());
    }

    @Test
    void findByNameContainingIgnoreCase_WithNonMatchingPattern_ShouldReturnEmptyList() {
        List<Medicine> result = medicineRepository.findByNameContainingIgnoreCase("xyz");

        assertEquals(0, result.size());
    }

    @Test
    void existsByNameIgnoreCase_WithExistingName_ShouldReturnTrue() {
        boolean exists = medicineRepository.existsByNameIgnoreCase("PARACETAMOL");

        assertTrue(exists);
    }

    @Test
    void existsByNameIgnoreCase_WithNonExistingName_ShouldReturnFalse() {
        boolean exists = medicineRepository.existsByNameIgnoreCase("Aspirin");

        assertFalse(exists);
    }

    @Test
    void findByNameContainingIgnoreCase_WithPartialMatch_ShouldReturnAllMatches() {
        Medicine medicine3 = Medicine.builder()
                .name("Paracetamol Extra")
                .description("Extra strength pain reliever")
                .build();
        entityManager.persistAndFlush(medicine3);

        List<Medicine> result = medicineRepository.findByNameContainingIgnoreCase("paracetamol");

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(m -> m.getName().equals("Paracetamol")));
        assertTrue(result.stream().anyMatch(m -> m.getName().equals("Paracetamol Extra")));
    }
}
