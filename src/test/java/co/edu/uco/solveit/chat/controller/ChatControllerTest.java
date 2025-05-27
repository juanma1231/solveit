package co.edu.uco.solveit.chat.controller;

import co.edu.uco.solveit.chat.model.ChatMessage;
import co.edu.uco.solveit.chat.model.MessageType;
import co.edu.uco.solveit.chat.service.ChatMessageService;
import co.edu.uco.solveit.chat.service.UserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private ChatMessageService chatMessageService;

    @Mock
    private UserStatusService userStatusService;

    @Mock
    private SimpMessageHeaderAccessor headerAccessor;

    @InjectMocks
    private ChatController chatController;

    private ChatMessage chatMessage;
    private Map<String, Object> sessionAttributes;

    @BeforeEach
    void setUp() {
        chatMessage = ChatMessage.builder()
                .type(MessageType.CHAT)
                .sender("sender")
                .recipient("recipient")
                .content("Hello, world!")
                .build(); // No timestamp set initially

        sessionAttributes = new HashMap<>();
    }

    @Test
    void sendMessage_WhenTimestampIsNull_ShouldSetTimestamp() {
        // Arrange
        doNothing().when(chatMessageService).processMessage(any(ChatMessage.class));

        // Act
        chatController.sendMessage(chatMessage);

        // Assert
        verify(chatMessageService).processMessage(chatMessage);
        // Verify timestamp was set
        verify(chatMessageService).processMessage(argThat(message -> 
            message.getTimestamp() != null
        ));
    }

    @Test
    void sendMessage_WhenTimestampIsNotNull_ShouldNotChangeTimestamp() {
        // Arrange
        LocalDateTime originalTimestamp = LocalDateTime.now().minusHours(1);
        chatMessage.setTimestamp(originalTimestamp);
        doNothing().when(chatMessageService).processMessage(any(ChatMessage.class));

        // Act
        chatController.sendMessage(chatMessage);

        // Assert
        verify(chatMessageService).processMessage(chatMessage);
        // Verify timestamp was not changed
        verify(chatMessageService).processMessage(argThat(message -> 
            message.getTimestamp().equals(originalTimestamp)
        ));
    }

    @Test
    void register_ShouldSetUsernameInSessionAttributes() {
        // Arrange
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);
        doNothing().when(userStatusService).connect(anyString());
        doNothing().when(messagingTemplate).convertAndSendToUser(
                anyString(), anyString(), any(ChatMessage.class));
        doNothing().when(chatMessageService).deliverHistoricalMessages(anyString());

        // Act
        chatController.register(chatMessage, headerAccessor);

        // Assert
        // Verify username was set in session attributes
        assertEquals("sender", sessionAttributes.get("username"));
    }

    @Test
    void register_WhenTimestampIsNull_ShouldSetTimestamp() {
        // Arrange
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);
        doNothing().when(userStatusService).connect(anyString());
        doNothing().when(messagingTemplate).convertAndSendToUser(
                anyString(), anyString(), any(ChatMessage.class));
        doNothing().when(chatMessageService).deliverHistoricalMessages(anyString());

        // Act
        chatController.register(chatMessage, headerAccessor);

        // Assert
        // Verify timestamp was set
        assertNotNull(chatMessage.getTimestamp());
    }

    @Test
    void register_ShouldConnectUser() {
        // Arrange
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);
        doNothing().when(userStatusService).connect(anyString());
        doNothing().when(messagingTemplate).convertAndSendToUser(
                anyString(), anyString(), any(ChatMessage.class));
        doNothing().when(chatMessageService).deliverHistoricalMessages(anyString());

        // Act
        chatController.register(chatMessage, headerAccessor);

        // Assert
        verify(userStatusService).connect("sender");
    }

    @Test
    void register_ShouldSendJoinMessage() {
        // Arrange
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);
        doNothing().when(userStatusService).connect(anyString());
        doNothing().when(messagingTemplate).convertAndSendToUser(
                anyString(), anyString(), any(ChatMessage.class));
        doNothing().when(chatMessageService).deliverHistoricalMessages(anyString());

        // Act
        chatController.register(chatMessage, headerAccessor);

        // Assert
        verify(messagingTemplate).convertAndSendToUser(
                eq("sender"),
                eq("/queue/messages"),
                argThat(msg -> {
                    ChatMessage message = (ChatMessage) msg;
                    return message.getType() == MessageType.JOIN &&
                           message.getSender().equals("system") &&
                           message.getRecipient().equals("sender") &&
                           message.getContent().equals("Conectado con exito a la sala de chat") &&
                           message.getTimestamp() != null;
                })
        );
    }

    @Test
    void register_ShouldDeliverHistoricalMessages() {
        // Arrange
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);
        doNothing().when(userStatusService).connect(anyString());
        doNothing().when(messagingTemplate).convertAndSendToUser(
                anyString(), anyString(), any(ChatMessage.class));
        doNothing().when(chatMessageService).deliverHistoricalMessages(anyString());

        // Act
        chatController.register(chatMessage, headerAccessor);

        // Assert
        verify(chatMessageService).deliverHistoricalMessages("sender");
    }

    @Test
    void register_WhenHeaderAccessorIsNull_ShouldNotSetSessionAttributes() {
        // Arrange
        doNothing().when(userStatusService).connect(anyString());
        doNothing().when(messagingTemplate).convertAndSendToUser(
                anyString(), anyString(), any(ChatMessage.class));
        doNothing().when(chatMessageService).deliverHistoricalMessages(anyString());

        // Act
        chatController.register(chatMessage, null);

        // Assert
        // No exception should be thrown
        verify(userStatusService).connect("sender");
        verify(messagingTemplate).convertAndSendToUser(
                anyString(), anyString(), any(ChatMessage.class));
        verify(chatMessageService).deliverHistoricalMessages("sender");
    }

    @Test
    void register_WhenSessionAttributesIsNull_ShouldNotSetSessionAttributes() {
        // Arrange
        when(headerAccessor.getSessionAttributes()).thenReturn(null);
        doNothing().when(userStatusService).connect(anyString());
        doNothing().when(messagingTemplate).convertAndSendToUser(
                anyString(), anyString(), any(ChatMessage.class));
        doNothing().when(chatMessageService).deliverHistoricalMessages(anyString());

        // Act
        chatController.register(chatMessage, headerAccessor);

        // Assert
        // No exception should be thrown
        verify(userStatusService).connect("sender");
        verify(messagingTemplate).convertAndSendToUser(
                anyString(), anyString(), any(ChatMessage.class));
        verify(chatMessageService).deliverHistoricalMessages("sender");
    }

    // Helper methods
    private static void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    private static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError("Expected non-null value");
        }
    }
}
