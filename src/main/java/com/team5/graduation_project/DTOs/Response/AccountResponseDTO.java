package com.team5.graduation_project.DTOs.Response;

import com.team5.graduation_project.Models.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private Role role;
}