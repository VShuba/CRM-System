package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.shpp.dto.auth.JwtAuthenticationResponseDTO;
import ua.shpp.dto.auth.SignInRequestDTO;
import ua.shpp.dto.auth.SignUpRequestDTO;
import ua.shpp.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and authorization operations")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "User registration", description = "Registering a new user with returning a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful registration",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data", content = @Content),
            @ApiResponse(responseCode = "409", description = "A user with this email already exists",
                    content = @Content)
    })
    @PostMapping("/sign-up")
    public JwtAuthenticationResponseDTO signUp(@RequestBody @Valid SignUpRequestDTO request) {
        return authenticationService.signUp(request);
    }


    @Operation(summary = "User authorization", description = "User login and obtaining a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful authorization",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Incorrect credentials", content = @Content)
    })
    @PostMapping("/sign-in")
    public JwtAuthenticationResponseDTO signIn(@RequestBody @Valid SignInRequestDTO request) {
        return authenticationService.signIn(request);
    }
}