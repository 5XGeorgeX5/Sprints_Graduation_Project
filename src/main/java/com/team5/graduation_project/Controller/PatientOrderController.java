package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.MedicineOrderRequestDTO;
import com.team5.graduation_project.DTOs.Response.MedicineOrderResponseDTO;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('PATIENT')")
@RequestMapping("/api/patient/{patientId}/orders")
@RequiredArgsConstructor
public class PatientOrderController {

    private final MedicineService medicineService;

    @PostMapping
    public ResponseEntity<BaseResponse> createMedicineOrder(@PathVariable Long patientId,
                                                            @Valid @RequestBody MedicineOrderRequestDTO request) {
        MedicineOrderResponseDTO order = medicineService.createMedicineOrder(patientId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse("Medicine order created successfully", order));
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getPatientOrders(@PathVariable Long patientId) {
        List<MedicineOrderResponseDTO> orders = medicineService.getPatientOrders(patientId);
        return ResponseEntity.ok(new BaseResponse("Patient orders retrieved successfully", orders));
    }
}
