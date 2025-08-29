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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicineServiceTest {

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private MedicineStockRepository medicineStockRepository;

    @Mock
    private MedicineOrderRepository medicineOrderRepository;

    @Mock
    private PharmacyRepository pharmacyRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DtoMapper dtoMapper;

    @InjectMocks
    private MedicineService medicineService;

    private Medicine medicine;
    private MedicineRequestDTO medicineRequest;
    private MedicineResponseDTO medicineResponse;
    private MedicineStock medicineStock;
    private MedicineStockResponseDTO stockResponse;
    private Patient patient;
    private Pharmacy pharmacy;
    private MedicineOrder medicineOrder;
    private MedicineOrderRequestDTO orderRequest;
    private MedicineOrderResponseDTO orderResponse;

    @BeforeEach
    void setUp() {
        medicine = Medicine.builder()
                .id(1L)
                .name("Paracetamol")
                .description("Pain reliever")
                .build();

        medicineRequest = MedicineRequestDTO.builder()
                .name("Paracetamol")
                .description("Pain reliever")
                .build();

        medicineResponse = MedicineResponseDTO.builder()
                .id(1L)
                .name("Paracetamol")
                .description("Pain reliever")
                .build();

        medicineStock = MedicineStock.builder()
                .id(1L)
                .medicine(medicine)
                .quantity(100)
                .build();

        stockResponse = MedicineStockResponseDTO.builder()
                .id(1L)
                .quantity(100)
                .build();

        patient = Patient.builder()
                .id(1L)
                .build();

        pharmacy = Pharmacy.builder()
                .id(1L)
                .build();

        orderRequest = MedicineOrderRequestDTO.builder()
                .medicineId(1L)
                .pharmacyId(1L)
                .quantity(5)
                .build();

        medicineOrder = MedicineOrder.builder()
                .id(1L)
                .medicine(medicine)
                .patient(patient)
                .pharmacy(pharmacy)
                .quantity(5)
                .build();

        orderResponse = MedicineOrderResponseDTO.builder()
                .id(1L)
                .quantity(5)
                .build();
    }

    @Test
    void createMedicine_WithValidData_ShouldCreateMedicine() {
        when(medicineRepository.existsByNameIgnoreCase(medicineRequest.getName())).thenReturn(false);
        when(dtoMapper.toMedicineEntity(medicineRequest)).thenReturn(medicine);
        when(medicineRepository.save(medicine)).thenReturn(medicine);
        when(dtoMapper.toMedicineResponseDTO(medicine)).thenReturn(medicineResponse);

        MedicineResponseDTO result = medicineService.createMedicine(medicineRequest);

        assertNotNull(result);
        assertEquals("Paracetamol", result.getName());
        verify(medicineRepository).save(medicine);
    }

    @Test
    void createMedicine_WithExistingName_ShouldThrowAlreadyExistsException() {
        when(medicineRepository.existsByNameIgnoreCase(medicineRequest.getName())).thenReturn(true);

        assertThrows(AlreadyExists.class, () -> medicineService.createMedicine(medicineRequest));
        verify(medicineRepository, never()).save(any());
    }

    @Test
    void getAllMedicines_ShouldReturnAllMedicines() {
        List<Medicine> medicines = Collections.singletonList(medicine);
        when(medicineRepository.findAll()).thenReturn(medicines);
        when(dtoMapper.toMedicineResponseDTO(medicine)).thenReturn(medicineResponse);

        List<MedicineResponseDTO> result = medicineService.getAllMedicines();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Paracetamol", result.get(0).getName());
    }

    @Test
    void getMedicineById_WithValidId_ShouldReturnMedicine() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(dtoMapper.toMedicineResponseDTO(medicine)).thenReturn(medicineResponse);

        MedicineResponseDTO result = medicineService.getMedicineById(1L);

        assertNotNull(result);
        assertEquals("Paracetamol", result.getName());
    }

    @Test
    void getMedicineById_WithInvalidId_ShouldThrowResourceNotFoundException() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> medicineService.getMedicineById(1L));
    }

    @Test
    void searchMedicinesByName_ShouldReturnMatchingMedicines() {
        List<Medicine> medicines = Collections.singletonList(medicine);
        when(medicineRepository.findByNameContainingIgnoreCase("Para")).thenReturn(medicines);
        when(dtoMapper.toMedicineResponseDTO(medicine)).thenReturn(medicineResponse);

        List<MedicineResponseDTO> result = medicineService.searchMedicinesByName("Para");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Paracetamol", result.get(0).getName());
    }

    @Test
    void updateMedicine_WithValidData_ShouldUpdateMedicine() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(medicine)).thenReturn(medicine);
        when(dtoMapper.toMedicineResponseDTO(medicine)).thenReturn(medicineResponse);

        MedicineResponseDTO result = medicineService.updateMedicine(1L, medicineRequest);

        assertNotNull(result);
        verify(medicineRepository).save(medicine);
    }

    @Test
    void updateMedicine_WithInvalidId_ShouldThrowResourceNotFoundException() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> medicineService.updateMedicine(1L, medicineRequest));
    }

    @Test
    void deleteMedicine_WithValidId_ShouldDeleteMedicine() {
        when(medicineRepository.existsById(1L)).thenReturn(true);
        when(medicineOrderRepository.findByMedicineId(1L)).thenReturn(List.of());
        when(medicineStockRepository.findByMedicineId(1L)).thenReturn(List.of());

        medicineService.deleteMedicine(1L);

        verify(medicineRepository).deleteById(1L);
    }

    @Test
    void deleteMedicine_WithExistingOrders_ShouldThrowIllegalArgumentException() {
        when(medicineRepository.existsById(1L)).thenReturn(true);
        when(medicineOrderRepository.findByMedicineId(1L)).thenReturn(Collections.singletonList(medicineOrder));

        assertThrows(IllegalArgumentException.class, () -> medicineService.deleteMedicine(1L));
        verify(medicineRepository, never()).deleteById(1L);
    }

    @Test
    void deleteMedicine_WithInvalidId_ShouldThrowResourceNotFoundException() {
        when(medicineRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFound.class, () -> medicineService.deleteMedicine(1L));
    }

    @Test
    void getAvailableMedicines_ShouldReturnAvailableStock() {
        List<MedicineStock> stocks = Collections.singletonList(medicineStock);
        when(medicineStockRepository.findAvailableStocks()).thenReturn(stocks);
        when(dtoMapper.toMedicineStockResponseDTO(medicineStock)).thenReturn(stockResponse);

        List<MedicineStockResponseDTO> result = medicineService.getAvailableMedicines();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAvailableMedicinesByPharmacy_WithValidPharmacyId_ShouldReturnPharmacyStock() {
        medicineStock.setQuantity(10); // Available quantity
        List<MedicineStock> stocks = List.of(medicineStock);
        when(pharmacyRepository.existsById(1L)).thenReturn(true);
        when(medicineStockRepository.findByPharmacyId(1L)).thenReturn(stocks);
        when(dtoMapper.toMedicineStockResponseDTO(medicineStock)).thenReturn(stockResponse);

        List<MedicineStockResponseDTO> result = medicineService.getAvailableMedicinesByPharmacy(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAvailableMedicinesByPharmacy_WithInvalidPharmacyId_ShouldThrowResourceNotFoundException() {
        when(pharmacyRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFound.class, () -> medicineService.getAvailableMedicinesByPharmacy(1L));
    }

    @Test
    void createMedicineOrder_WithValidData_ShouldCreateOrder() {
        medicineStock.setQuantity(100); // Sufficient stock
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(medicineStockRepository.findByMedicineIdAndPharmacyId(1L, 1L)).thenReturn(Optional.of(medicineStock));
        when(dtoMapper.toMedicineOrderEntity(orderRequest, medicine, patient, pharmacy)).thenReturn(medicineOrder);
        when(medicineOrderRepository.save(medicineOrder)).thenReturn(medicineOrder);
        when(dtoMapper.toMedicineOrderResponseDTO(medicineOrder)).thenReturn(orderResponse);

        MedicineOrderResponseDTO result = medicineService.createMedicineOrder(1L, orderRequest);

        assertNotNull(result);
        assertEquals(5, result.getQuantity());
        verify(medicineStockRepository).save(medicineStock);
        assertEquals(95, medicineStock.getQuantity()); // Stock should be reduced
    }

    @Test
    void createMedicineOrder_WithInsufficientStock_ShouldThrowIllegalArgumentException() {
        medicineStock.setQuantity(3); // Insufficient stock
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(medicineStockRepository.findByMedicineIdAndPharmacyId(1L, 1L)).thenReturn(Optional.of(medicineStock));

        assertThrows(IllegalArgumentException.class, () -> medicineService.createMedicineOrder(1L, orderRequest));
    }

    @Test
    void getPatientOrders_WithValidPatientId_ShouldReturnOrders() {
        List<MedicineOrder> orders = Collections.singletonList(medicineOrder);
        when(patientRepository.existsById(1L)).thenReturn(true);
        when(medicineOrderRepository.findByPatientId(1L)).thenReturn(orders);
        when(dtoMapper.toMedicineOrderResponseDTO(medicineOrder)).thenReturn(orderResponse);

        List<MedicineOrderResponseDTO> result = medicineService.getPatientOrders(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getPharmacyOrders_WithValidPharmacyId_ShouldReturnOrders() {
        List<MedicineOrder> orders = Collections.singletonList(medicineOrder);
        when(pharmacyRepository.existsById(1L)).thenReturn(true);
        when(medicineOrderRepository.findByPharmacyId(1L)).thenReturn(orders);
        when(dtoMapper.toMedicineOrderResponseDTO(medicineOrder)).thenReturn(orderResponse);

        List<MedicineOrderResponseDTO> result = medicineService.getPharmacyOrders(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}