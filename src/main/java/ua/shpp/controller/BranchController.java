package ua.shpp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.BranchPatchRequestDTO;
import ua.shpp.dto.BranchRequestDTO;
import ua.shpp.dto.BranchResponseDTO;
import ua.shpp.service.BranchService;

import java.net.URI;

@RestController
@RequestMapping("/api/branches") //TODO 13:42 15.04.2025 maybe /api/organizations/{orgId}/branches ???
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    ResponseEntity<BranchResponseDTO> createBranch(@RequestBody @Valid BranchRequestDTO requestDTO) {
        BranchResponseDTO createdBranch = branchService.create(requestDTO);

        URI location = URI.create("/api/branches/" + createdBranch.id());

        return ResponseEntity.created(location).body(createdBranch);
    }

    @GetMapping("/{id}")
    ResponseEntity<BranchResponseDTO> getBranch(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.get(id));
    }

    @PatchMapping("/{id}")
    ResponseEntity<BranchResponseDTO> updateBranch(@PathVariable Long id,
                                                   @RequestBody @Valid BranchPatchRequestDTO requestDTO) {
        return ResponseEntity.ok(branchService.updateName(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
