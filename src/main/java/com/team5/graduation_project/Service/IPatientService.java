package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;

public interface IPatientService {

    public AccountResponseDTO register(AccountRegistrationRequestDTO dto);
}
