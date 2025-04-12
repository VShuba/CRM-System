package ua.shpp.security.service.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Slf4j
@Service
public class PasswordGeneratorService {

    private static final int CYRILLIC_LETTER_START = 1040;
    private static final int CYRILLIC_LETTER_END = 1102;

    private final SecureRandom random;
    private final PasswordEncoder passwordEncoder;

    public PasswordGeneratorService(SecureRandom random, PasswordEncoder passwordEncoder) {
        this.random = random;
        this.passwordEncoder = passwordEncoder;
    }

    private String generateRandomString(int size) {
        return random.ints(size, CYRILLIC_LETTER_START, CYRILLIC_LETTER_END)
                .limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String generateRandomPassword(int size) {
        return passwordEncoder.encode(generateRandomString(size));
    }
}
