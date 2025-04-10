package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.shpp.dto.JwtAuthenticationResponse;
import ua.shpp.dto.SignInRequest;
import ua.shpp.dto.SignUpRequest;
import ua.shpp.entity.UserEntity;
import ua.shpp.model.Role;
import ua.shpp.security.service.JwtService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        log.info("Register new user: {}", request.getUsername());
        UserEntity userEntity = UserEntity.builder()
                .login(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.OWNER)
                .build();

        userService.create(userEntity);

        log.info("Registration successful for user: {}", request.getUsername());
        String jwt = jwtService.generateToken(userEntity);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        log.info("Sign in for user: {}", request.getLogin());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getLogin(),
                    request.getPassword()
            ));
        } catch (AuthenticationException ex) {
            log.warn("Unsuccessful login attempt ");
            throw ex;
        }

        UserEntity userEntity = userService.getByLogin(request.getLogin());
        log.info("Successful login attempt for user: {}", request.getLogin());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userEntity.getId());
        claims.put("email", userEntity.getEmail());
        claims.put("role", userEntity.getRole());

        String jwt = jwtService.generateToken(claims, userEntity);
        return new JwtAuthenticationResponse(jwt);
    }
}