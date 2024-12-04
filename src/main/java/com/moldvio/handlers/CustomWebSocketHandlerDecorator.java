package com.moldvio.handlers;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import java.util.concurrent.atomic.AtomicInteger;

public class CustomWebSocketHandlerDecorator extends WebSocketHandlerDecorator {

    private static final AtomicInteger subscriberCount = new AtomicInteger(0); // Numărătoare atomică pentru abonați

    public CustomWebSocketHandlerDecorator(WebSocketHandler delegate) {
        super(delegate);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        // Crește contorul când un client se conectează
        subscriberCount.incrementAndGet();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        // Scade contorul când un client se deconectează
        subscriberCount.decrementAndGet();
    }

    public static int getSubscriberCount() {
        return subscriberCount.get();
    }
}
