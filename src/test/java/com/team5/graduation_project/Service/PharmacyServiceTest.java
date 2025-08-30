package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Request.PharmacyCreateDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.PharmacyResponseDTO;
import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Pharmacy;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.AccountRepository;
import com.team5.graduation_project.Repository.PharmacyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PharmacyServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private PharmacyRepository pharmacyRepository;

    @Mock
    private DtoMapper mapper;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private PharmacyService pharmacyService;

    private Account account;
    private Pharmacy pharmacy;
    private PharmacyCreateDTO createDTO;
    private AccountRegistrationRequestDTO updateDTO;
    private PharmacyResponseDTO pharmacyResponseDTO;
    private AccountResponseDTO accountResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setId(1L);
        account.setUsername("pharma1");
        account.setEmail("pharma1@example.com");
        account.setName("Pharma One");
        account.setRole(Role.PHARMACY);

        pharmacy = new Pharmacy();
        pharmacy.setId(1L);
        pharmacy.setAccount(account);
        pharmacy.setAddress("123 Main St");

        createDTO = new PharmacyCreateDTO();
        createDTO.setAccount(new AccountRegistrationRequestDTO("pharma1", "pharma1@example.com", "pass", "Pharma One"));
        createDTO.setAddress("123 Main St");

        updateDTO = new AccountRegistrationRequestDTO("pharma1", "pharma1@example.com", "pass", "Pharma One");

        accountResponseDTO = new AccountResponseDTO(1L, "pharma1", "pharma1@example.com", "Pharma One", Role.PHARMACY);

        pharmacyResponseDTO = new PharmacyResponseDTO();
        pharmacyResponseDTO.setAccount(accountResponseDTO);
        pharmacyResponseDTO.setAddress("123 Main St");
    }

    @Test
    void register_ShouldSavePharmacyAndReturnResponse() {
        when(accountService.createAccount(createDTO.getAccount(), Role.PHARMACY)).thenReturn(account);
        when(pharmacyRepository.save(any(Pharmacy.class))).thenReturn(pharmacy);
        when(mapper.toPharmacyResponseDTO(any(Pharmacy.class))).thenReturn(pharmacyResponseDTO);

        PharmacyResponseDTO result = pharmacyService.register(createDTO);

        assertNotNull(result);
        assertEquals("123 Main St", result.getAddress());

        verify(accountService).createAccount(createDTO.getAccount(), Role.PHARMACY);
        verify(pharmacyRepository).save(any(Pharmacy.class));
        verify(mapper).toPharmacyResponseDTO(any(Pharmacy.class));
    }


    @Test
    void getAllRegisteredPharmacies_ShouldReturnList() {
        when(pharmacyRepository.findAll()).thenReturn(Collections.singletonList(pharmacy));
        when(mapper.toPharmacyResponseDTO(pharmacy)).thenReturn(pharmacyResponseDTO);

        List<PharmacyResponseDTO> result = pharmacyService.getAllRegisteredPharmacies();

        assertEquals(1, result.size());
        assertEquals("123 Main St", result.get(0).getAddress());
        verify(pharmacyRepository).findAll();
    }

    @Test
    void updatePharmacy_ShouldUpdateAndReturnAccountResponse() {
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(accountService.updateAccount(1L, updateDTO)).thenReturn(account);
        when(mapper.toAccountResponseDTO(account)).thenReturn(accountResponseDTO);

        AccountResponseDTO result = pharmacyService.updatePharmacy(1L, updateDTO);

        assertEquals("pharma1", result.getUsername());
        verify(accountService).updateAccount(1L, updateDTO);
    }

    @Test
    void updatePharmacy_ShouldThrow_WhenNotFound() {
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> pharmacyService.updatePharmacy(1L, updateDTO));
    }

    @Test
    void deletePharmacy_ShouldDeletePharmacyAndAccount() {
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));

        pharmacyService.deletePharmacy(1L);

        verify(pharmacyRepository).delete(pharmacy);
        verify(accountService).deleteAccount(1L);
    }

    @Test
    void deletePharmacy_ShouldThrow_WhenNotFound() {
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> pharmacyService.deletePharmacy(1L));
    }
}
