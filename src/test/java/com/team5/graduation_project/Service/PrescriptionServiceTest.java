package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.PrescriptionRequestDTO;
import com.team5.graduation_project.DTOs.Response.PrescriptionResponseDTO;
import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Doctor;
import com.team5.graduation_project.Models.Medicine;
import com.team5.graduation_project.Models.Patient;
import com.team5.graduation_project.Models.Prescription;
import com.team5.graduation_project.Repository.DoctorRepository;
import com.team5.graduation_project.Repository.MedicineRepository;
import com.team5.graduation_project.Repository.PatientRepository;
import com.team5.graduation_project.Repository.PrescriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private DtoMapper mapper;

    @InjectMocks
    private PrescriptionService prescriptionService;

    private Doctor doctor;
    private Patient patient;
    private Medicine medicine;
    private Prescription prescription;
    private PrescriptionRequestDTO requestDTO;
    private PrescriptionResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doctor = new Doctor();
        doctor.setId(1L);

        patient = new Patient();
        patient.setId(1L);

        medicine = new Medicine();
        medicine.setId(1L);

        prescription = new Prescription();
        prescription.setId(1L);
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
        prescription.setMedicine(medicine);
        prescription.setDosage("2 pills/day");
        prescription.setInstruction("After meals");

        requestDTO = new PrescriptionRequestDTO();
        requestDTO.setPatientId(1L);
        requestDTO.setMedicineId(1L);
        requestDTO.setDosage("2 pills/day");
        requestDTO.setInstruction("After meals");

        responseDTO = new PrescriptionResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setDoctorId(1L);
        responseDTO.setPatientId(1L);
        responseDTO.setMedicineId(1L);
        responseDTO.setDosage("2 pills/day");
        responseDTO.setInstruction("After meals");
    }

    @Test
    void createPrescription_ShouldReturnResponse_WhenAllExist() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);
        when(mapper.toPrescriptionResponseDTO(any(Prescription.class))).thenReturn(responseDTO);

        PrescriptionResponseDTO result = prescriptionService.createPrescription(1L, requestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getDoctorId());
        assertEquals("2 pills/day", result.getDosage());
        verify(prescriptionRepository).save(any(Prescription.class));
        verify(mapper).toPrescriptionResponseDTO(any(Prescription.class));
    }

    @Test
    void createPrescription_ShouldThrow_WhenDoctorNotFound() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> prescriptionService.createPrescription(1L, requestDTO));
    }

    @Test
    void createPrescription_ShouldThrow_WhenPatientNotFound() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> prescriptionService.createPrescription(1L, requestDTO));
    }

    @Test
    void createPrescription_ShouldThrow_WhenMedicineNotFound() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> prescriptionService.createPrescription(1L, requestDTO));
    }

    @Test
    void getDoctorPresciprion_ShouldReturnResponse_WhenExists() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(mapper.toPrescriptionResponseDTO(any(Prescription.class))).thenReturn(responseDTO);

        PrescriptionResponseDTO result = prescriptionService.getDoctorPresciprion(1L);

        assertNotNull(result);
        assertEquals(1L, result.getDoctorId());
        verify(mapper).toPrescriptionResponseDTO(any(Prescription.class));
    }

    @Test
    void getDoctorPresciprion_ShouldThrow_WhenNotFound() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> prescriptionService.getDoctorPresciprion(1L));
    }
}
