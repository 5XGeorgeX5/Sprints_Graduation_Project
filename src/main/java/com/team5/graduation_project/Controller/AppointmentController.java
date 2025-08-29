package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.AppointmentRequestDTO;
import com.team5.graduation_project.DTOs.Response.AppointmentResponseDTO;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Service.Appointment.IAppointmentService;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.Appointment.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;




    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @AuthenticationPrincipal Account patientAccount,
            @Valid @RequestBody AppointmentRequestDTO requestDTO) {
        AppointmentResponseDTO responseDTO = appointmentService.createAppointment(patientAccount.getId(), requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable Long id) {
        AppointmentResponseDTO responseDTO = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDTO requestDTO) {
        AppointmentResponseDTO responseDTO = appointmentService.updateAppointment(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{appointmentId}/add-follow-up")
    public ResponseEntity<BaseResponse> addFollowUpNote(
            @PathVariable Long appointmentId,
            @RequestBody String note) {
        appointmentService.addFollowupNotes(appointmentId, note);
        return ResponseEntity.ok(
                new BaseResponse("Follow-up note added successfully", null)
        );
    }


}