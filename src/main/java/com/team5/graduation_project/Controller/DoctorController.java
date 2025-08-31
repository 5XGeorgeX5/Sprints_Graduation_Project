package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.DoctorResponseDTO;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.IDoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("api/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final IDoctorService doctorService;

    @GetMapping("/all")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<BaseResponse> getAllRegisteredDoctors() {
        List<DoctorResponseDTO> doctors = doctorService.getAllRegisteredDoctors();
        return ResponseEntity.ok(new BaseResponse("Registered doctors retrieved successfully", doctors));
    }

    @PutMapping("/update/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<BaseResponse> updateDoctor(@PathVariable Long doctorId,
                                                     @RequestBody @Valid AccountRegistrationRequestDTO dto) {
        AccountResponseDTO updatedDoctor = doctorService.update(doctorId, dto);
        return ResponseEntity.ok(new BaseResponse("Doctor updated successfully", updatedDoctor));
    }

    @DeleteMapping("/delete/{doctorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> deleteDoctor(@PathVariable Long doctorId) {
        doctorService.delete(doctorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<BaseResponse> getAvailableDoctors(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<DoctorResponseDTO> doctors = doctorService.getAvailableDoctors(date);
        return ResponseEntity.ok(new BaseResponse("Available doctors retrieved successfully", doctors));
    }

    @GetMapping("/available-slots/{doctorId}")
    public ResponseEntity<BaseResponse> getDoctorAvailableSlots(@PathVariable Long doctorId,
                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LocalTime> availableSlots = doctorService.getDoctorAvailableSlots(doctorId, date);
        return ResponseEntity.ok(new BaseResponse("Doctor available slots retrieved successfully", availableSlots));
    }


}
