package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AccountRegistrationRequestDTO;
import com.team5.graduation_project.DTOs.Request.DoctorCreateDTO;
import com.team5.graduation_project.DTOs.Response.AccountResponseDTO;
import com.team5.graduation_project.DTOs.Response.DoctorResponseDTO;
import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Doctor;
import com.team5.graduation_project.Models.Patient;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.AppointmentRepository;
import com.team5.graduation_project.Repository.DoctorRepository;
import com.team5.graduation_project.Repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService implements IDoctorService {

    private final AccountService accountService;
    private final DoctorRepository doctorRepository;
    private final DtoMapper mapper;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    @Override
    public DoctorResponseDTO register(DoctorCreateDTO dto) {
        Account account = accountService.createAccount(dto.getAccount(), Role.DOCTOR);

        Doctor doctor = new Doctor();
        doctor.setAccount(account);
        doctor.setStartShift(dto.getStartShift());
        doctor.setEndShift(dto.getEndShift());
        doctor.setAppointmentDuration(dto.getAppointmentDuration());

        doctorRepository.save(doctor);

        return mapper.toDoctorResponseDTO(doctor);
    }

    @Override
    public List<DoctorResponseDTO> getAllRegisteredDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorResponseDTO> dtos = new ArrayList<>();
        for (Doctor doctor : doctors) {
            dtos.add(mapper.toDoctorResponseDTO(doctor));
        }
        return dtos;
    }

    @Override
    public AccountResponseDTO update(Long id, AccountRegistrationRequestDTO dto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Doctor doesn't exist"));

        Account updatedAccount = accountService.updateAccount(doctor.getAccount().getId(), dto);

        return mapper.toAccountResponseDTO(updatedAccount);
    }

    @Override
    public void delete(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Doctor doesn't exist"));
        doctorRepository.delete(doctor);

        accountService.deleteAccount(doctor.getAccount().getId());
    }

    @Override
    public List<DoctorResponseDTO> getAvailableDoctors(LocalDate date) {
        List<Doctor> doctors = doctorRepository.findAvailableDoctors(date);
        List<DoctorResponseDTO> dtos = new ArrayList<>();
        for (Doctor doctor : doctors) {
            dtos.add(mapper.toDoctorResponseDTO(doctor));
        }
        return dtos;
    }



    @Override
    public List<LocalTime> getDoctorAvailableSlots(Long id, LocalDate date) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Doctor Not Found"));


        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();


        List<LocalDateTime> bookedDateTimes =
                appointmentRepository.getBookedSlots(id, startOfDay, endOfDay);


        List<LocalTime> bookedSlots = bookedDateTimes.stream()
                .map(LocalDateTime::toLocalTime)
                .toList();


        List<LocalTime> allSlots = generateTimeSlots(
                doctor.getStartShift(),
                doctor.getEndShift(),
                doctor.getAppointmentDuration()
        );


        allSlots.removeAll(bookedSlots);

        return allSlots;
    }


    @Override
    public List<Patient> getDoctorPatients(Long id) {
        return patientRepository.findPatientsByDoctorId(id);
    }


    private List<LocalTime> generateTimeSlots(LocalTime startTime, LocalTime endTime,
                                              int slotDurationMinutes) {
        List<LocalTime> slots = new ArrayList<>();

        int totalMinutes = (int) Duration.between(startTime, endTime).toMinutes();

        int numberOfSlots = totalMinutes / slotDurationMinutes;

        LocalTime currentTime = startTime;
        for (int i = 0; i < numberOfSlots; i++) {
            slots.add(currentTime);
            currentTime = currentTime.plusMinutes(slotDurationMinutes);
        }

        return slots;
    }


}
