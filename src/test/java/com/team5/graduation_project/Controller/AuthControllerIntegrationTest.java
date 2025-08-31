package com.team5.graduation_project.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Request.DoctorCreateDTO;
import com.team5.graduation_project.DTOs.Request.LoginDTO;
import com.team5.graduation_project.DTOs.Request.PharmacyCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerDoctor_returns201() throws Exception {
        AccountRegistrationRequestDTO accountDto = new AccountRegistrationRequestDTO("doctor", "doctor@gmail.com", "password", "doctor");
        DoctorCreateDTO dto = new DoctorCreateDTO(accountDto, LocalTime.of(9, 0), LocalTime.of(17, 0), 30);

        mockMvc.perform(post("/api/auth/doctor/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Registered"));
    }

    @Test
    void registerPatient_returns201() throws Exception {
        AccountRegistrationRequestDTO dto = new AccountRegistrationRequestDTO(
                "patient", "patient@gmail.com", "password", "patient");

        mockMvc.perform(post("/api/auth/patient/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Registered"));
    }

    @Test
    void registerPharmacy_returns201() throws Exception {
        AccountRegistrationRequestDTO accountDto = new AccountRegistrationRequestDTO(
                "pharmacy", "pharmacy@gmail.com", "password", "pharmacy");
        PharmacyCreateDTO dto = new PharmacyCreateDTO(accountDto, "Pharmacy Street 123");

        mockMvc.perform(post("/api/auth/pharmacy/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Registered"));
    }

    @Test
    void registerAdmin_returns201() throws Exception {
        AccountRegistrationRequestDTO dto = new AccountRegistrationRequestDTO(
                "admin", "admin@gmail.com", "password", "admin");

        mockMvc.perform(post("/api/auth/admin/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Registered"));
    }

    @Test
    void login_returns200() throws Exception {
        // First, create an account (doctor) for login
        AccountRegistrationRequestDTO accountDto = new AccountRegistrationRequestDTO(
                "doctorLogin", "doctorLogin@gmail.com", "password", "doctor");
        DoctorCreateDTO doctorDto = new DoctorCreateDTO(accountDto, LocalTime.of(9, 0), LocalTime.of(17, 0), 30);

        mockMvc.perform(post("/api/auth/doctor/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorDto)))
                .andExpect(status().isCreated());

        // Then, attempt login
        LoginDTO loginDto = new LoginDTO("doctorLogin", "password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login"))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    void login_fakeUser_returns401() throws Exception {
        LoginDTO loginDto = new LoginDTO("fakeUser", "fakePassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized()).andDo(print());
    }
}
