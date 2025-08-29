package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.PrescriptionRequestDTO;
import com.team5.graduation_project.DTOs.Response.PrescriptionResponseDTO;
import com.team5.graduation_project.Service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<PrescriptionResponseDTO> createPrescription(
            @PathVariable Long doctorId,
            @Valid @RequestBody PrescriptionRequestDTO request) {
        PrescriptionResponseDTO response = prescriptionService.createPrescription(doctorId, request);
        return ResponseEntity.status(201).body(response);
    }
}
