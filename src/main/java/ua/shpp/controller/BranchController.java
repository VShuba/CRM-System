package ua.shpp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.BranchRequestDTO;
import ua.shpp.dto.BranchResponseDTO;
import ua.shpp.service.BranchService;

@RestController
@RequestMapping("/api/branches") //maybe /api/organizations/{orgId}/branches ???
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    ResponseEntity<BranchResponseDTO> createBranch(@RequestBody @Valid BranchRequestDTO requestDTO) {
        return ResponseEntity.ok(branchService.create(requestDTO));
    }

    @GetMapping("/{id}")
    ResponseEntity<BranchResponseDTO> getBranch(@PathVariable Integer id) {
        return ResponseEntity.ok(branchService.get(id));
    }

    @PatchMapping("/{id}")
    ResponseEntity<BranchResponseDTO> updateBranch(@PathVariable Integer id, @RequestBody BranchRequestDTO requestDTO) {
        return ResponseEntity.ok(branchService.update(id, requestDTO));
    }
}
