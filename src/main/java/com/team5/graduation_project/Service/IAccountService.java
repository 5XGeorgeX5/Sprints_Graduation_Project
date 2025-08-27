package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Request.LoginDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;

public interface IAccountService {

    public AccountResponseDTO register(AccountRegistrationRequestDTO dto);
}
