package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Request.PharmacyCreateDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.PharmacyResponseDTO;

import java.util.List;

public interface IPharmacyService {

    PharmacyResponseDTO register(PharmacyCreateDTO dto);

    List<PharmacyResponseDTO> getAllRegisteredPharmacies();

    AccountResponseDTO updatePharmacy(Long id, AccountRegistrationRequestDTO dto);

    void deletePharmacy(Long id);
}
