package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.ScheduleEventDto;
import ua.shpp.dto.ScheduleEventFilterDto;
import ua.shpp.service.ScheduleEventService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedule/event")
@RequiredArgsConstructor
@Tag(name = "ScheduleEvent", description = "Schedule events management API")
public class ScheduleEventController {
    private final ScheduleEventService scheduleEventService;

    @Operation(summary = "Create schedule event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Schedule event successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScheduleEventDto.class))),
            @ApiResponse(responseCode = "404", description = "Service id not fount", content = @Content),
    })
    @PostMapping
    @PreAuthorize("@authz.hasRoleByServiceId(#scheduleEventDto.serviceId(), T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<ScheduleEventDto> create(@RequestBody ScheduleEventDto scheduleEventDto) {
        var event = scheduleEventService.create(scheduleEventDto);
        URI location = URI.create("/api/schedule/event/" + event.id());

        return ResponseEntity.created(location).body(event);
    }

    @Operation(summary = "Get schedule event by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule event successfully find",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScheduleEventDto.class))),
            @ApiResponse(responseCode = "404", description = "Schedule event id not fount", content = @Content),
    })
    @GetMapping("/{id}")
    @PreAuthorize("@authz.hasRoleByScheduleEventId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<ScheduleEventDto> getById(@PathVariable Long id) {
        var dto = scheduleEventService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get schedule event by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule event successfully find",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ScheduleEventDto.class)))),
            @ApiResponse(responseCode = "404", description = "Schedule event id not fount", content = @Content),
    })
    @GetMapping("/from/{start}/to/{end}") // todo я не можу зробити PreAuthorize тому шо тут тянуться взагалі всі events. Незалежно від організації
    public ResponseEntity<List<ScheduleEventDto>> getEventsFromTo(
            @Parameter(
                    description = "Start date (day-month-year, dd-MM-yyyy)",
                    example = "22-10-2025"
            )
            @PathVariable
            @DateTimeFormat(pattern = "dd-MM-yyyy")
            LocalDate start,

            @Parameter(
                    description = "End date (day-month-year, dd-MM-yyyy)",
                    example = "29-10-2025"
            )
            @PathVariable
            @DateTimeFormat(pattern = "dd-MM-yyyy")
            LocalDate end) {
        var dto = scheduleEventService.getAllBetweenDates(start, end);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Delete a schedule event by id",
            description = "Delete a schedule event by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@authz.hasRoleByScheduleEventId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        scheduleEventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Filter schedule event by type, roomId, employeeId, serviceId, startDate, endDate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule event successfully find",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ScheduleEventDto.class)))),
    })
    @GetMapping("filter/from/{start}/to/{end}")
    // todo тут ніде немає фільтрації за organization_id, а service_id, room_id, employee_id - можуть належати різним організаціям.
    // todo тому так само не можу прикрутити PreAuthorize
    public ResponseEntity<List<ScheduleEventDto>> eventFilter(

            @ModelAttribute ScheduleEventFilterDto dto,
            @Parameter(
                    description = "Start date (day-month-year, dd-MM-yyyy)",
                    example = "22-10-2025"
            )
            @PathVariable
            @DateTimeFormat(pattern = "dd-MM-yyyy")
            LocalDate start,

            @Parameter(
                    description = "End date (day-month-year, dd-MM-yyyy)",
                    example = "29-10-2025"
            )
            @PathVariable
            @DateTimeFormat(pattern = "dd-MM-yyyy")
            LocalDate end) {
        var list = scheduleEventService
                .eventFilter(dto.roomId(), dto.employeeId(), dto.serviceId(), dto.eventTypeId(), start, end);
        return ResponseEntity.ok(list);
    }
}
