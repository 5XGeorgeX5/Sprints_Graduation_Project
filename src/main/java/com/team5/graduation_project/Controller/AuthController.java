package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Request.DoctorCreateDTO;
import com.team5.graduation_project.DTOs.Request.LoginDTO;
import com.team5.graduation_project.DTOs.Request.PharmacyCreateDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.DoctorResponseDTO;
import com.team5.graduation_project.DTOs.Response.PharmacyResponseDTO;
import com.team5.graduation_project.Models.Patient;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.*;
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
    private final IAccountService accountService;
    private final IPharmacyService pharmacyService;
    private final AccountRegisterAndLogin account;

    @PostMapping("/doctor/register")
    public ResponseEntity<BaseResponse> registerAsDoctor(@RequestBody DoctorCreateDTO dto){
        DoctorResponseDTO result = doctorService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse("Registered" , result));
    }

    @PostMapping("/patient/register")
    public ResponseEntity<BaseResponse> registerAsPatient(@RequestBody AccountRegistrationRequestDTO dto){
        AccountResponseDTO result = patientService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse("Registered" , result));
    }

    @PostMapping("/pharmacy/register")
    public ResponseEntity<BaseResponse> registerAsPharmacy(@RequestBody PharmacyCreateDTO dto){
        PharmacyResponseDTO result = pharmacyService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse("Registered" , result));
    }

    @PostMapping("/admin/register")
    public ResponseEntity<BaseResponse> registerAsAdmin(@RequestBody AccountRegistrationRequestDTO dto){
        AccountResponseDTO result = accountService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse("Registered" , result));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@RequestBody LoginDTO dto){
        String token = account.login(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse("Login" , token));
    }


}
