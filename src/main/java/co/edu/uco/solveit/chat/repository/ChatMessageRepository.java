package co.edu.uco.solveit.chat.repository;

import co.edu.uco.solveit.chat.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    
    /**
     * Find all undelivered messages for a specific recipient
     * @param recipient the username of the recipient
     * @param delivered whether the messages have been delivered
     * @return list of undelivered messages
     */
    List<ChatMessageEntity> findByRecipientAndDeliveredOrderByTimestampAsc(String recipient, boolean delivered);
    
    /**
     * Find all messages between two users
     * @param user1 first user
     * @param user2 second user
     * @return list of messages between the users
     */
    List<ChatMessageEntity> findBySenderAndRecipientOrSenderAndRecipientOrderByTimestampAsc(
            String sender1, String recipient1, String sender2, String recipient2);
}