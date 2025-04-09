package ua.shpp.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ua.shpp.dto.JwtAuthenticationResponse;
import ua.shpp.entity.User;
import ua.shpp.service.JwtService;
import ua.shpp.service.UserService;
import ua.shpp.entity.Role;

import java.io.IOException;

// Success handler for Google oAuth account registration/retrieval
@Slf4j
@Component
public class OAuthSuccessHandle implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtService jwtService;

    public OAuthSuccessHandle(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        DefaultOAuth2User oAuthUser = (DefaultOAuth2User) authentication.getPrincipal();

        User user = User.builder()
                .login(oAuthUser.getAttribute("name"))
                .email(oAuthUser.getAttribute("email"))
                .role(Role.OWNER)
                .build();
        userService.createOAuthUser(user);
        var jwt = jwtService.generateToken(user);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        //todo create Bean
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(new JwtAuthenticationResponse(jwt)));
    }
}

