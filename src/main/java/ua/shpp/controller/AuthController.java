package ua.shpp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.shpp.service.AuthenticationService;
import ua.shpp.dto.JwtAuthenticationResponse;
import ua.shpp.dto.SignInRequest;
import ua.shpp.dto.SignUpRequest;

//Ticket Scrum-33
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
//@Tag(name = "Аутентифікація")
public class AuthController {
    private final AuthenticationService authenticationService;

    //@Operation(summary = "Реєстрація користувача")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    //@Operation(summary = "Авторизація користувача")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}