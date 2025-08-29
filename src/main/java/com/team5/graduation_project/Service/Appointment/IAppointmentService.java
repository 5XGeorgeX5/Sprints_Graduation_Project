package com.team5.graduation_project.Service.Appointment;

import com.team5.graduation_project.DTOs.Request.AppointmentRequestDTO;
import com.team5.graduation_project.DTOs.Response.AppointmentResponseDTO;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class IAppointmentService implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DtoMapper mapper;


    @Override
    public AppointmentResponseDTO createAppointment(Long patientId, AppointmentRequestDTO requestDTO) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Patient not found"));
        Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Doctor not found"));
        LocalDateTime appointmentTime = requestDTO.getAppointmentTime();
        if (appointmentTime == null || appointmentTime.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(BAD_REQUEST, "Appointment time must be in the future");
        }
        List<Appointment> conflictingAppointments = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctor.getId(), appointmentTime.minusMinutes(30), appointmentTime.plusMinutes(30));
        if (!conflictingAppointments.isEmpty()) {
            throw new ResponseStatusException(CONFLICT, "Doctor is not available at the requested time");
        }
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentTime(appointmentTime)
                .build();
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapper.toAppointmentResponseDTO(savedAppointment);
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Appointment not found"));
        return mapper.toAppointmentResponseDTO(appointment);
    }
    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO requestDTO) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Appointment not found"));
        Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Doctor not found"));
        LocalDateTime appointmentTime = requestDTO.getAppointmentTime();
        if (appointmentTime == null || appointmentTime.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(BAD_REQUEST, "Appointment time must be in the future");
        } List<Appointment> conflictingAppointments = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctor.getId(), appointmentTime.minusMinutes(30), appointmentTime.plusMinutes(30))
                .stream()
                .filter(a -> !a.getId().equals(id))
                .collect(Collectors.toList());
        if (!conflictingAppointments.isEmpty()) {
            throw new ResponseStatusException(CONFLICT, "Doctor is not available at the requested time");
        }
        appointment.setDoctor(doctor);
        appointment.setAppointmentTime(appointmentTime);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return mapper.toAppointmentResponseDTO(updatedAppointment);
    }
    @Override
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Appointment not found");
        }
        appointmentRepository.deleteById(id);
    }
}