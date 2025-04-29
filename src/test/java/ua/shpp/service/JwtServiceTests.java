package ua.shpp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.shpp.entity.UserEntity;
import ua.shpp.model.GlobalRole;
import ua.shpp.security.service.JwtService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTests {

    @Autowired
    private JwtService jwtService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(1L)
                .login("testuser@example.com")
                .email("test@mail.com")
                .password("secretPassword")
                .globalRole(GlobalRole.USER) // <-- UPD
                .build();
    }

    @Test
    void shouldGenerateValidToken() {
        String token = jwtService.generateToken(userEntity);

        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void shouldExtractEmailFromToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userEntity.getId());
        claims.put("email", userEntity.getEmail());
        claims.put("role", userEntity.getGlobalRole());

        String token = jwtService.generateToken(claims, userEntity);
        String role = jwtService.extractAuthority(token);

        assertEquals(userEntity.getGlobalRole().name(), role);
    }

    @Test
    void shouldExtractUsernameFromToken() {
        String token = jwtService.generateToken(userEntity);
        String username = jwtService.extractUserName(token);

        assertEquals(userEntity.getUsername(), username);
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        String fakeToken = "invalid-token";

        assertThrows(Exception.class, () -> jwtService.isTokenValid(fakeToken));
    }
}