package ua.shpp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Ticket Scrum-33
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Registration request")
public class SignUpRequestDTO {

    @Schema(description = "User name", example = "user@gmail.com")
    @Size(min = 5, max = 255, message = "Username must be between 5 and 255 characters long")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Schema(description = "Email address", example = "user@gmail.com")
    @Size(min = 5, max = 255, message = "Email address must be between 5 and 255 characters long")
    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "The email address must be in the format user@example.com")
    private String email;

    @Schema(description = "Password", example = "my_1secret1_password")
    @Size(max = 255, message = "The password length must be no more than 255 characters")
    private String password;
}