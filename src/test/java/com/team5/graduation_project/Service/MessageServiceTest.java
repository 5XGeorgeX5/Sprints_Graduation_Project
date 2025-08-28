package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.MessageRequestDTO;
import com.team5.graduation_project.DTOs.Response.MessageResponseDTO;
import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Message;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.AccountRepository;
import com.team5.graduation_project.Repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private MessageService messageService;

    private Account patient;
    private Account doctor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = Account.builder().id(1L).username("patient").role(Role.PATIENT).build();
        doctor = Account.builder().id(2L).username("doctor").role(Role.DOCTOR).build();

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(patient.getUsername(), null)
        );
    }

    @Test
    void createMessage_success() {
        MessageRequestDTO request = new MessageRequestDTO();
        request.setReceiverId(doctor.getId());
        request.setContent("Hello Doctor!");

        when(accountRepository.findByUsername(patient.getUsername())).thenReturn(Optional.of(patient));
        when(accountRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        Message saved = Message.builder()
                .id(100L).sender(patient).receiver(doctor).content("Hello Doctor!").build();

        when(messageRepository.save(any(Message.class))).thenReturn(saved);

        MessageResponseDTO response = messageService.createMessage(request);

        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getContent()).isEqualTo("Hello Doctor!");
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void createMessage_senderNotFound() {
        when(accountRepository.findByUsername("patient")).thenReturn(Optional.empty());

        MessageRequestDTO request = new MessageRequestDTO();
        request.setReceiverId(doctor.getId());
        request.setContent("hi");

        assertThatThrownBy(() -> messageService.createMessage(request))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("Sender not found");
    }

    @Test
    void getChatBetweenPatientAndDoctor_success() {
        when(accountRepository.findByUsername(patient.getUsername())).thenReturn(Optional.of(patient));
        when(accountRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        Message m1 = Message.builder().id(1L).sender(patient).receiver(doctor).content("hi").build();
        Message m2 = Message.builder().id(2L).sender(doctor).receiver(patient).content("hello").build();

        when(messageRepository.findChatBetweenUsers(patient.getId(), doctor.getId()))
                .thenReturn(List.of(m1, m2));

        List<MessageResponseDTO> chat = messageService.getChatBetweenPatientAndDoctor(doctor.getId());

        assertThat(chat).hasSize(2);
        assertThat(chat.get(0).getContent()).isEqualTo("hi");
        assertThat(chat.get(1).getContent()).isEqualTo("hello");
    }
}
