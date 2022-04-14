package com.victorfisyuk.messagesservice.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageController(MessageService messageService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    @GetMapping("/inbox")
    public List<MessageDTO> getInboxMessages(@AuthenticationPrincipal Jwt principal) {
        return messageMapper.convertToDTO(messageService.getInboxMessages(principal.getSubject()));
    }

    @GetMapping("/outbox")
    public List<MessageDTO> getOutboxMessages(@AuthenticationPrincipal Jwt principal) {
        return messageMapper.convertToDTO(messageService.getOutboxMessages(principal.getSubject()));
    }

    @PostMapping
    public MessageDTO sendMessage(@Valid @RequestBody MessageComposeDTO messageComposeDTO) {
        Message message = messageMapper.convertToModel(messageComposeDTO);
        return messageMapper.convertToDTO(messageService.sendMessage(message));
    }
}
