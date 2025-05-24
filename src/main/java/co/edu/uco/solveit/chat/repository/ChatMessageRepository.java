package co.edu.uco.solveit.chat.repository;

import co.edu.uco.solveit.chat.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    /**
     * Find all messages between two users
     * @param sender1 first user as sender
     * @param recipient1 second user as recipient
     * @param sender2 second user as sender
     * @param recipient2 first user as recipient
     * @return list of messages between the users
     */
    List<ChatMessageEntity> findBySenderAndRecipientOrSenderAndRecipientOrderByTimestampAsc(
            String sender1, String recipient1, String sender2, String recipient2);

    /**
     * Find all messages where the user is either the sender or recipient
     * @param userId the user ID
     * @return list of messages where the user is involved
     */
    @Query("SELECT m FROM ChatMessageEntity m WHERE m.sender = :userId OR m.recipient = :userId ORDER BY m.timestamp ASC")
    List<ChatMessageEntity> findAllMessagesInvolvingUser(@Param("userId") String userId);
}
