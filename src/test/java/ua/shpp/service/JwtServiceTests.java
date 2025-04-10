package ua.shpp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.shpp.entity.Role;
import ua.shpp.entity.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTests {

    @Autowired
    private JwtService jwtService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .login("testuser@example.com")
                .email("test@mail.com")
                .password("secretPassword")
                .role(Role.OWNER)
                .build();
    }

    @Test
    void shouldGenerateValidToken() {
        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void shouldExtractEmailFromToken() {
        String token = jwtService.generateToken(user);
        String email = jwtService.extractEmail(token);

        assertEquals(user.getEmail(), email);
    }

    @Test
    void shouldExtractUsernameFromToken() {
        String token = jwtService.generateToken(user);
        String username = jwtService.extractUserName(token);

        assertEquals(user.getUsername(), username);
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        String fakeToken = "invalid-token";

        assertThrows(Exception.class, () -> jwtService.isTokenValid(fakeToken, user));
    }
}
