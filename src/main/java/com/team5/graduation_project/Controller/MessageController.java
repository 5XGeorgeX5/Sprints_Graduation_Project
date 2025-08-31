package com.team5.graduation_project.Controller;

import com.team5.graduation_project.DTOs.Request.MessageRequestDTO;
import com.team5.graduation_project.DTOs.Response.MessageResponseDTO;
import com.team5.graduation_project.Response.BaseResponse;
import com.team5.graduation_project.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<BaseResponse> getChatForPatient(
            @PathVariable Long doctorId,
            Authentication authentication) {
        List<MessageResponseDTO> messages = messageService.getChatBetweenPatientAndDoctor(doctorId);
        return ResponseEntity.ok(new BaseResponse("messages retrieved", messages));
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<BaseResponse> sendMessageToDoctor(
            @PathVariable Long doctorId,
            @RequestBody MessageRequestDTO messageRequest,
            Authentication authentication) {
        messageRequest.setReceiverId(doctorId);
        MessageResponseDTO response = messageService.createMessage(messageRequest);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(new BaseResponse("Sent a message", response));
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<BaseResponse> getChatForDoctor(
            @PathVariable Long patientId,
            Authentication authentication) {
        List<MessageResponseDTO> messages = messageService.getChatBetweenPatientAndDoctor(patientId);
        return ResponseEntity.ok(new BaseResponse("messages retrieved", messages));
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/patient/{patientId}")
    public ResponseEntity<BaseResponse> sendMessageToPatient(
            @PathVariable Long patientId,
            @RequestBody MessageRequestDTO messageRequest,
            Authentication authentication) {
        messageRequest.setReceiverId(patientId);
        MessageResponseDTO response = messageService.createMessage(messageRequest);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(new BaseResponse("Sent a message", response));
    }
}
