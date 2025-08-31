package com.team5.graduation_project.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.PharmacyResponseDTO;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Service.AccountService;
import com.team5.graduation_project.Service.PharmacyService;
import com.team5.graduation_project.Util.JwtFilter;
import com.team5.graduation_project.Util.JwtUtil;
import com.team5.graduation_project.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = PharmacyController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = JwtFilter.class))
class PharmacyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PharmacyService pharmacyService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllRegisteredPharmacies_returnsOk() throws Exception {
        AccountResponseDTO pharmacyAccount1 = new AccountResponseDTO(1L, "pharmacy1", "pharmacy1@example.com", "pharmacy1", Role.PHARMACY);
        AccountResponseDTO pharmacyAccount2 = new AccountResponseDTO(2L, "pharmacy2", "pharmacy2@example.com", "pharmacy2", Role.PHARMACY);

        List<PharmacyResponseDTO> mockPharmacies = List.of(
                new PharmacyResponseDTO(pharmacyAccount1, "123 Street"),
                new PharmacyResponseDTO(pharmacyAccount2, "456 Street")
        );

        when(pharmacyService.getAllRegisteredPharmacies()).thenReturn(mockPharmacies);

        mockMvc.perform(get("/api/pharmacy/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registered pharmacies retrieved successfully"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].account.id").value(1L))
                .andExpect(jsonPath("$.data[0].account.name").value("pharmacy1"));
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deletePharmacy_returnsNoContent() throws Exception {
        Long pharmacyId = 1L;

        Mockito.doNothing().when(pharmacyService).deletePharmacy(pharmacyId);

        mockMvc.perform(delete("/api/pharmacy/delete/{pharmacyId}", pharmacyId).with(csrf()))
                .andExpect(status().isNoContent());
    }
}