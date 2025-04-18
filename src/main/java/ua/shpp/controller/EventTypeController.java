package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.EventTypeRequestDTO;
import ua.shpp.dto.EventTypeResponseDTO;
import ua.shpp.service.EventTypeService;

@RestController
@RequestMapping("/api/event_types")
@RequiredArgsConstructor
@Tag(name = "Event Types", description = "API for managing event types")
public class EventTypeController {

    private final EventTypeService eventTypeService;

    @Operation(summary = "Create event type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event type successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventTypeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data", content = @Content),
            @ApiResponse(responseCode = "409", description = "An event type with this name already exists",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<EventTypeResponseDTO> create(@RequestBody @Valid EventTypeRequestDTO dto) {
        EventTypeResponseDTO response = eventTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get event type by ID (useful when copying)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event type found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventTypeResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Event type not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventTypeResponseDTO> get(@PathVariable Long id) {
        EventTypeResponseDTO response = eventTypeService.get(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update event type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event type updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventTypeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Event type not found", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<EventTypeResponseDTO> update(@PathVariable Long id,
                                                       @RequestBody @Valid EventTypeRequestDTO dto) {
        EventTypeResponseDTO response = eventTypeService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete event type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The event type has been deleted"),
            @ApiResponse(responseCode = "404", description = "Event type not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
