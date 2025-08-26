package com.team5.graduation_project.DTOs.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineStockRequestDTO {

    @NotNull
    private Long medicineId;

    @NotNull
    private BigDecimal price;

    @Min(0)
    private Integer quantity;
}