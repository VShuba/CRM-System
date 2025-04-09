package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Sign in request")
public class SignInRequest {

    @Size(min = 5, max = 50, message = "login length must be min = 5 and max = 50 characters")
    @NotBlank(message = "login cannot be blank")
    private String login;

    @Size(min = 8, max = 255, message = "password length must be from 8 to 255 characters")
    @NotBlank(message = "password cannot be blank")
    private String password;
}
