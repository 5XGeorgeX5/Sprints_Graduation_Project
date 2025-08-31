package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") // ensures H2 configuration in application-test.properties
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Doctor doctor;
    private Patient patient1;
    private Appointment appointment1;
    private Appointment appointment2;

    @BeforeEach
    void setUp() {
        // Create doctor account
        Account doctorAccount = Account.builder()
                .username("doctor")
                .email("doctor@test.com")
                .password("password")
                .role(Role.DOCTOR)
                .build();
        doctorAccount = accountRepository.save(doctorAccount);

        // Create doctor
        doctor = Doctor.builder()
                .account(doctorAccount)
                .startShift(LocalTime.of(9, 0))
                .endShift(LocalTime.of(17, 0))
                .appointmentDuration(30)
                .build();
        doctor = doctorRepository.save(doctor);

        // Create patient accounts
        Account patientAccount1 = Account.builder()
                .username("patient1")
                .email("patient1@test.com")
                .password("password")
                .role(Role.PATIENT)
                .build();
        patientAccount1 = accountRepository.save(patientAccount1);

        Account patientAccount2 = Account.builder()
                .username("patient2")
                .email("patient2@test.com")
                .password("password")
                .role(Role.PATIENT)
                .build();
        patientAccount2 = accountRepository.save(patientAccount2);

        // Create patients
        patient1 = Patient.builder().account(patientAccount1).build();
        patient1 = patientRepository.save(patient1);

        Patient patient2 = Patient.builder().account(patientAccount2).build();
        patient2 = patientRepository.save(patient2);

        // Create appointments
        appointment1 = Appointment.builder()
                .doctor(doctor)
                .patient(patient1)
                .appointmentTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0))
                .build();
        appointmentRepository.save(appointment1);

        appointment2 = Appointment.builder()
                .doctor(doctor)
                .patient(patient2)
                .appointmentTime(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0).withSecond(0).withNano(0))
                .build();
        appointmentRepository.save(appointment2);
    }

    @Test
    void testGetBookedSlots() {
        LocalDateTime startOfDay = appointment1.getAppointmentTime().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<LocalDateTime> slots = appointmentRepository.getBookedSlots(doctor.getId(), startOfDay, endOfDay);

        assertThat(slots).containsExactly(
                appointment1.getAppointmentTime(),
                appointment2.getAppointmentTime()
        );
    }

    @Test
    void testFindByPatientId() {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patient1.getId());
        assertThat(appointments).containsExactly(appointment1);
    }

    @Test
    void testFindByDoctorId() {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctor.getId());
        assertThat(appointments).containsExactlyInAnyOrder(appointment1, appointment2);
    }

    @Test
    void testFindByDoctorIdAndAppointmentTimeBetween() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(9).withMinute(30);
        LocalDateTime end = LocalDateTime.now().plusDays(1).withHour(10).withMinute(30);

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctor.getId(), start, end
        );

        assertThat(appointments).containsExactly(appointment1);
    }

    @Test
    void testFindConflictingAppointments() {
        LocalDateTime requestedStart = LocalDateTime.now().plusDays(1).withHour(10).withMinute(15);
        LocalDateTime requestedEnd = LocalDateTime.now().plusDays(1).withHour(10).withMinute(45);

        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(
                doctor.getId(), requestedStart, requestedEnd
        );

        assertThat(conflicts).containsExactly(appointment1);
    }
}
