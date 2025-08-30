package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Request.PharmacyCreateDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.PharmacyResponseDTO;
import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Pharmacy;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.AccountRepository;
import com.team5.graduation_project.Repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyService implements IPharmacyService {

    private final AccountService accountService;
    private final PharmacyRepository pharmacyRepository;
    private final DtoMapper mapper;
    private final AccountRepository accountRepository;

    @Override
    public PharmacyResponseDTO register(PharmacyCreateDTO dto) {
        Account account = accountService.createAccount(dto.getAccount(), Role.PHARMACY);

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setAccount(account);
        pharmacy.setAddress(dto.getAddress());

        pharmacyRepository.save(pharmacy);

        return mapper.toPharmacyResponseDTO(pharmacy);
    }

    @Override
    public List<PharmacyResponseDTO> getAllRegisteredPharmacies() {
        List<Pharmacy> pharmacies = pharmacyRepository.findAll();
        List<PharmacyResponseDTO> dtos = new ArrayList<>();
        for (Pharmacy pharmacy : pharmacies) {
            dtos.add(mapper.toPharmacyResponseDTO(pharmacy));
        }
        return dtos;
    }

    @Override
    public AccountResponseDTO updatePharmacy(Long id, AccountRegistrationRequestDTO dto) {
        Pharmacy pharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Doctor doesn't exist"));

        Account updatedAccount = accountService.updateAccount(pharmacy.getAccount().getId(), dto);

        return mapper.toAccountResponseDTO(updatedAccount);

    }

    @Override
    public void deletePharmacy(Long id) {
        Pharmacy pharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Pharmacy Not Found"));

        pharmacyRepository.delete(pharmacy);

        accountService.deleteAccount(pharmacy.getAccount().getId());
    }
}
