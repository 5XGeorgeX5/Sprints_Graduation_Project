package com.team5.graduation_project.DTOs.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO {

    private String username;

    private String password;
}
