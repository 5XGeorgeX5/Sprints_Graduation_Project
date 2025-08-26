package com.team5.graduation_project.DTOs.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorCreateDTO {
    @NotNull
    private AccountRegistrationRequestDTO account;

    private LocalTime startShift;
    private LocalTime endShift;

    @Min(1)
    private Integer appointmentDuration;
}
