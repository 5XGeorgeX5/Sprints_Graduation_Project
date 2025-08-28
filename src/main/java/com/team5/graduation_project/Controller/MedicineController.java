package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.MedicineRequestDTO;
import com.team5.graduation_project.DTOs.Response.MedicineResponseDTO;
import com.team5.graduation_project.DTOs.Response.MedicineStockResponseDTO;
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
@RequestMapping("/api/medicine")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    @GetMapping("/available")
    public ResponseEntity<BaseResponse> getAvailableMedicines() {
        List<MedicineStockResponseDTO> medicines = medicineService.getAvailableMedicines();
        return ResponseEntity.ok(new BaseResponse("Available medicines retrieved successfully", medicines));
    }

    @GetMapping("/available/pharmacy/{pharmacyId}")
    public ResponseEntity<BaseResponse> getAvailableMedicinesByPharmacy(@PathVariable Long pharmacyId) {
        List<MedicineStockResponseDTO> medicines = medicineService.getAvailableMedicinesByPharmacy(pharmacyId);
        return ResponseEntity.ok(new BaseResponse("Pharmacy medicines retrieved successfully", medicines));
    }

    @PreAuthorize("hasAuthority('PHARMACY')")
    @PostMapping
    public ResponseEntity<BaseResponse> createMedicine(@Valid @RequestBody MedicineRequestDTO request) {
        MedicineResponseDTO medicine = medicineService.createMedicine(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse("Medicine created successfully", medicine));
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAllMedicines() {
        List<MedicineResponseDTO> medicines = medicineService.getAllMedicines();
        return ResponseEntity.ok(new BaseResponse("Medicines retrieved successfully", medicines));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getMedicineById(@PathVariable Long id) {
        MedicineResponseDTO medicine = medicineService.getMedicineById(id);
        return ResponseEntity.ok(new BaseResponse("Medicine retrieved successfully", medicine));
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse> searchMedicinesByName(@RequestParam String name) {
        List<MedicineResponseDTO> medicines = medicineService.searchMedicinesByName(name);
        return ResponseEntity.ok(new BaseResponse("Search completed successfully", medicines));
    }

    @PreAuthorize("hasAuthority('PHARMACY')")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateMedicine(@PathVariable Long id,
                                                       @Valid @RequestBody MedicineRequestDTO request) {
        MedicineResponseDTO medicine = medicineService.updateMedicine(id, request);
        return ResponseEntity.ok(new BaseResponse("Medicine updated successfully", medicine));
    }

    @PreAuthorize("hasAuthority('PHARMACY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.ok(new BaseResponse("Medicine deleted successfully", null));
    }
}
