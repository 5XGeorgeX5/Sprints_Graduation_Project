package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.Service.Patient.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final IPatientService patientService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> registerPatient(@RequestBody AccountRegistrationRequestDTO dto) {
        return ResponseEntity.ok(patientService.register(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updatePatient(
            @PathVariable Long id,
            @RequestBody AccountRegistrationRequestDTO dto) {
        return ResponseEntity.ok(patientService.updatePatient(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
