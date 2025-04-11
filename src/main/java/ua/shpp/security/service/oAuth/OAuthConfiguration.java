package ua.shpp.security.service.oAuth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class OAuthConfiguration {

    @Bean
    SecureRandom getRandom() {
        return new SecureRandom();
    }
}
