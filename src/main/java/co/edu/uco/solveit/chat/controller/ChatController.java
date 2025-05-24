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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

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
                        .content("Connected successfully")
                        .timestamp(LocalDateTime.now())
                        .build()
        );

        chatMessageService.deliverHistoricalMessages(chatMessage.getSender());
    }

    @GetMapping("/api/chat/history/{userIdSender}/{userIdRecipient}")
    @ResponseBody
    public List<ChatMessage> getChatHistory(@PathVariable String userIdSender, @PathVariable String userIdRecipient) {
        return chatMessageService.getChatHistory(userIdSender, userIdRecipient);
    }
}
