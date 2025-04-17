package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.OneTimeOfferDTO;
import ua.shpp.service.OneTimeOfferService;

import java.net.URI;

@RestController
@RequestMapping("/api/offer/one-time")
@RequiredArgsConstructor
@Tag(name = "OneTimeOffer", description = "One-time service management API")
public class OneTimeOfferController {

    private final OneTimeOfferService oneTimeOfferService;

    @PostMapping
    public ResponseEntity<OneTimeOfferDTO> create(
            @RequestBody OneTimeOfferDTO oneTimeOfferDTO) {
        var service = oneTimeOfferService.create(oneTimeOfferDTO);
        URI location = URI.create("/api/offer/one-time/" + service.id());

        return ResponseEntity.created(location).body(service);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OneTimeOfferDTO> get(@PathVariable Long id) {
        var dto = oneTimeOfferService.get(id);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping
    public ResponseEntity<OneTimeOfferDTO> update(
            @RequestBody OneTimeOfferDTO oneTimeOfferDTO) {
        var service = oneTimeOfferService.update(oneTimeOfferDTO);
        URI location = URI.create("/api/offer/one-time/" + service.id());

        return ResponseEntity
                .ok()
                .header("Location", location.toString())
                .body(service);
    }


    @Operation(summary = "Delete a one‑time offer by its id",
            description = "Delete a one‑time offer by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<OneTimeOfferDTO> delete(@PathVariable Long id) {
        oneTimeOfferService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
