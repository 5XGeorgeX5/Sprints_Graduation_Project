package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.DoctorCreateDTO;
import com.team5.graduation_project.DTOs.Response.DoctorResponseDTO;

public interface IDoctorService {

    public DoctorResponseDTO register(DoctorCreateDTO dto);
}
