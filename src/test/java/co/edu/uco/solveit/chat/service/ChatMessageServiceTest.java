package co.edu.uco.solveit.chat.service;

import co.edu.uco.solveit.chat.entity.ChatMessageEntity;
import co.edu.uco.solveit.chat.mapper.ChatMessageMapper;
import co.edu.uco.solveit.chat.model.ChatMessage;
import co.edu.uco.solveit.chat.model.MessageType;
import co.edu.uco.solveit.chat.repository.ChatMessageRepository;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatMessageMapper chatMessageMapper;

    @Mock
    private UserStatusService userStatusService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatMessageService chatMessageService;

    private ChatMessage chatMessage;
    private ChatMessageEntity chatMessageEntity;
    private List<ChatMessageEntity> historicalMessages;
    private List<ChatMessage> mappedMessages;

    @BeforeEach
    void setUp() {
        chatMessage = ChatMessage.builder()
                .type(MessageType.CHAT)
                .sender("sender")
                .recipient("recipient")
                .content("Hello, world!")
                .timestamp(LocalDateTime.now())
                .build();

        chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.setId(1L);
        chatMessageEntity.setSender("sender");
        chatMessageEntity.setRecipient("recipient");
        chatMessageEntity.setContent("Hello, world!");

        historicalMessages = Collections.singletonList(chatMessageEntity);
        mappedMessages = Collections.singletonList(chatMessage);
    }

    @Test
    void processMessage_WhenRecipientConnected_ShouldSendMessageAndSave() {
        // Arrange
        when(chatMessageMapper.toEntity(any(ChatMessage.class))).thenReturn(chatMessageEntity);
        when(userStatusService.isConnected(anyString())).thenReturn(true);
        doNothing().when(messagingTemplate).convertAndSendToUser(anyString(), anyString(), any(ChatMessage.class));

        // Act
        chatMessageService.processMessage(chatMessage);

        // Assert
        verify(chatMessageMapper).toEntity(chatMessage);
        verify(userStatusService).isConnected("recipient");
        verify(messagingTemplate).convertAndSendToUser(("recipient"),("/queue/messages"),(chatMessage)
        );
        verify(chatMessageRepository).save(chatMessageEntity);
    }

    @Test
    void processMessage_WhenRecipientNotConnected_ShouldOnlySave() {
        // Arrange
        when(chatMessageMapper.toEntity(any(ChatMessage.class))).thenReturn(chatMessageEntity);
        when(userStatusService.isConnected(anyString())).thenReturn(false);

        // Act
        chatMessageService.processMessage(chatMessage);

        // Assert
        verify(chatMessageMapper).toEntity(chatMessage);
        verify(userStatusService).isConnected("recipient");
        verify(messagingTemplate, never()).convertAndSendToUser(anyString(), anyString(), any(ChatMessage.class));
        verify(chatMessageRepository).save(chatMessageEntity);
    }

    @Test
    void deliverHistoricalMessages_WhenMessagesExist_ShouldDeliverMessages() {
        // Arrange
        String userId = "user123";
        when(chatMessageRepository.findAllMessagesInvolvingUser(anyString())).thenReturn(historicalMessages);
        when(chatMessageMapper.toModelList(anyList())).thenReturn(mappedMessages);

        // Act
        chatMessageService.deliverHistoricalMessages(userId);

        // Assert
        verify(chatMessageRepository).findAllMessagesInvolvingUser(userId);
        verify(chatMessageMapper).toModelList(historicalMessages);
        verify(messagingTemplate).convertAndSendToUser((userId),("/queue/messages"),(chatMessage)
        );
    }

    @Test
    void deliverHistoricalMessages_WhenNoMessages_ShouldNotDeliverMessages() {
        // Arrange
        String userId = "user123";
        when(chatMessageRepository.findAllMessagesInvolvingUser(anyString())).thenReturn(Collections.emptyList());

        // Act
        chatMessageService.deliverHistoricalMessages(userId);

        // Assert
        verify(chatMessageRepository).findAllMessagesInvolvingUser(userId);
        verify(chatMessageMapper, never()).toModelList(anyList());
        verify(messagingTemplate, never()).convertAndSendToUser(anyString(), anyString(), any(ChatMessage.class));
    }
}
