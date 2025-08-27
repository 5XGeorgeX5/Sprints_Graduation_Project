package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.PharmacyCreateDTO;
import com.team5.graduation_project.DTOs.Response.PharmacyResponseDTO;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Pharmacy;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PharmacyService implements IPharmacyService {

    private final AccountService accountService;
    private final PharmacyRepository pharmacyRepository;
    private final DtoMapper mapper;

    @Override
    public PharmacyResponseDTO register(PharmacyCreateDTO dto) {
        Account account = accountService.createAccount(dto.getAccount(), Role.PHARMACY);

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setAccount(account);
        pharmacy.setAddress(dto.getAddress());

        pharmacyRepository.save(pharmacy);

        return mapper.toPharmacyResponseDTO(pharmacy);
    }
}
