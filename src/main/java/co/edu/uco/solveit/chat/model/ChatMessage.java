package co.edu.uco.solveit.chat.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String recipient;
    private LocalDateTime timestamp;
}
