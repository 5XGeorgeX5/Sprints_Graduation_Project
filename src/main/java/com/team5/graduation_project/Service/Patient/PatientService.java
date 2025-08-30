package com.team5.graduation_project.Service.Patient;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.PrescriptionResponseDTO;
import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.*;
import com.team5.graduation_project.Repository.AppointmentRepository;
import com.team5.graduation_project.Repository.PatientRepository;
import com.team5.graduation_project.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PatientService implements IPatientService {

    private final AccountService accountService;
    private final PatientRepository patientRepository;
    private final DtoMapper mapper;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public AccountResponseDTO register(AccountRegistrationRequestDTO dto) {
        Account account = accountService.createAccount(dto, Role.PATIENT);

        Patient patient = new Patient();
        patient.setAccount(account);

        patientRepository.save(patient);

        return mapper.toAccountResponseDTO(account);
    }

    @Override
    public List<AccountResponseDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patient -> mapper.toAccountResponseDTO(patient.getAccount()))
                .toList();
    }

    @Override
    public AccountResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Patient not found"));

        return mapper.toAccountResponseDTO(patient.getAccount());
    }

    @Override
    public AccountResponseDTO updatePatient(Long id, AccountRegistrationRequestDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Patient not found"));

        Account updatedAccount = accountService.updateAccount(patient.getAccount().getId(), dto);

        return mapper.toAccountResponseDTO(updatedAccount);
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Patient not found"));

        patientRepository.deleteById(id);

        accountService.deleteAccount(patient.getAccount().getId());
    }

    @Override
    public List<PrescriptionResponseDTO> getPatientMedicalHistory(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Patient not found"));
        List<Prescription> prescriptions = patient.getPrescriptions();
        List<PrescriptionResponseDTO> dtos = new ArrayList<>();
        for (Prescription prescription : prescriptions) {
            dtos.add(mapper.toPrescriptionResponseDTO(prescription));
        }
        return dtos;
    }

    @Override
    public List<List<String>> getPatientPreviousConsultations(Long id) {
        List<Appointment> patientAppointments = appointmentRepository.findByPatientId(id);
        List<List<String>> consultations = new ArrayList<>();
        for (Appointment appointment : patientAppointments) {
            List<String> notes = appointment.getFollowUpNotes() != null
                    ? appointment.getFollowUpNotes()
                    : new ArrayList<>();
            consultations.add(notes);
        }
        return consultations;
    }

}
