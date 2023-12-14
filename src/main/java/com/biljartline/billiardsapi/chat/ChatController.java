package com.biljartline.billiardsapi.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    @MessageMapping("/send")    // URI endpoint
    @SendTo("/topic/chat")   // Destination
    public ChatMessage sendMessage(@Payload ChatMessage message) {
        return message;
    }
}
