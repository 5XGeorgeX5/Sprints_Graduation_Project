package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.MedicineStockRequestDTO;
import com.team5.graduation_project.DTOs.Response.MedicineStockResponseDTO;
import com.team5.graduation_project.Exceptions.AlreadyExists;
import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Medicine;
import com.team5.graduation_project.Models.MedicineStock;
import com.team5.graduation_project.Models.Pharmacy;
import com.team5.graduation_project.Repository.MedicineRepository;
import com.team5.graduation_project.Repository.MedicineStockRepository;
import com.team5.graduation_project.Repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicineStockService {

    private final MedicineStockRepository medicineStockRepository;
    private final MedicineRepository medicineRepository;
    private final PharmacyRepository pharmacyRepository;
    private final DtoMapper dtoMapper;

    // Pharmacy inventory management (Story 9)
    public MedicineStockResponseDTO addMedicineToInventory(Long pharmacyId, MedicineStockRequestDTO request) {
        // Validate pharmacy exists
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new ResourceNotFound("Pharmacy not found with id: " + pharmacyId));

        // Validate medicine exists
        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new ResourceNotFound("Medicine not found with id: " + request.getMedicineId()));

        // Check if medicine already exists in this pharmacy's inventory
        if (medicineStockRepository.findByMedicineIdAndPharmacyId(request.getMedicineId(), pharmacyId).isPresent()) {
            throw new AlreadyExists("Medicine already exists in pharmacy inventory. Use update instead.");
        }

        MedicineStock stock = dtoMapper.toMedicineStockEntity(request, medicine, pharmacy);
        MedicineStock savedStock = medicineStockRepository.save(stock);
        return dtoMapper.toMedicineStockResponseDTO(savedStock);
    }

    public List<MedicineStockResponseDTO> getPharmacyInventory(Long pharmacyId) {
        if (!pharmacyRepository.existsById(pharmacyId)) {
            throw new ResourceNotFound("Pharmacy not found with id: " + pharmacyId);
        }

        return medicineStockRepository.findByPharmacyId(pharmacyId).stream()
                .map(dtoMapper::toMedicineStockResponseDTO)
                .collect(Collectors.toList());
    }

    public MedicineStockResponseDTO getMedicineStockById(Long stockId) {
        MedicineStock stock = medicineStockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFound("Medicine stock not found with id: " + stockId));
        return dtoMapper.toMedicineStockResponseDTO(stock);
    }

    public MedicineStockResponseDTO updateMedicineStock(Long stockId, MedicineStockRequestDTO request) {
        // I am assuming we won't change the medicine reference.
        MedicineStock stock = medicineStockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFound("Medicine stock not found with id: " + stockId));

        if (request.getPrice() != null) {
            stock.setPrice(request.getPrice());
        }

        if (request.getQuantity() != null) {
            stock.setQuantity(request.getQuantity());
        }

        MedicineStock updatedStock = medicineStockRepository.save(stock);
        return dtoMapper.toMedicineStockResponseDTO(updatedStock);
    }

    public void deleteMedicineStock(Long stockId) {
        if (!medicineStockRepository.existsById(stockId)) {
            throw new ResourceNotFound("Medicine stock not found with id: " + stockId);
        }

        medicineStockRepository.deleteById(stockId);
    }

    // Update only quantity (useful for restocking)
    public MedicineStockResponseDTO updateStockQuantity(Long stockId, Integer newQuantity) {
        MedicineStock stock = medicineStockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFound("Medicine stock not found with id: " + stockId));

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        stock.setQuantity(newQuantity);
        MedicineStock updatedStock = medicineStockRepository.save(stock);
        return dtoMapper.toMedicineStockResponseDTO(updatedStock);
    }

    // Update only price
    public MedicineStockResponseDTO updateStockPrice(Long stockId, BigDecimal newPrice) {
        MedicineStock stock = medicineStockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFound("Medicine stock not found with id: " + stockId));

        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        stock.setPrice(newPrice);
        MedicineStock updatedStock = medicineStockRepository.save(stock);
        return dtoMapper.toMedicineStockResponseDTO(updatedStock);
    }

    // Check availability for specific medicine and quantity
    public List<MedicineStockResponseDTO> checkMedicineAvailability(Long medicineId, Integer requiredQuantity) {
        if (!medicineRepository.existsById(medicineId)) {
            throw new ResourceNotFound("Medicine not found with id: " + medicineId);
        }

        return medicineStockRepository.findAvailableStocksByMedicineAndQuantity(medicineId, requiredQuantity)
                .stream()
                .map(dtoMapper::toMedicineStockResponseDTO)
                .collect(Collectors.toList());
    }

    // Get low stock items for pharmacy (useful for inventory management)
    public List<MedicineStockResponseDTO> getLowStockItems(Long pharmacyId, Integer threshold) {
        if (!pharmacyRepository.existsById(pharmacyId)) {
            throw new ResourceNotFound("Pharmacy not found with id: " + pharmacyId);
        }

        return medicineStockRepository.findByPharmacyId(pharmacyId).stream()
                .filter(stock -> stock.getQuantity() <= threshold)
                .map(dtoMapper::toMedicineStockResponseDTO)
                .collect(Collectors.toList());
    }

    // Get out of stock items for pharmacy
    public List<MedicineStockResponseDTO> getOutOfStockItems(Long pharmacyId) {
        if (!pharmacyRepository.existsById(pharmacyId)) {
            throw new ResourceNotFound("Pharmacy not found with id: " + pharmacyId);
        }

        return medicineStockRepository.findByPharmacyId(pharmacyId).stream()
                .filter(stock -> stock.getQuantity() == 0)
                .map(dtoMapper::toMedicineStockResponseDTO)
                .collect(Collectors.toList());
    }
}