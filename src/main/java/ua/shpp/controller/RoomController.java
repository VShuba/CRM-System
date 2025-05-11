package ua.shpp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.branch.room.RoomRequestDTO;
import ua.shpp.dto.branch.room.RoomResponseDTO;
import ua.shpp.service.RoomService;

import java.net.URI;

@RestController
@RequestMapping("/api/organizations/{orgId}/branches/{branchId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService service;

    @PostMapping
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<RoomResponseDTO> createRoom(
            @PathVariable(name = "orgId") Long orgId,
            @PathVariable(name = "branchId") Long branchId,
            @RequestBody @Valid RoomRequestDTO requestDTO) {

        RoomResponseDTO createdRoom = service.create(orgId, branchId, requestDTO);

        URI location = URI.create("/api/organizations/" + orgId + "/branches/" + branchId + "/rooms/" + createdRoom.id());

        return ResponseEntity.created(location).body(createdRoom);
    }

    @GetMapping("/{roomId}")
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<RoomResponseDTO> getRoom(
            @PathVariable(name = "orgId") Long orgId,
            @PathVariable(name = "branchId") Long branchId,
            @PathVariable(name = "roomId") Long roomId) {

        return ResponseEntity.ok(service.get(orgId, branchId, roomId));
    }

    @GetMapping
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<Page<RoomResponseDTO>> getAllRooms(
            @PathVariable(name = "orgId") Long orgId,
            @PathVariable(name = "branchId") Long branchId,
            @ParameterObject Pageable pageRequest) {
        return ResponseEntity.ok(service.getAll(orgId, branchId, pageRequest));
    }

    @PatchMapping("/{roomId}")
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<RoomResponseDTO> patchRoom(
            @PathVariable(name = "orgId") Long orgId,
            @PathVariable(name = "branchId") Long branchId,
            @PathVariable(name = "roomId") Long roomId,
            @RequestBody @Valid RoomRequestDTO requestDTO) {
        return ResponseEntity.ok(service.patch(orgId, branchId, roomId, requestDTO));
    }

    @DeleteMapping("/{roomId}")
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<RoomResponseDTO> deleteRoom(
            @PathVariable(name = "orgId") Long orgId,
            @PathVariable(name = "branchId") Long branchId,
            @PathVariable(name = "roomId") Long roomId) {

        return ResponseEntity.ok(service.delete(orgId, branchId, roomId));
    } // todo maybe return 204 - no content?
}
