package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.DoctorCreateDTO;
import com.team5.graduation_project.DTOs.Response.DoctorResponseDTO;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Doctor;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorService implements IDoctorService {

    private final AccountService accountService;
    private final DoctorRepository doctorRepository;
    private final DtoMapper dtoMapper;

    @Override
    public DoctorResponseDTO register(DoctorCreateDTO dto) {
        Account account = accountService.createAccount(dto.getAccount(), Role.DOCTOR);

        Doctor doctor = new Doctor();
        doctor.setAccount(account);
        doctor.setStartShift(dto.getStartShift());
        doctor.setEndShift(dto.getEndShift());
        doctor.setAppointmentDuration(dto.getAppointmentDuration());

        doctorRepository.save(doctor);

        return dtoMapper.toDoctorResponseDTO(doctor);
    }
}
