package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.SubscriptionHistoryDTO;
import ua.shpp.service.SubscriptionHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-history")
@RequiredArgsConstructor
@Tag(name = "Subscription History", description = "API для перегляду історії підписок клієнтів")
public class SubscriptionHistoryController {

    private final SubscriptionHistoryService subscriptionHistoryService;

    @Operation(summary = "Отримати відфільтровану історію підписок клієнта",
            description = "Повертає список дійсних або недійсних підписок клієнта з історії за параметром `isValid`.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успішно отримано список підписок",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SubscriptionHistoryDTO[].class))),
            @ApiResponse(responseCode = "400", description = "Невірний запит (наприклад, відсутній параметр 'isValid')",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Клієнта з вказаним ID не знайдено", content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    @GetMapping("/clients/{clientId}/filter")
    public ResponseEntity<List<SubscriptionHistoryDTO>> getClientSubscriptionHistory(
            @Parameter(description = "ID клієнта", required = true, example = "1")
            @PathVariable Long clientId,
            @Parameter(description = "Фільтр за валідністю (true - дійсні, false - недійсні)",
                    required = true, example = "true")
            @RequestParam Boolean isValid) {

        List<SubscriptionHistoryDTO> history =
                subscriptionHistoryService.getSubscriptionHistoryByClientAndValidity(clientId, isValid);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Отримати всю історію підписок клієнта",
            description = "Повертає повний список всіх підписок клієнта з історії (дійсних та недійсних).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успішно отримано повний список підписок",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SubscriptionHistoryDTO[].class))),
            @ApiResponse(responseCode = "404", description = "Клієнта з вказаним ID не знайдено", content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    @GetMapping("/clients/{clientId}")
    public ResponseEntity<List<SubscriptionHistoryDTO>> getAllClientSubscriptionHistory(
            @Parameter(description = "ID клієнта", required = true, example = "1")
            @PathVariable Long clientId) {

        List<SubscriptionHistoryDTO> history = subscriptionHistoryService.getAllSubscriptionHistoryByClient(clientId);
        return ResponseEntity.ok(history);
    }
}
