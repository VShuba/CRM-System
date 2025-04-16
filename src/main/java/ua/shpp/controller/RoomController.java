package ua.shpp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.RoomRequestDTO;
import ua.shpp.dto.RoomResponseDTO;
import ua.shpp.service.RoomService;

import java.net.URI;

@RestController
@RequestMapping("/api/organizations/{orgId}/branches/{branchId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService service;

    @PostMapping
    public ResponseEntity<RoomResponseDTO> createRoom(
            @PathVariable(name = "orgId") Long orgId,
            @PathVariable(name = "branchId") Long branchId,
            @RequestBody @Valid RoomRequestDTO requestDTO) {

        RoomResponseDTO createdRoom = service.create(orgId, branchId, requestDTO);

        URI location = URI.create("/api/organizations/" + orgId + "/branches/" + branchId + "/rooms/" + createdRoom.id());

        return ResponseEntity.created(location).body(createdRoom);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponseDTO> getRoom(
            @PathVariable(name = "orgId") Long orgId,
            @PathVariable(name = "branchId") Long branchId,
            @PathVariable(name = "roomId") Long roomId) {

        return ResponseEntity.ok(service.get(orgId, branchId, roomId));
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<RoomResponseDTO> patchRoom(
            @PathVariable(name = "orgId") Long orgId,
            @PathVariable(name = "branchId") Long branchId,
            @PathVariable(name = "roomId") Long roomId,
            @RequestBody @Valid RoomRequestDTO requestDTO) {
        return ResponseEntity.ok(service.patch(orgId, branchId, roomId, requestDTO));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<RoomResponseDTO> deleteRoom(
            @PathVariable(name = "orgId") Long orgId,
            @PathVariable(name = "branchId") Long branchId,
            @PathVariable(name = "roomId") Long roomId) {

        return ResponseEntity.ok(service.delete(orgId, branchId, roomId));
    }
}
