package com.team5.graduation_project.Service.Appointment;

import com.team5.graduation_project.DTOs.Request.AppointmentRequestDTO;
import com.team5.graduation_project.DTOs.Response.AppointmentResponseDTO;
import java.util.List;
public interface AppointmentService {
    AppointmentResponseDTO createAppointment(Long patientId, AppointmentRequestDTO requestDTO);
    AppointmentResponseDTO getAppointmentById(Long id);
    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO requestDTO);
    void deleteAppointment(Long id);
}
