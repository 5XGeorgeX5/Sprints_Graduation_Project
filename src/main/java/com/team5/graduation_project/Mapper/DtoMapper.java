package com.team5.graduation_project.Mapper;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.DoctorResponseDTO;
import com.team5.graduation_project.DTOs.Response.PharmacyResponseDTO;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Doctor;
import com.team5.graduation_project.Models.Pharmacy;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    private final ModelMapper modelMapper;

    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Account toAccountEntity(AccountRegistrationRequestDTO dto) {
        return modelMapper.map(dto, Account.class);
    }

    public AccountResponseDTO toAccountResponseDTO(Account account) {
        return modelMapper.map(account, AccountResponseDTO.class);
    }

    public DoctorResponseDTO toDoctorResponseDTO(Doctor doctor) {
        return modelMapper.map(doctor, DoctorResponseDTO.class);
    }

    public PharmacyResponseDTO toPharmacyResponseDTO(Pharmacy pharmacy) {
        return modelMapper.map(pharmacy, PharmacyResponseDTO.class);
    }
}
