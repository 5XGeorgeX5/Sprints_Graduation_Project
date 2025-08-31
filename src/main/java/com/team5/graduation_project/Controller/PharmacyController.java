package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.PharmacyResponseDTO;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.PharmacyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {

    private PharmacyService pharmacyService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> getAllRegisteredPharmacies() {
        List<PharmacyResponseDTO> pharmacies = pharmacyService.getAllRegisteredPharmacies();
        return ResponseEntity.ok(new BaseResponse("Registered pharmacies retrieved successfully", pharmacies));
    }

    @PutMapping("/update/{pharmacyId}")
    @PreAuthorize("hasRole('PHARMACY')")
    public ResponseEntity<BaseResponse> updatePharmacy(@PathVariable Long pharmacyId,
                                                       @RequestBody @Valid AccountRegistrationRequestDTO dto) {
        AccountResponseDTO updatedPharmacy = pharmacyService.updatePharmacy(pharmacyId, dto);
        return ResponseEntity.ok(new BaseResponse("Pharmacy updated successfully", updatedPharmacy));
    }

    @DeleteMapping("/delete/{pharmacyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> deletePharmacy(@PathVariable Long pharmacyId) {
        pharmacyService.deletePharmacy(pharmacyId);
        return ResponseEntity.noContent().build();
    }
}
