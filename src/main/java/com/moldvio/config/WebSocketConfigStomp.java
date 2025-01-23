package com.moldvio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfigStomp implements WebSocketMessageBrokerConfigurer {

    @Value("#{'${cors.allowed.origins}'.split(',')}")
    private String[] corsAllowedOrigins;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Aici se vor trimite mesaje de la server către client
        config.setApplicationDestinationPrefixes("/app"); // Prefixul pentru mesajele care vor fi procesate de server
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Configurarea endpoint-ului pentru STOMP
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://192.168.1.13:8081", "http://10.0.2.2:8081","http://127.0.0.1:5500","http://localhost", "http://192.168.1.13")
                .withSockJS(); // Permite fallback folosind SockJS
    }


}