package com.team5.graduation_project.DTOs.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineOrderRequestDTO {

    @NotNull
    private Long medicineId;

    @NotNull
    private Long pharmacyId;

    @Min(1)
    private Integer quantity;
}