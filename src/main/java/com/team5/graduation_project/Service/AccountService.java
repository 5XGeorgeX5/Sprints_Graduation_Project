package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Request.LoginDTO;
import com.team5.graduation_project.Exceptions.AlreadyExists;
import com.team5.graduation_project.Exceptions.InvalidLoginException;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.AccountRepository;
import com.team5.graduation_project.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final DtoMapper mapper;

    public Account createAccount(AccountRegistrationRequestDTO dto, Role role) {
        accountRepository.findByUsername(dto.getUsername()).ifPresent(s -> {
            throw new AlreadyExists("User already exists");
        });
        Account account = mapper.toAccountEntity(dto);
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setRole(role);

        accountRepository.save(account);

        return account;
    }

    public String login(LoginDTO loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InvalidLoginException("Invalid username or password");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtUtil.generateToken(loginDto.getUsername());

    }

    public Account updateAccount(Long id, AccountRegistrationRequestDTO dto) {
        // Fetch existing account
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        // Check if username is being changed and already exists
        if (!account.getUsername().equals(dto.getUsername())) {
            accountRepository.findByUsername(dto.getUsername()).ifPresent(existing -> {
                throw new AlreadyExists("Username already exists: " + dto.getUsername());
            });
            account.setUsername(dto.getUsername());
        }

        // Update email if changed
        if (!account.getEmail().equals(dto.getEmail())) {
            account.setEmail(dto.getEmail());
        }

        // Update name if changed
        if (!account.getName().equals(dto.getName())) {
            account.setName(dto.getName());
        }

        // Update password if provided (always encode it)
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            account.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Save and return updated entity
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        // Check if account exists
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        // Delete account
        accountRepository.delete(account);
    }

}
