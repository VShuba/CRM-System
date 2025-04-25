package ua.shpp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Sign in request")
public class SignInRequestDTO {

    @Schema(description = "User login", example = "test@gmail.com")
    @Size(min = 5, max = 50, message = "Login length must be min = 5 and max = 50 characters")
    @NotBlank(message = "Login cannot be blank")
    private String login;

    @Schema(description = "Password", example = "my_1secret1_password")
    @Size(min = 8, max = 255, message = "Password length must be from 8 to 255 characters")
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
