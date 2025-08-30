package com.team5.graduation_project.DTOs.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "HH:mm")
    @NotNull
    private LocalTime startShift;

    @JsonFormat(pattern = "HH:mm")
    @NotNull
    private LocalTime endShift;

    @Min(1)
    private Integer appointmentDuration = 30;
}
