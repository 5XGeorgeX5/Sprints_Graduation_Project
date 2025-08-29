package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.PrescriptionRequestDTO;
import com.team5.graduation_project.DTOs.Response.PrescriptionResponseDTO;
import com.team5.graduation_project.Models.*;
import com.team5.graduation_project.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final MedicineRepository medicineRepository;

    @Transactional
    public PrescriptionResponseDTO createPrescription(Long doctorId, PrescriptionRequestDTO request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new EntityNotFoundException("Medicine not found"));

        Prescription prescription = Prescription.builder()
                .doctor(doctor)
                .patient(patient)
                .medicine(medicine)
                .dosage(request.getDosage())
                .instruction(request.getInstruction())
                .build();

        Prescription saved = prescriptionRepository.save(prescription);

        return PrescriptionResponseDTO.builder()
                .id(saved.getId())
                .doctorId(saved.getDoctor().getId())
                .patientId(saved.getPatient().getId())
                .medicineId(saved.getMedicine().getId())
                .dosage(saved.getDosage())
                .instruction(saved.getInstruction())
                .build();
    }
}

