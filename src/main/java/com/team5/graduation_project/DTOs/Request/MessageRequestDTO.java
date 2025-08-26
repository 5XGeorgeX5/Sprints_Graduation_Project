package com.team5.graduation_project.DTOs.Request;

import com.team5.graduation_project.Models.Sender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequestDTO {

    @NotNull
    private Long doctorId;

    @NotNull
    private Long patientId;

    @NotBlank
    private String content;

    @NotNull
    private Sender sender;
}