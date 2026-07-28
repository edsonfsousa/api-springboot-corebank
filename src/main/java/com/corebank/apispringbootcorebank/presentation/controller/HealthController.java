package com.corebank.apispringbootcorebank.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Health", description = "API availability check")
public class HealthController {

    @GetMapping(value = "/api/v1/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Check API health",
            description = "Returns a simple status indicating that the API is available."
    )
    @ApiResponse(
            responseCode = "200",
            description = "API is available",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = "{\"status\":\"UP\"}"),
                    examples = @ExampleObject(value = """
                            {
                              "status": "UP"
                            }
                            """)
            )
    )
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
