package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.PatientResponseDTO;
import com.team5.graduation_project.DTOs.Response.PrescriptionResponseDTO;
import com.team5.graduation_project.Models.Patient;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.IDoctorService;
import com.team5.graduation_project.Service.Patient.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final IPatientService patientService;
    private final IDoctorService doctorService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> registerPatient(@RequestBody AccountRegistrationRequestDTO dto) {
        return ResponseEntity.ok(patientService.register(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AccountResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<AccountResponseDTO> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == principal.id")
    public ResponseEntity<AccountResponseDTO> updatePatient(
            @PathVariable Long id,
            @RequestBody AccountRegistrationRequestDTO dto) {
        return ResponseEntity.ok(patientService.updatePatient(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<BaseResponse> getDoctorPatients(@PathVariable Long doctorId) {
        List<PatientResponseDTO> patients = doctorService.getDoctorPatients(doctorId);
        return ResponseEntity.ok(new BaseResponse("Doctor patients retrieved successfully", patients));
    }

    @GetMapping("/patients/{patientId}/medical-history")
    public ResponseEntity<BaseResponse> getPatientMedicalHistory(@PathVariable Long patientId) {
        List<PrescriptionResponseDTO> medicalHistory = patientService.getPatientMedicalHistory(patientId);
        return ResponseEntity.ok(new BaseResponse("Patient medical history retrieved successfully", medicalHistory));
    }

    @GetMapping("/patients/{patientId}/consultations")
    public ResponseEntity<BaseResponse> getPatientPreviousConsultations(@PathVariable Long patientId) {
        List<List<String>> consultations = patientService.getPatientPreviousConsultations(patientId);
        return ResponseEntity.ok(new BaseResponse("Patient previous consultations retrieved successfully", consultations));
    }
}
