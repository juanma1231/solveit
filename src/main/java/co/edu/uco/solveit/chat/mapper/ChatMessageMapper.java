package co.edu.uco.solveit.chat.mapper;

import co.edu.uco.solveit.chat.entity.ChatMessageEntity;
import co.edu.uco.solveit.chat.model.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMessageMapper {

    /**
     * Convert a ChatMessage model to a ChatMessageEntity
     * @param chatMessage the chat message model
     * @return the chat message entity
     */
    public ChatMessageEntity toEntity(ChatMessage chatMessage) {
        return ChatMessageEntity.builder()
                .type(chatMessage.getType())
                .content(chatMessage.getContent())
                .sender(chatMessage.getSender())
                .recipient(chatMessage.getRecipient())
                .timestamp(chatMessage.getTimestamp())
                .delivered(false)
                .build();
    }

    /**
     * Convert a ChatMessageEntity to a ChatMessage model
     * @param entity the chat message entity
     * @return the chat message model
     */
    public ChatMessage toModel(ChatMessageEntity entity) {
        return ChatMessage.builder()
                .type(entity.getType())
                .content(entity.getContent())
                .sender(entity.getSender())
                .recipient(entity.getRecipient())
                .timestamp(entity.getTimestamp())
                .build();
    }

    /**
     * Convert a list of ChatMessageEntity objects to a list of ChatMessage models
     * @param entities the list of entities
     * @return the list of models
     */
    public List<ChatMessage> toModelList(List<ChatMessageEntity> entities) {
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}