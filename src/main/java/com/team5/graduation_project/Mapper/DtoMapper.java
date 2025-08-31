package com.team5.graduation_project.Mapper;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Request.MedicineOrderRequestDTO;
import com.team5.graduation_project.DTOs.Request.MedicineRequestDTO;
import com.team5.graduation_project.DTOs.Request.MedicineStockRequestDTO;
import com.team5.graduation_project.DTOs.Response.*;
import com.team5.graduation_project.Models.*;
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

    public PatientResponseDTO patientResponseDTO(Patient patient) {
        return modelMapper.map(patient, PatientResponseDTO.class);
    }

    public PharmacyResponseDTO toPharmacyResponseDTO(Pharmacy pharmacy) {
        return modelMapper.map(pharmacy, PharmacyResponseDTO.class);
    }

    public AppointmentResponseDTO toAppointmentResponseDTO(Appointment appointment) {
        return modelMapper.map(appointment, AppointmentResponseDTO.class);
    }

    // Medicine mappings
    public Medicine toMedicineEntity(MedicineRequestDTO dto) {
        return modelMapper.map(dto, Medicine.class);
    }

    public MedicineResponseDTO toMedicineResponseDTO(Medicine medicine) {
        return modelMapper.map(medicine, MedicineResponseDTO.class);
    }

    // Medicine Stock mappings
    public MedicineStock toMedicineStockEntity(MedicineStockRequestDTO dto, Medicine medicine, Pharmacy pharmacy) {
        MedicineStock stock = new MedicineStock();
        stock.setMedicine(medicine);
        stock.setPharmacy(pharmacy);
        stock.setPrice(dto.getPrice());
        stock.setQuantity(dto.getQuantity());
        return stock;
    }

    public MedicineStockResponseDTO toMedicineStockResponseDTO(MedicineStock stock) {
        return MedicineStockResponseDTO.builder()
                .id(stock.getId())
                .medicineId(stock.getMedicine().getId())
                .pharmacyId(stock.getPharmacy().getId())
                .price(stock.getPrice())
                .quantity(stock.getQuantity())
                .build();
    }

    // Medicine Order mappings
    public MedicineOrder toMedicineOrderEntity(MedicineOrderRequestDTO dto, Medicine medicine, Patient patient, Pharmacy pharmacy) {
        MedicineOrder order = new MedicineOrder();
        order.setMedicine(medicine);
        order.setPatient(patient);
        order.setPharmacy(pharmacy);
        order.setQuantity(dto.getQuantity());
        return order;
    }

    public MedicineOrderResponseDTO toMedicineOrderResponseDTO(MedicineOrder order) {
        return MedicineOrderResponseDTO.builder()
                .id(order.getId())
                .medicineId(order.getMedicine().getId())
                .patientId(order.getPatient().getId())
                .pharmacyId(order.getPharmacy().getId())
                .quantity(order.getQuantity())
                .build();
    }

    public PrescriptionResponseDTO toPrescriptionResponseDTO(Prescription prescription) {

        return PrescriptionResponseDTO.builder()
                .id(prescription.getId())
                .doctorId(prescription.getDoctor().getId())
                .patientId(prescription.getPatient().getId())
                .medicineId(prescription.getMedicine().getId())
                .dosage(prescription.getDosage())
                .instruction(prescription.getInstruction())
                .build();
    }
}