package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.MessageRequestDTO;
import com.team5.graduation_project.DTOs.Response.MessageResponseDTO;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private MessageController messageController;

    private MessageRequestDTO requestDTO;
    private MessageResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = MessageRequestDTO.builder()
                .receiverId(null) // will be set by controller
                .content("Hello doctor")
                .build();

        responseDTO = MessageResponseDTO.builder()
                .id(1L)
                .senderId(100L)
                .receiverId(200L)
                .content("Hello doctor")
                .build();
    }

    @Test
    void sendMessageToDoctor_ShouldReturnCreatedResponse() {
        Long doctorId = 200L;
        when(messageService.createMessage(any(MessageRequestDTO.class)))
                .thenReturn(responseDTO);

        ResponseEntity<BaseResponse> response = messageController.sendMessageToDoctor(doctorId, requestDTO, authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Sent a message", response.getBody().getMessage());
        assertEquals(responseDTO, response.getBody().getData());

        // verify that controller set receiverId
        assertEquals(doctorId, requestDTO.getReceiverId());
        verify(messageService).createMessage(requestDTO);
    }

    @Test
    void sendMessageToPatient_ShouldReturnCreatedResponse() {
        Long patientId = 300L;
        when(messageService.createMessage(any(MessageRequestDTO.class)))
                .thenReturn(responseDTO);

        ResponseEntity<BaseResponse> response = messageController.sendMessageToPatient(patientId, requestDTO, authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Sent a message", response.getBody().getMessage());
        assertEquals(responseDTO, response.getBody().getData());

        assertEquals(patientId, requestDTO.getReceiverId());
        verify(messageService).createMessage(requestDTO);
    }

    @Test
    void getChatForPatient_ShouldReturnMessages() {
        Long doctorId = 200L;
        List<MessageResponseDTO> messages = List.of(responseDTO);
        when(messageService.getChatBetweenPatientAndDoctor(doctorId)).thenReturn(messages);

        ResponseEntity<BaseResponse> response = messageController.getChatForPatient(doctorId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("messages retrieved", response.getBody().getMessage());
        assertEquals(messages, response.getBody().getData());

        verify(messageService).getChatBetweenPatientAndDoctor(doctorId);
    }

    @Test
    void getChatForDoctor_ShouldReturnMessages() {
        Long patientId = 300L;
        List<MessageResponseDTO> messages = List.of(responseDTO);
        when(messageService.getChatBetweenPatientAndDoctor(patientId)).thenReturn(messages);

        ResponseEntity<BaseResponse> response = messageController.getChatForDoctor(patientId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("messages retrieved", response.getBody().getMessage());
        assertEquals(messages, response.getBody().getData());

        verify(messageService).getChatBetweenPatientAndDoctor(patientId);
    }
}
