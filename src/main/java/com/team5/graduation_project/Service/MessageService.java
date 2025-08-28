package com.team5.graduation_project.Service;

import com.team5.graduation_project.DTOs.Request.MessageRequestDTO;
import com.team5.graduation_project.DTOs.Response.MessageResponseDTO;
import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Models.Account;
import com.team5.graduation_project.Models.Message;
import com.team5.graduation_project.Models.Role;
import com.team5.graduation_project.Repository.AccountRepository;
import com.team5.graduation_project.Repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    public MessageResponseDTO createMessage(MessageRequestDTO messageRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Account sender = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFound("Sender not found"));

        Account receiver = accountRepository.findById(messageRequest.getReceiverId())
                .orElseThrow(() -> new ResourceNotFound("Receiver not found"));

        validateSenderReceiever(sender, receiver);

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(messageRequest.getContent())
                .build();

        Message savedMessage = messageRepository.save(message);
        return mapToResponseDTO(savedMessage);
    }

    public List<MessageResponseDTO> getChatBetweenPatientAndDoctor(Long receiverId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Account sender = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFound("Sender not found"));
        Account receiver = accountRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFound("Receiver not found"));

        validateSenderReceiever(sender, receiver);

        List<Message> messages = messageRepository.findChatBetweenUsers(sender.getId(), receiverId);
        return messages.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private void validateSenderReceiever(Account sender, Account receiver) {
        if (sender.getId().equals(receiver.getId()))
            throw new IllegalArgumentException("You can't message yourself");

        if (sender.getRole().equals(receiver.getRole()))
            throw new IllegalArgumentException("You can't message a person of the same role");

        if (sender.getRole().equals(Role.ADMIN) || sender.getRole().equals(Role.PHARMACY))
            throw new IllegalArgumentException("No messaging for admin or pharmacy");

        if (receiver.getRole().equals(Role.ADMIN) || receiver.getRole().equals(Role.PHARMACY))
            throw new IllegalArgumentException("No messaging to admin or pharmacy");
    }

    private MessageResponseDTO mapToResponseDTO(Message message) {
        return MessageResponseDTO.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .receiverId(message.getReceiver().getId())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
