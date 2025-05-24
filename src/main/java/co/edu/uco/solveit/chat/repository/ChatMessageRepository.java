package co.edu.uco.solveit.chat.repository;

import co.edu.uco.solveit.chat.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    /**
     * Find all messages between two users
     * @param userIdSender first user
     * @param userIdRecipient second user
     * @return list of messages between the users
     */
    List<ChatMessageEntity> findBySenderAndRecipientOrSenderAndRecipientOrderByTimestampAsc(
            String sender1, String recipient1, String sender2, String recipient2);

    List<ChatMessageEntity> findByRecipientOrderByTimestampAsc(String userId);
}