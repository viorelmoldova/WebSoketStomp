package com.moldvio.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate template;

//    @Scheduled(fixedRate = 5000)
//    public void sendPeriodicMessages() {
//        template.convertAndSend("/topic/messages", "Periodic message from server");
//    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message, StompHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        return sessionId + " : " + message + "!";
    }

//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public String greeting(String message, @AuthenticationPrincipal User user, StompHeaderAccessor headerAccessor) {
//        String sessionId = headerAccessor.getSessionId();
//        String username = user != null ? user.getUsername() : "Anonymous";  // Obține username-ul dacă există
//        return "User " + username + " with session ID " + sessionId + " says: " + message;
//    }
}
