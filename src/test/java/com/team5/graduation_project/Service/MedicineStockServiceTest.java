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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MedicineStockServiceTest {

    @InjectMocks
    private MedicineStockService service;

    @Mock
    private MedicineStockRepository stockRepo;
    @Mock
    private MedicineRepository medicineRepo;
    @Mock
    private PharmacyRepository pharmacyRepo;
    @Mock
    private DtoMapper dtoMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addMedicineToInventory_ShouldSave() {
        Pharmacy pharmacy = new Pharmacy();
        Medicine medicine = new Medicine();
        MedicineStockRequestDTO request = new MedicineStockRequestDTO();
        request.setMedicineId(10L);

        MedicineStock stock = new MedicineStock();
        MedicineStockResponseDTO responseDto = new MedicineStockResponseDTO();

        when(pharmacyRepo.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(medicineRepo.findById(10L)).thenReturn(Optional.of(medicine));
        when(stockRepo.findByMedicineIdAndPharmacyId(10L, 1L)).thenReturn(Optional.empty());
        when(dtoMapper.toMedicineStockEntity(request, medicine, pharmacy)).thenReturn(stock);
        when(stockRepo.save(stock)).thenReturn(stock);
        when(dtoMapper.toMedicineStockResponseDTO(stock)).thenReturn(responseDto);

        MedicineStockResponseDTO result = service.addMedicineToInventory(1L, request);

        assertNotNull(result);
        verify(stockRepo).save(stock);
    }

    @Test
    void addMedicineToInventory_ShouldThrowAlreadyExists() {
        MedicineStockRequestDTO request = new MedicineStockRequestDTO();
        request.setMedicineId(5L);

        when(pharmacyRepo.findById(1L)).thenReturn(Optional.of(new Pharmacy()));
        when(medicineRepo.findById(5L)).thenReturn(Optional.of(new Medicine()));
        when(stockRepo.findByMedicineIdAndPharmacyId(5L, 1L))
                .thenReturn(Optional.of(new MedicineStock()));

        assertThrows(AlreadyExists.class,
                () -> service.addMedicineToInventory(1L, request));
    }

    @Test
    void updateStockQuantity_ShouldUpdate() {
        MedicineStock stock = new MedicineStock();
        stock.setQuantity(5);
        MedicineStockResponseDTO dto = new MedicineStockResponseDTO();

        when(stockRepo.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepo.save(stock)).thenReturn(stock);
        when(dtoMapper.toMedicineStockResponseDTO(stock)).thenReturn(dto);

        MedicineStockResponseDTO result = service.updateStockQuantity(1L, 20);

        assertNotNull(result);
        assertEquals(20, stock.getQuantity());
    }

    @Test
    void updateStockPrice_ShouldRejectInvalidPrice() {
        when(stockRepo.findById(1L)).thenReturn(Optional.of(new MedicineStock()));

        assertThrows(IllegalArgumentException.class,
                () -> service.updateStockPrice(1L, BigDecimal.ZERO));
    }

    @Test
    void deleteMedicineStock_ShouldDelete() {
        when(stockRepo.existsById(1L)).thenReturn(true);

        service.deleteMedicineStock(1L);

        verify(stockRepo).deleteById(1L);
    }

    @Test
    void deleteMedicineStock_ShouldThrowNotFound() {
        when(stockRepo.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFound.class,
                () -> service.deleteMedicineStock(1L));
    }
}
