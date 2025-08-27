package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Request.LoginDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.Exceptions.AlreadyExists;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.AccountRepository;
import com.team5.graduation_project.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public AccountResponseDTO register(AccountRegistrationRequestDTO dto) {
        accountRepository.findByUsername(dto.getUsername())
                .ifPresent(s -> {
                    throw new AlreadyExists("user already exists");
                });
        Account account = new Account();
        account.setEmail(dto.getEmail());
        account.setName(dto.getName());
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setRole(Role.ADMIN);

        accountRepository.save(account);

        AccountResponseDTO response = new AccountResponseDTO();
        response.setUsername(account.getUsername());
        response.setEmail(account.getEmail());
        response.setName(account.getName());
        response.setRole(account.getRole());
        response.setId(account.getId());

        return response;
    }

    @Override
    public String login(LoginDTO dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.getUsername(),
                dto.getPassword()
        ));
        return jwtUtil.generateToken(dto.getUsername());
    }
}
