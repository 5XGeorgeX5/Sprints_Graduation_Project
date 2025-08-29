package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.PrescriptionRequestDTO;
import com.team5.graduation_project.DTOs.Response.PrescriptionResponseDTO;
import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Doctor;
import com.team5.graduation_project.Models.Medicine;
import com.team5.graduation_project.Models.Patient;
import com.team5.graduation_project.Models.Prescription;
import com.team5.graduation_project.Repository.DoctorRepository;
import com.team5.graduation_project.Repository.MedicineRepository;
import com.team5.graduation_project.Repository.PatientRepository;
import com.team5.graduation_project.Repository.PrescriptionRepository;
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
    private final DtoMapper mapper;

    @Transactional
    public PrescriptionResponseDTO createPrescription(Long doctorId, PrescriptionRequestDTO dto) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFound("Patient not found"));
        Medicine medicine = medicineRepository.findById(dto.getMedicineId())
                .orElseThrow(() -> new ResourceNotFound("Medicine not found"));



        Prescription prescription = new Prescription();
        prescription.setPatient(patient);
        prescription.setMedicine(medicine);
        prescription.setDoctor(doctor);
        prescription.setDosage(dto.getDosage());
        prescription.setInstruction(dto.getInstruction());

        prescriptionRepository.save(prescription);

        return mapper.toPrescriptionResponseDTO(prescription);
    }

    public PrescriptionResponseDTO getDoctorPresciprion(Long id){
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Prescription Not Found"));

        return mapper.toPrescriptionResponseDTO(prescription);
    }
}