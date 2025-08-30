package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.PrescriptionRequestDTO;
import com.team5.graduation_project.DTOs.Response.PrescriptionResponseDTO;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescription")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping("/{doctor_id}")
    @PreAuthorize(("hasAuthority('DOCTOR')"))
    public ResponseEntity<BaseResponse> createPrescription(@PathVariable Long doctor_id, @RequestBody PrescriptionRequestDTO prescriptionRequest) {
        PrescriptionResponseDTO prescription = prescriptionService.createPrescription(doctor_id, prescriptionRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaseResponse("Prescription created successfully", prescription));
    }

    @GetMapping("/{prescriptionId}")
    public ResponseEntity<BaseResponse> getDoctorPrescription(@PathVariable Long prescriptionId) {
        PrescriptionResponseDTO prescription = prescriptionService.getDoctorPresciprion(prescriptionId);
        return ResponseEntity.ok(new BaseResponse("Prescription retrieved successfully", prescription));
    }
}
