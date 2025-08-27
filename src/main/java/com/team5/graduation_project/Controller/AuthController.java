package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Request.DoctorCreateDTO;
import com.team5.graduation_project.DTOs.Request.LoginDTO;
import com.team5.graduation_project.DTOs.Request.PharmacyCreateDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.DoctorResponseDTO;
import com.team5.graduation_project.DTOs.Response.PharmacyResponseDTO;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.AccountService;
import com.team5.graduation_project.Service.IDoctorService;
import com.team5.graduation_project.Service.IPatientService;
import com.team5.graduation_project.Service.IPharmacyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IDoctorService doctorService;
    private final IPatientService patientService;
    private final IPharmacyService pharmacyService;
    private final AccountService accountService;
    private final DtoMapper mapper;

    @PostMapping("/doctor/register")
    public ResponseEntity<BaseResponse> registerAsDoctor(@Valid @RequestBody DoctorCreateDTO dto) {
        DoctorResponseDTO result = doctorService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse("Registered", result));
    }

    @PostMapping("/patient/register")
    public ResponseEntity<BaseResponse> registerAsPatient(@Valid @RequestBody AccountRegistrationRequestDTO dto) {
        AccountResponseDTO result = patientService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse("Registered", result));
    }

    @PostMapping("/pharmacy/register")
    public ResponseEntity<BaseResponse> registerAsPharmacy(@Valid @RequestBody PharmacyCreateDTO dto) {
        PharmacyResponseDTO result = pharmacyService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse("Registered", result));
    }

    @PostMapping("/admin/register")
    public ResponseEntity<BaseResponse> registerAsAdmin(@Valid @RequestBody AccountRegistrationRequestDTO dto) {
        Account account = accountService.createAccount(dto, Role.ADMIN);
        AccountResponseDTO result = mapper.toAccountResponseDTO(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse("Registered", result));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginDTO dto) {
        String token = accountService.login(dto);
        return ResponseEntity.ok(new BaseResponse("Login", token));
    }
}
