package com.team5.graduation_project.DTOs.Response;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDTO {

    private Long id;
    private String name;
    private String email;
    private List<AppointmentResponseDTO> appointments;
}
