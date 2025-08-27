package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.PharmacyCreateDTO;
import com.team5.graduation_project.DTOs.Response.PharmacyResponseDTO;

public interface IPharmacyService {

    PharmacyResponseDTO register(PharmacyCreateDTO dto);
}
