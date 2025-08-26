package com.team5.graduation_project.DTOs.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineOrderResponseDTO {
    private Long id;
    private Long medicineId;
    private Long patientId;
    private Long pharmacyId;
    private Integer quantity;
}