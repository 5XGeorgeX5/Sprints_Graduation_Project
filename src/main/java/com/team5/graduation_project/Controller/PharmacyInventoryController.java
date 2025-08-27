package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.MedicineStockRequestDTO;
import com.team5.graduation_project.DTOs.Response.MedicineStockResponseDTO;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.MedicineStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pharmacy/{pharmacyId}/inventory")
@RequiredArgsConstructor
public class PharmacyInventoryController {

    private final MedicineStockService medicineStockService;

    @PostMapping
    public ResponseEntity<BaseResponse> addMedicineToInventory(@PathVariable Long pharmacyId,
                                                               @Valid @RequestBody MedicineStockRequestDTO request) {
        MedicineStockResponseDTO stock = medicineStockService.addMedicineToInventory(pharmacyId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse("Medicine added to inventory successfully", stock));
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getPharmacyInventory(@PathVariable Long pharmacyId) {
        List<MedicineStockResponseDTO> inventory = medicineStockService.getPharmacyInventory(pharmacyId);
        return ResponseEntity.ok(new BaseResponse("Pharmacy inventory retrieved successfully", inventory));
    }

    @PutMapping("/{stockId}")
    public ResponseEntity<BaseResponse> updateMedicineStock(@PathVariable Long pharmacyId,
                                                            @PathVariable Long stockId,
                                                            @Valid @RequestBody MedicineStockRequestDTO request) {
        MedicineStockResponseDTO stock = medicineStockService.updateMedicineStock(stockId, request);
        return ResponseEntity.ok(new BaseResponse("Medicine stock updated successfully", stock));
    }

    @PutMapping("/{stockId}/quantity")
    public ResponseEntity<BaseResponse> updateStockQuantity(@PathVariable Long pharmacyId,
                                                            @PathVariable Long stockId,
                                                            @RequestParam Integer quantity) {
        MedicineStockResponseDTO stock = medicineStockService.updateStockQuantity(stockId, quantity);
        return ResponseEntity.ok(new BaseResponse("Stock quantity updated successfully", stock));
    }

    @PutMapping("/{stockId}/price")
    public ResponseEntity<BaseResponse> updateStockPrice(@PathVariable Long pharmacyId,
                                                         @PathVariable Long stockId,
                                                         @RequestParam BigDecimal price) {
        MedicineStockResponseDTO stock = medicineStockService.updateStockPrice(stockId, price);
        return ResponseEntity.ok(new BaseResponse("Stock price updated successfully", stock));
    }

    @DeleteMapping("/{stockId}")
    public ResponseEntity<BaseResponse> deleteMedicineStock(@PathVariable Long pharmacyId,
                                                            @PathVariable Long stockId) {
        medicineStockService.deleteMedicineStock(stockId);
        return ResponseEntity.ok(new BaseResponse("Medicine removed from inventory successfully", null));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<BaseResponse> getLowStockItems(@PathVariable Long pharmacyId,
                                                         @RequestParam(defaultValue = "10") Integer threshold) {
        List<MedicineStockResponseDTO> lowStock = medicineStockService.getLowStockItems(pharmacyId, threshold);
        return ResponseEntity.ok(new BaseResponse("Low stock items retrieved successfully", lowStock));
    }
}