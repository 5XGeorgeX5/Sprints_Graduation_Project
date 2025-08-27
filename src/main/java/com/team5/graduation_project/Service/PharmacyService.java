package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.PharmacyCreateDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.PharmacyResponseDTO;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Pharmacy;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PharmacyService implements IPharmacyService{

    private final AccountRegisterAndLogin helper;
    private final PharmacyRepository pharmacyRepository;

    @Override
    public PharmacyResponseDTO register(PharmacyCreateDTO dto) {
        Account account = helper.registerAccount(dto.getAccount(), Role.PHARMACY);

        AccountResponseDTO response = helper.registerResponse(account);

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setAccount(account);
        pharmacy.setAddress(dto.getAddress());

        pharmacyRepository.save(pharmacy);

        PharmacyResponseDTO result = new PharmacyResponseDTO();
        result.setAccount(response);
        result.setAddress(pharmacy.getAddress());

        return result;
    }
}
