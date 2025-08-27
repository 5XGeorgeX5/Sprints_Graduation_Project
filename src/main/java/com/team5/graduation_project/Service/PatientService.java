package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Patient;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService implements IPatientService {

    private final AccountService accountService;
    private final PatientRepository patientRepository;
    private final DtoMapper mapper;

    @Override
    public AccountResponseDTO register(AccountRegistrationRequestDTO dto) {
        Account account = accountService.createAccount(dto, Role.PATIENT);

        Patient patient = new Patient();
        patient.setAccount(account);

        patientRepository.save(patient);

        return mapper.toAccountResponseDTO(account);
    }
}
