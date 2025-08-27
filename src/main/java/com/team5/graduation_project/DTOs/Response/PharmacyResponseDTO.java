package com.team5.graduation_project.DTOs.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyResponseDTO {
    private AccountResponseDTO account;
    private String address;
}
