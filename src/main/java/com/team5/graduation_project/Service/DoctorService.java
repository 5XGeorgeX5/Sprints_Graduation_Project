package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.DoctorCreateDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.DoctorResponseDTO;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Doctor;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorService implements IDoctorService{

    private final AccountRegisterAndLogin helper;
    private final DoctorRepository doctorRepository;

    @Override
    public DoctorResponseDTO register(DoctorCreateDTO dto) {
        Account account = helper.registerAccount(dto.getAccount(), Role.DOCTOR);

        AccountResponseDTO response = helper.registerResponse(account);

        Doctor doctor = new Doctor();
        doctor.setAccount(account);
        doctor.setStartShift(dto.getStartShift());
        doctor.setEndShift(dto.getEndShift());
        doctor.setAppointmentDuration(dto.getAppointmentDuration());

        doctorRepository.save(doctor);

        DoctorResponseDTO result = new DoctorResponseDTO();
        result.setAccount(response);
        result.setStartShift(doctor.getStartShift());
        result.setEndShift(doctor.getEndShift());
        result.setAppointmentDuration(doctor.getAppointmentDuration());


        return result;
    }
}
