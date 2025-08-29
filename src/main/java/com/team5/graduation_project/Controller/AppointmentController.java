package com.team5.graduation_project.Controller;
import com.team5.graduation_project.DTOs.Request.AppointmentRequestDTO;
import com.team5.graduation_project.DTOs.Response.AppointmentResponseDTO;
import com.team5.graduation_project.Service.Appointment.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    @PostMapping("/patient/{patientId}")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @PathVariable Long patientId,
            @Valid @RequestBody AppointmentRequestDTO requestDTO) {
        AppointmentResponseDTO responseDTO = appointmentService.createAppointment(patientId, requestDTO);
        return ResponseEntity.status(201).body(responseDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable Long id) {
        AppointmentResponseDTO responseDTO = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(responseDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDTO requestDTO) {
        AppointmentResponseDTO responseDTO = appointmentService.updateAppointment(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

}