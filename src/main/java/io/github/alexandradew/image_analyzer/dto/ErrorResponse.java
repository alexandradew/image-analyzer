package io.github.alexandradew.image_analyzer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Standard response wrapper for invalid requests")
public class ErrorResponse {
    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Descriptive message", example = "Missing parameter: prompt")
    private String error;

    @Schema(description = "The field where the error occurred", example = "prompt", nullable = true)
    private String field;

    @Schema(description = "The actual content returned")
    private String content;
}
