package com.team5.graduation_project.Service.Patient;


import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;

import java.util.List;

public interface IPatientService {

    AccountResponseDTO register(AccountRegistrationRequestDTO dto);
    AccountResponseDTO getPatientById(Long id);
    AccountResponseDTO updatePatient(Long id, AccountRegistrationRequestDTO dto);
    void deletePatient(Long id);
}

