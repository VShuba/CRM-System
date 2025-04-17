package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.SubscriptionOfferDTO;
import ua.shpp.service.SubscriptionOfferService;

import java.net.URI;

@RestController
@RequestMapping("/api/offer/subscription")
@RequiredArgsConstructor
@Tag(name = "SubscriptionOffer", description = "Subscription offer management API")
public class SubscriptionOfferController {
    private final SubscriptionOfferService subscriptionOfferService;

    @PostMapping
    public ResponseEntity<SubscriptionOfferDTO> create(
            @RequestBody SubscriptionOfferDTO subscriptionOfferDTO) {
        var offer = subscriptionOfferService.create(subscriptionOfferDTO);
        URI location = URI.create("/api/offer/subscription/" + offer.id());

        return ResponseEntity.created(location).body(offer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionOfferDTO> get(@PathVariable Long id) {
        var dto = subscriptionOfferService.get(id);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping
    public ResponseEntity<SubscriptionOfferDTO> update(
            @RequestBody SubscriptionOfferDTO subscriptionOfferDTO) {
        var service = subscriptionOfferService.update(subscriptionOfferDTO);
        URI location = URI.create("/api/offer/subscription/" + service.id());

        return ResponseEntity
                .ok()
                .header("Location", location.toString())
                .body(service);
    }


    @Operation(summary = "Delete a subscription offer by its id",
            description = "Delete a subscription offer by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SubscriptionOfferDTO> delete(@PathVariable Long id) {
        subscriptionOfferService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
