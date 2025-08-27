package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRegisterAndLogin helper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public AccountResponseDTO register(AccountRegistrationRequestDTO dto) {
        Account account = helper.registerAccount(dto, Role.ADMIN);

        return helper.registerResponse(account);
    }
}
