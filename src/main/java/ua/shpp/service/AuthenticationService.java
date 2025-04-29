package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.shpp.dto.auth.JwtAuthenticationResponseDTO;
import ua.shpp.dto.auth.SignInRequestDTO;
import ua.shpp.dto.auth.SignUpRequestDTO;
import ua.shpp.entity.UserEntity;
import ua.shpp.model.GlobalRole;
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

    public JwtAuthenticationResponseDTO signUp(SignUpRequestDTO request) {
        log.info("Register new user: {}", request.getUsername());
        UserEntity userEntity = UserEntity.builder()
                .login(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .globalRole(GlobalRole.USER)
                .build();

        userService.create(userEntity);

        log.info("Registration successful for user: {}", request.getUsername());

        return generateTokenByUser(userEntity);
    }

    public JwtAuthenticationResponseDTO signIn(SignInRequestDTO request) {
        log.info("Sign in for user: {}", request.getLogin());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getLogin(),
                request.getPassword()
        ));

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        log.info("Successful login attempt for user: {}", request.getLogin());

        return generateTokenByUser(userEntity);
    }

    private JwtAuthenticationResponseDTO generateTokenByUser(UserEntity userEntity) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userEntity.getGlobalRole());

        String jwt = jwtService.generateToken(claims, userEntity);
        return new JwtAuthenticationResponseDTO(jwt);
    }
}