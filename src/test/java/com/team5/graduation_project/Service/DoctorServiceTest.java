package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Request.DoctorCreateDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.DoctorResponseDTO;
import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Doctor;
import com.team5.graduation_project.Models.Patient;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.AppointmentRepository;
import com.team5.graduation_project.Repository.DoctorRepository;
import com.team5.graduation_project.Repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DtoMapper mapper;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Account account;
    private Doctor doctor;
    private DoctorCreateDTO createDTO;
    private AccountRegistrationRequestDTO updateDTO;
    private DoctorResponseDTO doctorResponseDTO;
    private AccountResponseDTO accountResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setId(1L);
        account.setUsername("doc1");
        account.setEmail("doc1@example.com");
        account.setName("Dr. John");
        account.setRole(Role.DOCTOR);

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setAccount(account);
        doctor.setStartShift(LocalTime.of(9, 0));
        doctor.setEndShift(LocalTime.of(17, 0));
        doctor.setAppointmentDuration(30);

        createDTO = new DoctorCreateDTO();
        createDTO.setAccount(new AccountRegistrationRequestDTO("doc1", "doc1@example.com", "pass", "Dr. John"));
        createDTO.setStartShift(LocalTime.of(9, 0));
        createDTO.setEndShift(LocalTime.of(17, 0));
        createDTO.setAppointmentDuration(30);

        updateDTO = new AccountRegistrationRequestDTO("doc1", "doc1@example.com", "pass", "Dr. John");

        // Initialize accountResponseDTO first
        accountResponseDTO = new AccountResponseDTO(1L, "doc1", "doc1@example.com", "Dr. John", Role.DOCTOR);

        // Then assign it to doctorResponseDTO
        doctorResponseDTO = new DoctorResponseDTO();
        doctorResponseDTO.setAccount(accountResponseDTO);
        doctorResponseDTO.setStartShift(LocalTime.of(9, 0));
        doctorResponseDTO.setEndShift(LocalTime.of(17, 0));
        doctorResponseDTO.setAppointmentDuration(30);
    }


    @Test
    void register_ShouldSaveDoctorAndReturnResponse() {
        when(accountService.createAccount(createDTO.getAccount(), Role.DOCTOR)).thenReturn(account);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(mapper.toDoctorResponseDTO(any(Doctor.class))).thenReturn(doctorResponseDTO);


        DoctorResponseDTO result = doctorService.register(createDTO);

        assertNotNull(result);
        verify(accountService).createAccount(createDTO.getAccount(), Role.DOCTOR);
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void getAllRegisteredDoctors_ShouldReturnList() {
        when(doctorRepository.findAll()).thenReturn(Collections.singletonList(doctor));
        when(mapper.toDoctorResponseDTO(doctor)).thenReturn(doctorResponseDTO);

        List<DoctorResponseDTO> result = doctorService.getAllRegisteredDoctors();

        assertEquals(1, result.size());
        verify(doctorRepository).findAll();
    }

    @Test
    void update_ShouldUpdateAndReturnAccountResponse() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(accountService.updateAccount(1L, updateDTO)).thenReturn(account);
        when(mapper.toAccountResponseDTO(account)).thenReturn(accountResponseDTO);

        AccountResponseDTO result = doctorService.update(1L, updateDTO);

        assertEquals("doc1", result.getUsername());
        verify(accountService).updateAccount(1L, updateDTO);
    }

    @Test
    void update_ShouldThrow_WhenDoctorNotFound() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFound.class, () -> doctorService.update(1L, updateDTO));
    }

    @Test
    void delete_ShouldRemoveDoctorAndAccount() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        doctorService.delete(1L);

        verify(doctorRepository).delete(doctor);
        verify(accountService).deleteAccount(1L);
    }

    @Test
    void getAvailableDoctors_ShouldReturnList() {
        LocalDate date = LocalDate.now();
        when(doctorRepository.findAvailableDoctors(date)).thenReturn(Collections.singletonList(doctor));
        when(mapper.toDoctorResponseDTO(doctor)).thenReturn(doctorResponseDTO);

        List<DoctorResponseDTO> result = doctorService.getAvailableDoctors(date);

        assertEquals(1, result.size());
        verify(doctorRepository).findAvailableDoctors(date);
    }

    @Test
    void getDoctorAvailableSlots_ShouldReturnSlots() {
        LocalDate date = LocalDate.now();
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.getBookedSlots(1L, date)).thenReturn(Collections.singletonList(LocalTime.of(9, 0)));

        List<LocalTime> result = doctorService.getDoctorAvailableSlots(1L, date);

        assertFalse(result.contains(LocalTime.of(9, 0)));
        assertTrue(result.contains(LocalTime.of(9, 30)));
    }

    @Test
    void getDoctorAvailableSlots_ShouldThrow_WhenDoctorNotFound() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFound.class, () -> doctorService.getDoctorAvailableSlots(1L, LocalDate.now()));
    }

    @Test
    void getDoctorPatients_ShouldReturnList() {
        Patient patient = new Patient();
        when(patientRepository.findPatientsByDoctorId(1L)).thenReturn(List.of(patient));

        List<Patient> result = doctorService.getDoctorPatients(1L);

        assertEquals(1, result.size());
        verify(patientRepository).findPatientsByDoctorId(1L);
    }
}
