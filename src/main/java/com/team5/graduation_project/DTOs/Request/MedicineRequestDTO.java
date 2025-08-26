package com.team5.graduation_project.DTOs.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineRequestDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String description;
}