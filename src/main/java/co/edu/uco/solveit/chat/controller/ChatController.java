package co.edu.uco.solveit.chat.controller;

import co.edu.uco.solveit.chat.model.ChatMessage;
import co.edu.uco.solveit.chat.model.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {

        if (chatMessage.getTimestamp() == null) {
            chatMessage.setTimestamp(LocalDateTime.now());
        }

        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient(),
                "/queue/messages",
                chatMessage
        );
    }

    @MessageMapping("/chat.register")
    public void register(@Payload ChatMessage chatMessage, 
                         SimpMessageHeaderAccessor headerAccessor) {


        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        if (chatMessage.getTimestamp() == null) {
            chatMessage.setTimestamp(LocalDateTime.now());
        }

        messagingTemplate.convertAndSendToUser(
                chatMessage.getSender(),
                "/queue/messages",
                ChatMessage.builder()
                        .type(MessageType.JOIN)
                        .sender("system")
                        .recipient(chatMessage.getSender())
                        .content("Connected successfully")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
