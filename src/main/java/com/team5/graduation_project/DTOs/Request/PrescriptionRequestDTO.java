package com.team5.graduation_project.DTOs.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionRequestDTO {

    @NotNull
    private Long patientId;

    @NotNull
    private Long medicineId;

    @NotBlank
    private String dosage;

    @NotBlank
    private String instruction;
}