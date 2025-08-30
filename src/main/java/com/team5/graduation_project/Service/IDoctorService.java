package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Request.DoctorCreateDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.DoctorResponseDTO;
import com.team5.graduation_project.DTOs.Response.PatientResponseDTO;
import com.team5.graduation_project.Models.Patient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IDoctorService {

    DoctorResponseDTO register(DoctorCreateDTO dto);

    List<DoctorResponseDTO> getAllRegisteredDoctors();


    AccountResponseDTO update(Long id, AccountRegistrationRequestDTO dto);

    void delete(Long id);

    List<DoctorResponseDTO> getAvailableDoctors(LocalDate date);

    List<LocalTime> getDoctorAvailableSlots(Long id, LocalDate date);

    List<PatientResponseDTO> getDoctorPatients(Long id);


}
