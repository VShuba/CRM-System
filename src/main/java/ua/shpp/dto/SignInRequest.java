package ua.shpp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

//Ticket Scrum-33
@Data
//@Schema(description = "Запит на аутентифікацію")
public class SignInRequest {

//    @Schema(description = "Ім'я користувача", example = "User1")
    @Size(min = 5, max = 50, message = "Ім'я користувача має бути від 5 до 50 символів")
    @NotBlank(message = "Ім'я користувача не може бути порожнім")
    private String username;

//    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(min = 8, max = 255, message = "Довжина пароля має бути від 8 до 255 символів")
    @NotBlank(message = "Пароль не може бути порожнім")
    private String password;
}
