package com.team5.graduation_project.Service.Patient;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Patient;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.PatientRepository;
import com.team5.graduation_project.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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

    @Override
    public List<AccountResponseDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patient -> mapper.toAccountResponseDTO(patient.getAccount()))
                .toList();
    }

    @Override
    public AccountResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Patient not found"));

        return mapper.toAccountResponseDTO(patient.getAccount());
    }

    @Override
    public AccountResponseDTO updatePatient(Long id, AccountRegistrationRequestDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Patient not found"));

        Account updatedAccount = accountService.updateAccount(patient.getAccount().getId(), dto);

        return mapper.toAccountResponseDTO(updatedAccount);
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Patient not found"));

        patientRepository.deleteById(id);

        accountService.deleteAccount(patient.getAccount().getId());
    }

}
