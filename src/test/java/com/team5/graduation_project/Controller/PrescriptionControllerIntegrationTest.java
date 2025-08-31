package com.team5.graduation_project.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.graduation_project.DTOs.Request.PrescriptionRequestDTO;
import com.team5.graduation_project.Models.*;
import com.team5.graduation_project.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class PrescriptionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    private Long doctorId;
    private Long patientId;
    private Long medicineId;

    @BeforeEach
    void setUp() {
        // Clean up DB
        prescriptionRepository.deleteAll();
        doctorRepository.deleteAll();
        patientRepository.deleteAll();
        accountRepository.deleteAll();
        medicineRepository.deleteAll();

        // Create Doctor Account and Doctor
        Account doctorAccount = Account.builder()
                .username("doctor")
                .password("password")
                .role(Role.DOCTOR)
                .email("doctor@example.com")
                .name("doctor")
                .build();

        Doctor doctor = new Doctor();
        doctor.setAccount(doctorAccount);
        doctor.setStartShift(LocalTime.of(9, 0));
        doctor.setEndShift(LocalTime.of(17, 0));
        doctor.setAppointmentDuration(30);
        doctorRepository.saveAndFlush(doctor);
        doctorId = doctor.getId();

        // Create Patient Account and Patient
        Account patientAccount = Account.builder()
                .username("patient1")
                .password("password")
                .role(Role.PATIENT)
                .email("patient@example.com")
                .name("patient")
                .build();

        Patient patient = new Patient();
        patient.setAccount(patientAccount);
        patientRepository.saveAndFlush(patient);
        patientId = patient.getId();


        Medicine medicine = Medicine.builder()
                .name("med1")
                .description("hopefully the patient won't die")
                .build();
        medicineRepository.saveAndFlush(medicine);
        medicineId = medicine.getId();
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"DOCTOR"})
    void createPrescription_returnsCreated() throws Exception {
        PrescriptionRequestDTO requestDTO = PrescriptionRequestDTO.builder()
                .doctorId(doctorId)
                .patientId(patientId)
                .medicineId(medicineId)
                .dosage("2 tablets")
                .instruction("After meals")
                .build();

        mockMvc.perform(post("/api/prescription/{doctor_id}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Prescription created successfully"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.dosage").value("2 tablets"));
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"DOCTOR"})
    void getDoctorPrescription_returnsPrescription() throws Exception {
        // First, create a prescription
        PrescriptionRequestDTO requestDTO = PrescriptionRequestDTO.builder()
                .doctorId(doctorId)
                .patientId(patientId)
                .medicineId(medicineId)
                .dosage("2 tablets")
                .instruction("After meals")
                .build();

        String response = mockMvc.perform(post("/api/prescription/{doctor_id}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long prescriptionId = objectMapper.readTree(response).path("data").path("id").asLong();

        mockMvc.perform(get("/api/prescription/{prescriptionId}", prescriptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Prescription retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(prescriptionId));
    }
}