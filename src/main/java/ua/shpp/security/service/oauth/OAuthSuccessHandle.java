package ua.shpp.security.service.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ua.shpp.dto.auth.JwtAuthenticationResponseDTO;
import ua.shpp.entity.UserEntity;
import ua.shpp.model.GlobalRole;
import ua.shpp.security.service.JwtService;
import ua.shpp.service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Success handler for Google oAuth account registration/retrieval
@Slf4j
@Component
public class OAuthSuccessHandle implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordGeneratorService passwordGeneratorService;

    private static final Integer RANDOM_PASSWORD_LENGTH = 15;
    public static final String EMAIL = "email";

    private final ObjectMapper objectMapper;

    public OAuthSuccessHandle(UserService userService,
                              JwtService jwtService,
                              PasswordGeneratorService passwordGeneratorService,
                              ObjectMapper objectMapper
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordGeneratorService = passwordGeneratorService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        DefaultOAuth2User oAuthUser = (DefaultOAuth2User) authentication.getPrincipal();
        log.debug("Entered onAuthenticationSuccess");
        UserEntity userEntity = UserEntity.builder()
                .login(oAuthUser.getAttribute(EMAIL))
                .email(oAuthUser.getAttribute(EMAIL))
                .password(passwordGeneratorService.generateRandomPassword(RANDOM_PASSWORD_LENGTH))
                .globalRole(GlobalRole.USER)
                .build();
        userEntity = userService.createOAuthUser(userEntity);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userEntity.getId());
        claims.put("role", userEntity.getGlobalRole());

        var jwt = jwtService.generateToken(claims, userEntity);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(new JwtAuthenticationResponseDTO(jwt)));
    }
}