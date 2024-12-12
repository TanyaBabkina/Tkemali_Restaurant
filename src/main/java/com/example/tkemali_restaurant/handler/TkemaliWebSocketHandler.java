package com.example.tkemali_restaurant.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
//import com.example.tkemali_restaurant.dto.RestaurantUpdateMessage;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class TkemaliWebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("New WebSocket connection established: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("WebSocket connection closed: {}", session.getId());
    }

//    public void broadcastUpdate(RestaurantUpdateMessage message) {
//        try {
//            String payload = objectMapper.writeValueAsString(message);
//            TextMessage textMessage = new TextMessage(payload);
//
//            sessions.forEach(session -> {
//                try {
//                    if (session.isOpen()) {
//                        session.sendMessage(textMessage);
//                    }
//                } catch (IOException e) {
//                    log.error("Error sending message to session {}: {}", session.getId(), e.getMessage());
//                }
//            });
//        } catch (IOException e) {
//            log.error("Error serializing message: {}", e.getMessage());
//        }
//    }
}