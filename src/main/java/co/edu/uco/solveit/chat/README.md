# UCO SolveIT Chat Module

This module provides a simple one-to-one real-time chat functionality using WebSocket for the UCO SolveIT application.

## Features

- Real-time one-to-one messaging using WebSocket
- User registration and connection notifications
- Text-only messages (no attachments)
- Backend-only implementation (frontend is in a separate repository)

## Technical Implementation

The chat functionality is implemented using:

- Spring WebSocket with STOMP protocol
- In-memory message handling (no persistence)
- Spring controllers for message routing
- User-specific message destinations for private messaging

## API Endpoints

### WebSocket Endpoints

- `/ws` - WebSocket connection endpoint with SockJS fallback

### Message Destinations

- Client to Server:
  - `/app/chat.sendMessage` - Send a private chat message
  - `/app/chat.register` - Register a user for chat

- Server to Client:
  - `/user/{username}/queue/messages` - Receive private messages

## Message Format

```json
{
  "type": "CHAT|JOIN|LEAVE",
  "content": "Message content",
  "sender": "SenderUsername",
  "recipient": "RecipientUsername",
  "timestamp": "2023-05-01T12:34:56"
}
```

## Integration with Frontend

The frontend application should:

1. Connect to the WebSocket endpoint `/ws`
2. Register the user by sending a message to `/app/chat.register`
3. Subscribe to `/user/{username}/queue/messages` to receive messages
4. Send messages to `/app/chat.sendMessage` with both sender and recipient fields

## Example Usage

### Connecting and Registering

```javascript
// Connect to WebSocket
const socket = new SockJS('http://your-backend-url/ws');
const stompClient = Stomp.over(socket);

// On connection, register the user
stompClient.connect({}, frame => {
  // Subscribe to private messages
  stompClient.subscribe('/user/username/queue/messages', message => {
    const receivedMessage = JSON.parse(message.body);
    // Handle received message
  });

  // Register the user
  stompClient.send('/app/chat.register', {}, JSON.stringify({
    sender: 'username',
    type: 'JOIN'
  }));
});
```

### Sending a Message

```javascript
stompClient.send('/app/chat.sendMessage', {}, JSON.stringify({
  sender: 'senderUsername',
  recipient: 'recipientUsername',
  content: 'Hello!',
  type: 'CHAT'
}));
```

## Future Enhancements

Potential future enhancements could include:

- Message persistence
- User authentication integration
- Online user status
- Typing indicators
- Read receipts
- Message history retrieval
