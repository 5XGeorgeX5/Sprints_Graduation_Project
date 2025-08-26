package com.team5.graduation_project.DTOs.Response;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineResponseDTO {
    private Long id;
    private String name;
    private String description;
}