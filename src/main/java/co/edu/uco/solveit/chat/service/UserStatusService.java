package co.edu.uco.solveit.chat.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class UserStatusService {
    
    private final Map<String, Boolean> connectedUsers = new ConcurrentHashMap<>();
    
    /**
     * Mark a user as connected
     * @param username the username
     */
    public void connect(String username) {
        connectedUsers.put(username, true);
    }
    
    /**
     * Mark a user as disconnected
     * @param username the username
     */
    public void disconnect(String username) {
        connectedUsers.put(username, false);
    }
    
    /**
     * Check if a user is connected
     * @param username the username
     * @return true if the user is connected, false otherwise
     */
    public boolean isConnected(String username) {
        return connectedUsers.getOrDefault(username, false);
    }
}