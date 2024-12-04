package com.moldvio.config;

import com.moldvio.handlers.CustomWebSocketHandlerDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfigStomp implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    //                .setAllowedOriginPatterns("http://192.168.1.13:8099","http://10.0.2.2:8081", "http://10.0.2.16:8081") // permite originile pentru WebSocket
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket-endpoint");// Endpoint STOMP
//                .setAllowedOriginPatterns("*") // permite toate originile pentru WebSocket
//                .withSockJS();// Fallback SockJS
    }
    @Bean
    public WebSocketHandlerDecoratorFactory handlerDecoratorFactory() {
        return new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandlerDecorator decorate(WebSocketHandler handler) {
                return new CustomWebSocketHandlerDecorator(handler);
            }
        };
    }

}