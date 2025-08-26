package com.team5.graduation_project.DTOs.Response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineStockResponseDTO {
    private Long id;
    private Long medicineId;
    private Long pharmacyId;
    private BigDecimal price;
    private Integer quantity;
}