package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
@Tag(name = "Log files", description = "Obtaining application log files")
public class LogController {

    @Operation(summary = "Get log file",
            description = "Returns the current application log file in plain text format")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Log file successfully returned"),
            @ApiResponse(responseCode = "404", description = "Log file not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/logfile")
    public ResponseEntity<Resource> getLogfile() {
        Path path = Paths.get("logs/app.log");
        Resource resource = new FileSystemResource(path.toFile());
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }
}

