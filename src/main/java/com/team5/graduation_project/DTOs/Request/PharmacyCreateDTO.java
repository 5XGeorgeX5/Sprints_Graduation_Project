package com.team5.graduation_project.DTOs.Request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyCreateDTO {
    @NotNull
    private AccountRegistrationRequestDTO account;

    private String address;
}
