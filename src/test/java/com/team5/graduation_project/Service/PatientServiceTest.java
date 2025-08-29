package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Patient;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.PatientRepository;
import com.team5.graduation_project.Service.Patient.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DtoMapper mapper;

    @InjectMocks
    private PatientService patientService;

    private Account account;
    private Patient patient;
    private AccountResponseDTO responseDTO;
    private AccountRegistrationRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setId(1L);
        account.setUsername("john_doe");
        account.setEmail("john@example.com");
        account.setName("John Doe");
        account.setRole(Role.PATIENT);

        patient = new Patient();
        patient.setId(1L);
        patient.setAccount(account);

        requestDTO = new AccountRegistrationRequestDTO("john_doe", "john@example.com", "password123", "John Doe");

        responseDTO = new AccountResponseDTO(1L, "john_doe", "john@example.com", "John Doe", Role.PATIENT);
    }

    @Test
    void register_ShouldSavePatientAndReturnResponse() {
        when(accountService.createAccount(requestDTO, Role.PATIENT)).thenReturn(account);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(mapper.toAccountResponseDTO(account)).thenReturn(responseDTO);

        AccountResponseDTO result = patientService.register(requestDTO);

        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        verify(accountService).createAccount(requestDTO, Role.PATIENT);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void getPatientById_ShouldReturnResponse_WhenPatientExists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(mapper.toAccountResponseDTO(account)).thenReturn(responseDTO);

        AccountResponseDTO result = patientService.getPatientById(1L);

        assertEquals("john_doe", result.getUsername());
        verify(patientRepository).findById(1L);
    }

    @Test
    void getPatientById_ShouldThrow_WhenNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> patientService.getPatientById(1L));
    }

    @Test
    void updatePatient_ShouldUpdateAndReturnResponse_WhenPatientExists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(accountService.updateAccount(1L, requestDTO)).thenReturn(account);
        when(mapper.toAccountResponseDTO(account)).thenReturn(responseDTO);

        AccountResponseDTO result = patientService.updatePatient(1L, requestDTO);

        assertEquals("john_doe", result.getUsername());
        verify(accountService).updateAccount(1L, requestDTO);
    }

    @Test
    void updatePatient_ShouldThrow_WhenNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> patientService.updatePatient(1L, requestDTO));
    }

    @Test
    void deletePatient_ShouldDeletePatientAndAccount_WhenPatientExists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        patientService.deletePatient(1L);

        verify(patientRepository).deleteById(1L);
        verify(accountService).deleteAccount(1L);
    }

    @Test
    void deletePatient_ShouldThrow_WhenNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> patientService.deletePatient(1L));
    }
}
