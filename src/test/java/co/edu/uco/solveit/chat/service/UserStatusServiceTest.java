package co.edu.uco.solveit.chat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserStatusServiceTest {

    private UserStatusService userStatusService;

    @BeforeEach
    void setUp() {
        userStatusService = new UserStatusService();
    }

    @Test
    void connectShouldMarkUserAsConnected() {
        // Arrange
        String username = "testUser";

        // Act
        userStatusService.connect(username);

        // Assert
        assertTrue(userStatusService.isConnected(username));
    }

    @Test
    void disconnect_ShouldMarkUserAsDisconnected() {
        // Arrange
        String username = "testUser";
        userStatusService.connect(username);
        assertTrue(userStatusService.isConnected(username));

        // Act
        userStatusService.disconnect(username);

        // Assert
        assertFalse(userStatusService.isConnected(username));
    }

    @Test
    void isConnected_WhenUserNotInMap_ShouldReturnFalse() {
        // Arrange
        String username = "nonExistentUser";

        // Act & Assert
        assertFalse(userStatusService.isConnected(username));
    }



    @Test
    void isConnected_WhenUserDisconnected_ShouldReturnFalse() {
        // Arrange
        String username = "testUser";
        userStatusService.connect(username);
        userStatusService.disconnect(username);

        // Act & Assert
        assertFalse(userStatusService.isConnected(username));
    }
}