package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.MedicineOrderRequestDTO;
import com.team5.graduation_project.DTOs.Response.MedicineOrderResponseDTO;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.MedicineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PatientOrderControllerTest {

    @InjectMocks
    private PatientOrderController controller;

    @Mock
    private MedicineService medicineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMedicineOrder_ShouldReturnCreatedResponse() {
        Long patientId = 1L;
        MedicineOrderRequestDTO request = new MedicineOrderRequestDTO();
        MedicineOrderResponseDTO responseDto = new MedicineOrderResponseDTO();

        when(medicineService.createMedicineOrder(patientId, request)).thenReturn(responseDto);

        ResponseEntity<BaseResponse> response = controller.createMedicineOrder(patientId, request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Medicine order created successfully", response.getBody().getMessage());
        assertEquals(responseDto, response.getBody().getData());

        verify(medicineService).createMedicineOrder(patientId, request);
    }

    @Test
    void getPatientOrders_ShouldReturnOrders() {
        Long patientId = 2L;
        List<MedicineOrderResponseDTO> orders = List.of(new MedicineOrderResponseDTO());

        when(medicineService.getPatientOrders(patientId)).thenReturn(orders);

        ResponseEntity<BaseResponse> response = controller.getPatientOrders(patientId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Patient orders retrieved successfully", response.getBody().getMessage());
        assertEquals(orders, response.getBody().getData());

        verify(medicineService).getPatientOrders(patientId);
    }
}
