package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.MedicineOrderRequestDTO;
import com.team5.graduation_project.DTOs.Request.MedicineRequestDTO;
import com.team5.graduation_project.DTOs.Response.MedicineOrderResponseDTO;
import com.team5.graduation_project.DTOs.Response.MedicineResponseDTO;
import com.team5.graduation_project.DTOs.Response.MedicineStockResponseDTO;
import com.team5.graduation_project.Exceptions.AlreadyExists;
import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.*;
import com.team5.graduation_project.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineStockRepository medicineStockRepository;
    private final MedicineOrderRepository medicineOrderRepository;
    private final PharmacyRepository pharmacyRepository;
    private final PatientRepository patientRepository;
    private final DtoMapper dtoMapper;

    // CRUD Operations for Medicine (Story 9)
    public MedicineResponseDTO createMedicine(MedicineRequestDTO request) {
        if (medicineRepository.existsByNameIgnoreCase(request.getName())) {
            throw new AlreadyExists("Medicine with name '" + request.getName() + "' already exists");
        }

        Medicine medicine = dtoMapper.toMedicineEntity(request);
        Medicine savedMedicine = medicineRepository.save(medicine);
        return dtoMapper.toMedicineResponseDTO(savedMedicine);
    }

    public List<MedicineResponseDTO> getAllMedicines() {
        return medicineRepository.findAll().stream()
                .map(dtoMapper::toMedicineResponseDTO)
                .collect(Collectors.toList());
    }

    public MedicineResponseDTO getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Medicine not found with id: " + id));
        return dtoMapper.toMedicineResponseDTO(medicine);
    }

    public List<MedicineResponseDTO> searchMedicinesByName(String name) {
        return medicineRepository.findByNameContainingIgnoreCase(name).stream()
                .map(dtoMapper::toMedicineResponseDTO)
                .collect(Collectors.toList());
    }

    public MedicineResponseDTO updateMedicine(Long id, MedicineRequestDTO request) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Medicine not found with id: " + id));

        if (!request.getName().equals(medicine.getName())) {
            if (medicineRepository.existsByNameIgnoreCase(request.getName())) {
                throw new AlreadyExists("Medicine with name '" + request.getName() + "' already exists");
            }
            medicine.setName(request.getName());
        }

        medicine.setDescription(request.getDescription());
        Medicine updatedMedicine = medicineRepository.save(medicine);
        return dtoMapper.toMedicineResponseDTO(updatedMedicine);
    }

    public void deleteMedicine(Long id) {
        if (!medicineRepository.existsById(id)) {
            throw new ResourceNotFound("Medicine not found with id: " + id);
        }

        List<MedicineOrder> existingOrders = medicineOrderRepository.findByMedicineId(id);
        if (!existingOrders.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete medicine that has existing orders");
        }
        List<MedicineStock> existingStock = medicineStockRepository.findByMedicineId(id);

        if (!existingStock.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete medicine that has existing stock");
        }

        medicineRepository.deleteById(id);
    }

    public List<MedicineStockResponseDTO> getAvailableMedicines() {
        return medicineStockRepository.findAvailableStocks().stream()
                .map(dtoMapper::toMedicineStockResponseDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineStockResponseDTO> getAvailableMedicinesByPharmacy(Long pharmacyId) {
        if (!pharmacyRepository.existsById(pharmacyId)) {
            throw new ResourceNotFound("Pharmacy not found with id: " + pharmacyId);
        }

        return medicineStockRepository.findByPharmacyId(pharmacyId).stream()
                .filter(stock -> stock.getQuantity() > 0)
                .map(dtoMapper::toMedicineStockResponseDTO)
                .collect(Collectors.toList());
    }

    // Medicine ordering for patients (Story 7)
    public MedicineOrderResponseDTO createMedicineOrder(Long patientId, MedicineOrderRequestDTO request) {
        // Validate patient exists
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFound("Patient not found with id: " + patientId));

        // Validate medicine exists
        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new ResourceNotFound("Medicine not found with id: " + request.getMedicineId()));

        // Validate pharmacy exists
        Pharmacy pharmacy = pharmacyRepository.findById(request.getPharmacyId())
                .orElseThrow(() -> new ResourceNotFound("Pharmacy not found with id: " + request.getPharmacyId()));

        // Check if pharmacy has sufficient stock
        MedicineStock stock = medicineStockRepository.findByMedicineIdAndPharmacyId(
                        request.getMedicineId(), request.getPharmacyId())
                .orElseThrow(() -> new ResourceNotFound("Medicine not available at this pharmacy"));

        if (stock.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + stock.getQuantity() +
                    ", Requested: " + request.getQuantity());
        }

        // Create order
        MedicineOrder order = dtoMapper.toMedicineOrderEntity(request, medicine, patient, pharmacy);
        MedicineOrder savedOrder = medicineOrderRepository.save(order);

        // Update stock quantity
        stock.setQuantity(stock.getQuantity() - request.getQuantity());
        medicineStockRepository.save(stock);

        return dtoMapper.toMedicineOrderResponseDTO(savedOrder);
    }

    public List<MedicineOrderResponseDTO> getPatientOrders(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFound("Patient not found with id: " + patientId);
        }

        return medicineOrderRepository.findByPatientId(patientId).stream()
                .map(dtoMapper::toMedicineOrderResponseDTO)
                .collect(Collectors.toList());
    }

    // Get pharmacy orders (for pharmacy to see their orders)
    public List<MedicineOrderResponseDTO> getPharmacyOrders(Long pharmacyId) {
        if (!pharmacyRepository.existsById(pharmacyId)) {
            throw new ResourceNotFound("Pharmacy not found with id: " + pharmacyId);
        }

        return medicineOrderRepository.findByPharmacyId(pharmacyId).stream()
                .map(dtoMapper::toMedicineOrderResponseDTO)
                .collect(Collectors.toList());
    }
}