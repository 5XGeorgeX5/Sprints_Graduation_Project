package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Message;
import com.team5.graduation_project.Models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Account patient;
    private Account doctor;

    @BeforeEach
    void setUp() {
        // Create accounts with required fields
        patient = new Account();
        patient.setUsername("alice");
        patient.setEmail("alice@example.com");  // required
        patient.setPassword("secret");          // required
        patient.setRole(Role.PATIENT);
        patient = accountRepository.save(patient);

        doctor = new Account();
        doctor.setUsername("bob");
        doctor.setEmail("bob@example.com");     // required
        doctor.setPassword("secret");           // required
        doctor.setRole(Role.DOCTOR);
        doctor = accountRepository.save(doctor);

        // Create messages
        Message m1 = Message.builder()
                .sender(patient)
                .receiver(doctor)
                .content("Hello Doctor")
                .build();

        Message m2 = Message.builder()
                .sender(doctor)
                .receiver(patient)
                .content("Hello Patient")
                .build();

        messageRepository.save(m1);
        messageRepository.save(m2);
    }


    @Test
    void findChatBetweenUsers_ShouldReturnOrderedConversation() {
        List<Message> messages = messageRepository.findChatBetweenUsers(patient.getId(), doctor.getId());

        assertThat(messages).hasSize(2);
        assertThat(messages.get(0).getContent()).isEqualTo("Hello Doctor");
        assertThat(messages.get(1).getContent()).isEqualTo("Hello Patient");
    }

    @Test
    void findChatBetweenUsers_NoMessages_ShouldReturnEmptyList() {
        List<Message> messages = messageRepository.findChatBetweenUsers(patient.getId(), 999L);
        assertThat(messages).isEmpty();
    }
}
