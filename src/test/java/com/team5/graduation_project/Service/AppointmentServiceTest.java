package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.AppointmentRequestDTO;
import com.team5.graduation_project.DTOs.Response.AppointmentResponseDTO;
import com.team5.graduation_project.Mapper.DtoMapper;
import com.team5.graduation_project.Models.Appointment;
import com.team5.graduation_project.Models.Doctor;
import com.team5.graduation_project.Models.Patient;
import com.team5.graduation_project.Repository.AppointmentRepository;
import com.team5.graduation_project.Repository.DoctorRepository;
import com.team5.graduation_project.Repository.PatientRepository;
import com.team5.graduation_project.Service.Appointment.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DtoMapper mapper;

    @InjectMocks
    private AppointmentService appointmentService;

    private Patient patient;
    private Doctor doctor;
    @Mock
    private Appointment appointment;
    private AppointmentRequestDTO requestDTO;
    private AppointmentResponseDTO responseDTO;
    private LocalDateTime futureTime;

    @BeforeEach
    void setUp() {
        // Setup test data
        futureTime = LocalDateTime.now().plusDays(1);

        patient = new Patient();
        patient.setId(1L);

        doctor = new Doctor();
        doctor.setId(1L);

        // Mock appointment is already created by @Mock annotation

        requestDTO = new AppointmentRequestDTO();
        requestDTO.setDoctorId(1L);
        requestDTO.setAppointmentTime(futureTime);

        responseDTO = new AppointmentResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setPatientId(1L);
        responseDTO.setDoctorId(1L);
        responseDTO.setAppointmentTime(futureTime);
    }

    // ========== CREATE APPOINTMENT TESTS ==========

    @Test
    void createAppointment_Success() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(mapper.toAppointmentResponseDTO(appointment)).thenReturn(responseDTO);

        // Act
        AppointmentResponseDTO result = appointmentService.createAppointment(1L, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getPatientId());
        assertEquals(1L, result.getDoctorId());
        assertEquals(futureTime, result.getAppointmentTime());

        verify(patientRepository).findById(1L);
        verify(doctorRepository).findById(1L);
        verify(appointmentRepository).findByDoctorIdAndAppointmentTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(appointmentRepository).save(any(Appointment.class));
        verify(mapper).toAppointmentResponseDTO(appointment);
    }

    @Test
    void createAppointment_PatientNotFound() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.createAppointment(1L, requestDTO));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Patient not found", exception.getReason());

        verify(patientRepository).findById(1L);
        verifyNoInteractions(doctorRepository, appointmentRepository, mapper);
    }

    @Test
    void createAppointment_DoctorNotFound() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.createAppointment(1L, requestDTO));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Doctor not found", exception.getReason());

        verify(patientRepository).findById(1L);
        verify(doctorRepository).findById(1L);
        verifyNoInteractions(appointmentRepository, mapper);
    }

    @Test
    void createAppointment_NullAppointmentTime() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        requestDTO.setAppointmentTime(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.createAppointment(1L, requestDTO));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Appointment time must be in the future", exception.getReason());
    }

    @Test
    void createAppointment_PastAppointmentTime() {
        // Arrange
        LocalDateTime pastTime = LocalDateTime.now().minusHours(1);
        requestDTO.setAppointmentTime(pastTime);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.createAppointment(1L, requestDTO));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Appointment time must be in the future", exception.getReason());
    }

    @Test
    void createAppointment_DoctorNotAvailable() {
        // Arrange
        Appointment conflictingAppointment = new Appointment();
        conflictingAppointment.setId(2L);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(conflictingAppointment));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.createAppointment(1L, requestDTO));

        assertEquals(CONFLICT, exception.getStatusCode());
        assertEquals("Doctor is not available at the requested time", exception.getReason());

        verify(appointmentRepository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    // ========== GET APPOINTMENT TESTS ==========

    @Test
    void getAppointmentById_Success() {
        // Arrange
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(mapper.toAppointmentResponseDTO(appointment)).thenReturn(responseDTO);

        // Act
        AppointmentResponseDTO result = appointmentService.getAppointmentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getPatientId());
        assertEquals(1L, result.getDoctorId());
        assertEquals(futureTime, result.getAppointmentTime());

        verify(appointmentRepository).findById(1L);
        verify(mapper).toAppointmentResponseDTO(appointment);
    }

    @Test
    void getAppointmentById_NotFound() {
        // Arrange
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.getAppointmentById(1L));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Appointment not found", exception.getReason());

        verify(appointmentRepository).findById(1L);
        verifyNoInteractions(mapper);
    }

    // ========== UPDATE APPOINTMENT TESTS ==========

    @Test
    void updateAppointment_Success() {
        // Arrange
        LocalDateTime newTime = LocalDateTime.now().plusDays(2);
        requestDTO.setAppointmentTime(newTime);

        Appointment updatedAppointment = Appointment.builder()
                .id(1L)
                .patient(patient)
                .doctor(doctor)
                .appointmentTime(newTime)
                .build();

        AppointmentResponseDTO updatedResponseDTO = new AppointmentResponseDTO();
        updatedResponseDTO.setId(1L);
        updatedResponseDTO.setAppointmentTime(newTime);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.save(appointment)).thenReturn(updatedAppointment);
        when(mapper.toAppointmentResponseDTO(updatedAppointment)).thenReturn(updatedResponseDTO);

        // Act
        AppointmentResponseDTO result = appointmentService.updateAppointment(1L, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(newTime, result.getAppointmentTime());

        // Verify repository and mapper interactions
        verify(appointmentRepository).findById(1L);
        verify(doctorRepository).findById(1L);
        verify(appointmentRepository).findByDoctorIdAndAppointmentTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(appointmentRepository).save(appointment);
        verify(mapper).toAppointmentResponseDTO(updatedAppointment);

        // Verify that the mock appointment's setters were called
        verify(appointment).setDoctor(doctor);
        verify(appointment).setAppointmentTime(newTime);
    }

    @Test
    void updateAppointment_AppointmentNotFound() {
        // Arrange
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.updateAppointment(1L, requestDTO));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Appointment not found", exception.getReason());

        verify(appointmentRepository).findById(1L);
        verifyNoInteractions(doctorRepository, mapper);
    }

    @Test
    void updateAppointment_DoctorNotFound() {
        // Arrange
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.updateAppointment(1L, requestDTO));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Doctor not found", exception.getReason());

        verify(appointmentRepository).findById(1L);
        verify(doctorRepository).findById(1L);
    }

    @Test
    void updateAppointment_NullAppointmentTime() {
        // Arrange
        requestDTO.setAppointmentTime(null);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.updateAppointment(1L, requestDTO));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Appointment time must be in the future", exception.getReason());
    }

    @Test
    void updateAppointment_PastAppointmentTime() {
        // Arrange
        LocalDateTime pastTime = LocalDateTime.now().minusHours(1);
        requestDTO.setAppointmentTime(pastTime);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.updateAppointment(1L, requestDTO));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Appointment time must be in the future", exception.getReason());
    }

    @Test
    void updateAppointment_DoctorNotAvailable() {
        // Arrange
        Appointment conflictingAppointment = new Appointment();
        conflictingAppointment.setId(2L); // Different ID than the one being updated

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(conflictingAppointment));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.updateAppointment(1L, requestDTO));

        assertEquals(CONFLICT, exception.getStatusCode());
        assertEquals("Doctor is not available at the requested time", exception.getReason());

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void updateAppointment_SameAppointmentInTimeSlot_Success() {
        // Arrange - Testing that updating the same appointment doesn't conflict with itself
        Appointment sameAppointment = new Appointment();
        sameAppointment.setId(1L); // Same ID as the one being updated

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(sameAppointment)); // Same appointment found, should be filtered out
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(mapper.toAppointmentResponseDTO(appointment)).thenReturn(responseDTO);

        // Act
        AppointmentResponseDTO result = appointmentService.updateAppointment(1L, requestDTO);

        // Assert
        assertNotNull(result);
        verify(appointmentRepository).save(appointment);
    }

    // ========== DELETE APPOINTMENT TESTS ==========

    @Test
    void deleteAppointment_Success() {
        // Arrange
        when(appointmentRepository.existsById(1L)).thenReturn(true);

        // Act
        appointmentService.deleteAppointment(1L);

        // Assert
        verify(appointmentRepository).existsById(1L);
        verify(appointmentRepository).deleteById(1L);
    }

    @Test
    void deleteAppointment_AppointmentNotFound() {
        // Arrange
        when(appointmentRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.deleteAppointment(1L));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Appointment not found", exception.getReason());

        verify(appointmentRepository).existsById(1L);
        verify(appointmentRepository, never()).deleteById(any());
    }

    // ========== EDGE CASE TESTS ==========

    @Test
    void createAppointment_AppointmentTimeExactlyNow() {
        // Arrange - Test boundary condition where appointment time is exactly now
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        requestDTO.setAppointmentTime(now);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.createAppointment(1L, requestDTO));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Appointment time must be in the future", exception.getReason());
    }

    @Test
    void createAppointment_MultipleConflictingAppointments() {
        // Arrange
        Appointment conflict1 = new Appointment();
        conflict1.setId(2L);
        Appointment conflict2 = new Appointment();
        conflict2.setId(3L);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(conflict1, conflict2));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.createAppointment(1L, requestDTO));

        assertEquals(CONFLICT, exception.getStatusCode());
        assertEquals("Doctor is not available at the requested time", exception.getReason());
    }

    @Test
    void updateAppointment_ChangeDoctorAndTime() {
        // Arrange
        Doctor newDoctor = new Doctor();
        newDoctor.setId(2L);

        LocalDateTime newTime = LocalDateTime.now().plusDays(3);
        requestDTO.setDoctorId(2L);
        requestDTO.setAppointmentTime(newTime);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(newDoctor));
        when(appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                eq(2L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(mapper.toAppointmentResponseDTO(appointment)).thenReturn(responseDTO);

        // Act
        AppointmentResponseDTO result = appointmentService.updateAppointment(1L, requestDTO);

        // Assert
        assertNotNull(result);

        // Verify that the mock appointment's setters were called with new values
        verify(appointment).setDoctor(newDoctor);
        verify(appointment).setAppointmentTime(newTime);
        verify(appointmentRepository).save(appointment);

        // Verify repository interactions
        verify(appointmentRepository).findById(1L);
        verify(doctorRepository).findById(2L);
        verify(appointmentRepository).findByDoctorIdAndAppointmentTimeBetween(
                eq(2L), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(mapper).toAppointmentResponseDTO(appointment);
    }

    // ========== VERIFICATION TESTS ==========

    @Test
    void createAppointment_VerifyTimeRangeCalculation() {
        // Arrange
        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(1).withHour(14).withMinute(30);
        requestDTO.setAppointmentTime(appointmentTime);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(mapper.toAppointmentResponseDTO(appointment)).thenReturn(responseDTO);

        // Act
        appointmentService.createAppointment(1L, requestDTO);

        // Assert - Verify the time range is calculated correctly (±30 minutes)
        LocalDateTime expectedStart = appointmentTime.minusMinutes(30);
        LocalDateTime expectedEnd = appointmentTime.plusMinutes(30);

        verify(appointmentRepository).findByDoctorIdAndAppointmentTimeBetween(
                1L, expectedStart, expectedEnd);
    }

    @Test
    void updateAppointment_VerifyFilteringLogic() {
        // Arrange
        Appointment sameAppointment = new Appointment();
        sameAppointment.setId(1L); // Same ID as being updated

        Appointment differentAppointment = new Appointment();
        differentAppointment.setId(2L); // Different ID

        List<Appointment> allAppointments = Arrays.asList(sameAppointment, differentAppointment);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(allAppointments);

        // Act & Assert - Should throw conflict because of the different appointment
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> appointmentService.updateAppointment(1L, requestDTO));

        assertEquals(CONFLICT, exception.getStatusCode());
        assertEquals("Doctor is not available at the requested time", exception.getReason());
    }
}