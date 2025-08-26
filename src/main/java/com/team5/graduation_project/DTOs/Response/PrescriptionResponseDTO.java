package com.team5.graduation_project.DTOs.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponseDTO {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private Long medicineId;
    private String dosage;
    private String instruction;
}