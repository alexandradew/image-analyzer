package io.github.alexandradew.image_analyzer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Standard response wrapper for successful requests")
public class SuccessResponse<T> {
    @Schema(description = "HTTP status code", example = "200")
    private int status;

    @Schema(description = "Descriptive message", example = "Image analyzed successfully")
    private String message;

    @Schema(description = "The actual content returned")
    private T content;
}
