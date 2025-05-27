package co.edu.uco.solveit.chat.controller;

import co.edu.uco.solveit.chat.model.ChatMessage;
import co.edu.uco.solveit.chat.model.MessageType;
import co.edu.uco.solveit.chat.service.ChatMessageService;
import co.edu.uco.solveit.chat.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final UserStatusService userStatusService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        log.debug("Received message from {} to {}", chatMessage.getSender(), chatMessage.getRecipient());

        if (chatMessage.getTimestamp() == null) {
            chatMessage.setTimestamp(LocalDateTime.now());
        }

        chatMessageService.processMessage(chatMessage);
    }

    @MessageMapping("/chat.register")
    public void register(@Payload ChatMessage chatMessage, 
                         SimpMessageHeaderAccessor headerAccessor) {
        log.debug("User registered: {}", chatMessage.getSender());

        if (headerAccessor != null && headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        }

        if (chatMessage.getTimestamp() == null) {
            chatMessage.setTimestamp(LocalDateTime.now());
        }

        userStatusService.connect(chatMessage.getSender());

        messagingTemplate.convertAndSendToUser(
                chatMessage.getSender(),
                "/queue/messages",
                ChatMessage.builder()
                        .type(MessageType.JOIN)
                        .sender("system")
                        .recipient(chatMessage.getSender())
                        .content("Conectado con exito a la sala de chat")
                        .timestamp(LocalDateTime.now())
                        .build()
        );

        chatMessageService.deliverHistoricalMessages(chatMessage.getSender());
    }
}
