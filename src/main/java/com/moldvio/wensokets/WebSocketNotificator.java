package com.moldvio.wensokets;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class WebSocketNotificator {

    @Autowired
    private SimpMessagingTemplate websocketTemplate;

    public void sendMessage(String topicName, String message) {
        sendMessage(topicName, message, 0l);
    }

    public void sendMessage(String topicName, String message, Long delay) {
        if (delay != null && delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
        try {
            websocketTemplate.convertAndSend(topicName, message);
            log.debug(">>>Web socket message sent on topic {}, message {}", topicName, message);
        } catch (MessagingException e) {
            log.warn("Unable to send websocket message on topic {}", topicName, e);
        }
    }
}
