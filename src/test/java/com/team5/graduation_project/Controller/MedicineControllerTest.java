package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.MedicineRequestDTO;
import com.team5.graduation_project.DTOs.Response.MedicineResponseDTO;
import com.team5.graduation_project.DTOs.Response.MedicineStockResponseDTO;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.MedicineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicineControllerTest {

    @Mock
    private MedicineService medicineService;

    @InjectMocks
    private MedicineController medicineController;

    private MedicineResponseDTO medicineResponse;
    private MedicineStockResponseDTO stockResponse;
    private MedicineRequestDTO medicineRequest;

    @BeforeEach
    void setUp() {
        medicineResponse = MedicineResponseDTO.builder()
                .id(1L)
                .name("Paracetamol")
                .description("Pain reliever")
                .build();

        stockResponse = MedicineStockResponseDTO.builder()
                .id(1L)
                .medicineId(1L)
                .pharmacyId(1L)
                .quantity(100)
                .build();

        medicineRequest = MedicineRequestDTO.builder()
                .name("Paracetamol")
                .description("Pain reliever")
                .build();
    }

    @Test
    void getAvailableMedicines_ShouldReturnAvailableMedicines() {
        // Arrange
        List<MedicineStockResponseDTO> medicines = Collections.singletonList(stockResponse);
        when(medicineService.getAvailableMedicines()).thenReturn(medicines);

        // Act
        ResponseEntity<BaseResponse> response = medicineController.getAvailableMedicines();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Available medicines retrieved successfully", response.getBody().getMessage());

        @SuppressWarnings("unchecked")
        List<MedicineStockResponseDTO> actualData = (List<MedicineStockResponseDTO>) response.getBody().getData();
        assertEquals(1, actualData.size());
    }

    @Test
    void getAvailableMedicinesByPharmacy_ShouldReturnPharmacyMedicines() {
        // Arrange
        List<MedicineStockResponseDTO> medicines = Collections.singletonList(stockResponse);
        when(medicineService.getAvailableMedicinesByPharmacy(1L)).thenReturn(medicines);

        // Act
        ResponseEntity<BaseResponse> response = medicineController.getAvailableMedicinesByPharmacy(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pharmacy medicines retrieved successfully", response.getBody().getMessage());

        @SuppressWarnings("unchecked")
        List<MedicineStockResponseDTO> actualData = (List<MedicineStockResponseDTO>) response.getBody().getData();
        assertEquals(1, actualData.size());
    }

    @Test
    void createMedicine_WithValidData_ShouldCreateMedicine() {
        // Arrange
        when(medicineService.createMedicine(any(MedicineRequestDTO.class))).thenReturn(medicineResponse);

        // Act
        ResponseEntity<BaseResponse> response = medicineController.createMedicine(medicineRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Medicine created successfully", response.getBody().getMessage());

        MedicineResponseDTO actualData = (MedicineResponseDTO) response.getBody().getData();
        assertEquals("Paracetamol", actualData.getName());
        verify(medicineService).createMedicine(medicineRequest);
    }

    @Test
    void getAllMedicines_ShouldReturnAllMedicines() {
        // Arrange
        List<MedicineResponseDTO> medicines = Collections.singletonList(medicineResponse);
        when(medicineService.getAllMedicines()).thenReturn(medicines);

        // Act
        ResponseEntity<BaseResponse> response = medicineController.getAllMedicines();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Medicines retrieved successfully", response.getBody().getMessage());

        @SuppressWarnings("unchecked")
        List<MedicineResponseDTO> actualData = (List<MedicineResponseDTO>) response.getBody().getData();
        assertEquals(1, actualData.size());
        assertEquals("Paracetamol", actualData.get(0).getName());
    }

    @Test
    void getMedicineById_ShouldReturnMedicine() {
        // Arrange
        when(medicineService.getMedicineById(1L)).thenReturn(medicineResponse);

        // Act
        ResponseEntity<BaseResponse> response = medicineController.getMedicineById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Medicine retrieved successfully", response.getBody().getMessage());

        MedicineResponseDTO actualData = (MedicineResponseDTO) response.getBody().getData();
        assertEquals("Paracetamol", actualData.getName());
        verify(medicineService).getMedicineById(1L);
    }

    @Test
    void searchMedicinesByName_ShouldReturnMatchingMedicines() {
        // Arrange
        List<MedicineResponseDTO> medicines = Collections.singletonList(medicineResponse);
        when(medicineService.searchMedicinesByName("Para")).thenReturn(medicines);

        // Act
        ResponseEntity<BaseResponse> response = medicineController.searchMedicinesByName("Para");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Search completed successfully", response.getBody().getMessage());

        @SuppressWarnings("unchecked")
        List<MedicineResponseDTO> actualData = (List<MedicineResponseDTO>) response.getBody().getData();
        assertEquals(1, actualData.size());
        assertEquals("Paracetamol", actualData.get(0).getName());
        verify(medicineService).searchMedicinesByName("Para");
    }

    @Test
    void updateMedicine_WithValidData_ShouldUpdateMedicine() {
        // Arrange
        when(medicineService.updateMedicine(eq(1L), any(MedicineRequestDTO.class))).thenReturn(medicineResponse);

        // Act
        ResponseEntity<BaseResponse> response = medicineController.updateMedicine(1L, medicineRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Medicine updated successfully", response.getBody().getMessage());

        MedicineResponseDTO actualData = (MedicineResponseDTO) response.getBody().getData();
        assertEquals("Paracetamol", actualData.getName());
        verify(medicineService).updateMedicine(1L, medicineRequest);
    }

    @Test
    void deleteMedicine_ShouldDeleteMedicine() {
        // Arrange
        doNothing().when(medicineService).deleteMedicine(1L);

        // Act
        ResponseEntity<BaseResponse> response = medicineController.deleteMedicine(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Medicine deleted successfully", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(medicineService).deleteMedicine(1L);
    }
}