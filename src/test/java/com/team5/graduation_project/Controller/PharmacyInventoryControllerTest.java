package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.MedicineStockRequestDTO;
import com.team5.graduation_project.DTOs.Response.MedicineStockResponseDTO;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.MedicineStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PharmacyInventoryControllerTest {

    @InjectMocks
    private PharmacyInventoryController controller;

    @Mock
    private MedicineStockService medicineStockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addMedicineToInventory_ShouldReturnCreatedResponse() {
        MedicineStockRequestDTO request = new MedicineStockRequestDTO();
        MedicineStockResponseDTO responseDto = new MedicineStockResponseDTO();
        when(medicineStockService.addMedicineToInventory(1L, request)).thenReturn(responseDto);

        ResponseEntity<BaseResponse> response = controller.addMedicineToInventory(1L, request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Medicine added to inventory successfully", response.getBody().getMessage());
        verify(medicineStockService).addMedicineToInventory(1L, request);
    }

    @Test
    void getPharmacyInventory_ShouldReturnList() {
        List<MedicineStockResponseDTO> inventory = List.of(new MedicineStockResponseDTO());
        when(medicineStockService.getPharmacyInventory(1L)).thenReturn(inventory);

        ResponseEntity<BaseResponse> response = controller.getPharmacyInventory(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Pharmacy inventory retrieved successfully", response.getBody().getMessage());
        verify(medicineStockService).getPharmacyInventory(1L);
    }

    @Test
    void updateStockQuantity_ShouldUpdate() {
        MedicineStockResponseDTO dto = new MedicineStockResponseDTO();
        when(medicineStockService.updateStockQuantity(5L, 50)).thenReturn(dto);

        ResponseEntity<BaseResponse> response = controller.updateStockQuantity(1L, 5L, 50);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Stock quantity updated successfully", response.getBody().getMessage());
        verify(medicineStockService).updateStockQuantity(5L, 50);
    }

    @Test
    void updateStockPrice_ShouldUpdate() {
        MedicineStockResponseDTO dto = new MedicineStockResponseDTO();
        when(medicineStockService.updateStockPrice(5L, BigDecimal.TEN)).thenReturn(dto);

        ResponseEntity<BaseResponse> response = controller.updateStockPrice(1L, 5L, BigDecimal.TEN);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Stock price updated successfully", response.getBody().getMessage());
        verify(medicineStockService).updateStockPrice(5L, BigDecimal.TEN);
    }
}
