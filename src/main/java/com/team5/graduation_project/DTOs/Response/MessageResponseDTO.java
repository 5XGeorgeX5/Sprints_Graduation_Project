package com.team5.graduation_project.DTOs.Response;


import com.team5.graduation_project.Models.Sender;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDTO {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private String content;
    private Sender sender;
    private LocalDateTime timestamp;
}
