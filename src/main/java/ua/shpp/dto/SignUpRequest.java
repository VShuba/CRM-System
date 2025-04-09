package ua.shpp.dto;

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
//@Schema(description = "Запит не реєстрацію")
public class SignUpRequest {

    @Schema(description = "Ім'я користувача", example = "User1")
    @Size(min = 5, max = 50, message = "Ім'я користувача має бути від 5 до 50 символів")
    @NotBlank(message = "Ім'я користувача не може бути порожнім")
    private String username;

    @Schema(description = "Адреса електронної пошти", example = "user@gmail.com")
    @Size(min = 5, max = 255, message = "Адреса електронної пошти має бути від 5 до 255 символів")
    @NotBlank(message = "Адреса електронної пошти не може бути порожньою")
    @Email(message = "Адреса електронної пошти має бути в форматі user@example.com")
    private String email;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(max = 255, message = "Довжина пароля має бути не більш 255 символів")
    private String password;
}