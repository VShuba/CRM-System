package ua.shpp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Ticket Scrum-33
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Schema(description = "Відповідь з токеном доступу")
public class JwtAuthenticationResponse {
//    @Schema(description = "Токен доступу", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String token;
}