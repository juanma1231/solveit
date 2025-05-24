package co.edu.uco.solveit.chat.service;

import co.edu.uco.solveit.chat.entity.ChatMessageEntity;
import co.edu.uco.solveit.chat.mapper.ChatMessageMapper;
import co.edu.uco.solveit.chat.model.ChatMessage;
import co.edu.uco.solveit.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for handling chat messages
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final UserStatusService userStatusService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Process a chat message
     * If the recipient is connected, send the message directly
     * If the recipient is not connected, store the message for later delivery
     * @param chatMessage the chat message to process
     */
    @Transactional
    public void processMessage(ChatMessage chatMessage) {
        log.debug("Processing message from {} to {}", chatMessage.getSender(), chatMessage.getRecipient());
        
        // Save the message to the database
        ChatMessageEntity entity = chatMessageMapper.toEntity(chatMessage);
        
        // Check if recipient is connected
        boolean recipientConnected = userStatusService.isConnected(chatMessage.getRecipient());
        
        // If recipient is connected, mark as delivered
        if (recipientConnected) {
            entity.setDelivered(true);
            // Send the message directly
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getRecipient(),
                    "/queue/messages",
                    chatMessage
            );
        }
        
        // Save the message (delivered or not)
        chatMessageRepository.save(entity);
    }

    /**
     * Deliver any undelivered messages to a user
     * @param username the username of the user
     */
    @Transactional
    public void deliverPendingMessages(String username) {
        log.debug("Delivering pending messages to {}", username);
        
        // Find all undelivered messages for this user
        List<ChatMessageEntity> undeliveredMessages = 
                chatMessageRepository.findByRecipientAndDeliveredOrderByTimestampAsc(username, false);
        
        if (!undeliveredMessages.isEmpty()) {
            log.debug("Found {} undelivered messages for {}", undeliveredMessages.size(), username);
            
            // Convert entities to models
            List<ChatMessage> messages = chatMessageMapper.toModelList(undeliveredMessages);
            
            // Send each message to the user
            for (ChatMessage message : messages) {
                messagingTemplate.convertAndSendToUser(
                        username,
                        "/queue/messages",
                        message
                );
            }
            
            // Mark all messages as delivered
            undeliveredMessages.forEach(message -> message.setDelivered(true));
            chatMessageRepository.saveAll(undeliveredMessages);
        }
    }

    /**
     * Get the chat history between two users
     * @param user1 the first user
     * @param user2 the second user
     * @return the list of messages between the users
     */
    public List<ChatMessage> getChatHistory(String user1, String user2) {
        List<ChatMessageEntity> entities = chatMessageRepository
                .findBySenderAndRecipientOrSenderAndRecipientOrderByTimestampAsc(
                        user1, user2, user2, user1);
        
        return chatMessageMapper.toModelList(entities);
    }
}