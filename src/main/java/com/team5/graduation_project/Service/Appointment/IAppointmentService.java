package com.team5.graduation_project.Service.Appointment;

import com.team5.graduation_project.DTOs.Request.AppointmentRequestDTO;
import com.team5.graduation_project.DTOs.Response.AppointmentResponseDTO;
import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Appointment;
import com.team5.graduation_project.Models.Doctor;
import com.team5.graduation_project.Models.Patient;
import com.team5.graduation_project.Repository.AppointmentRepository;
import com.team5.graduation_project.Repository.DoctorRepository;
import com.team5.graduation_project.Repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface IAppointmentService {
    AppointmentResponseDTO createAppointment(Long patientId, AppointmentRequestDTO requestDTO);

    AppointmentResponseDTO getAppointmentById(Long id);

    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO requestDTO);

    void deleteAppointment(Long id);

    AppointmentResponseDTO addFollowupNotes(Long id, String note);


}