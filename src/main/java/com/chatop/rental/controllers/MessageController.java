package com.chatop.rental.controllers;

import com.chatop.rental.configuration.CustomUserDetails;
import com.chatop.rental.dto.MessageDTO;
import com.chatop.rental.services.CustomUserDetailsService;
import com.chatop.rental.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createMessage(@RequestBody MessageDTO messageDTO,
                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        messageService.createMessage(messageDTO, userDetails);
        return ResponseEntity.ok(Map.of("message", "Message send with success"));
    }
}
