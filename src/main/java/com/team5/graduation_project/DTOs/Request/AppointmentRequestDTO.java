package com.team5.graduation_project.DTOs.Request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequestDTO {

    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @Future
    private LocalDateTime appointmentTime;
}