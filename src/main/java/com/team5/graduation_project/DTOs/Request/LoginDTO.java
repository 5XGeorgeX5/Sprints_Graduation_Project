package com.team5.graduation_project.DTOs.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
