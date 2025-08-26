package com.team5.graduation_project.DTOs.Response;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponseDTO {
    private AccountResponseDTO account;
    private LocalTime startShift;
    private LocalTime endShift;
    private Integer appointmentDuration;
}
