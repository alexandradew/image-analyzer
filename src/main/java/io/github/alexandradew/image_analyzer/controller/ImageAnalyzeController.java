package io.github.alexandradew.image_analyzer.controller;

import io.github.alexandradew.image_analyzer.dto.SuccessResponse;
import io.github.alexandradew.image_analyzer.service.LlmService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;

import java.io.File;
import java.io.IOException;

@Validated
@RestController
@RequestMapping("/image")
public class ImageAnalyzeController {

    LlmService llmService;

    public ImageAnalyzeController(LlmService llmService) {
        this.llmService = llmService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<SuccessResponse<String>> extractText(@RequestParam("file") @NotNull MultipartFile file, @RequestParam("prompt") @NotBlank String prompt) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new SuccessResponse<>(400, "Uploaded file is empty.", null));
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        File tempFile = File.createTempFile("upload_", extension);
        file.transferTo(tempFile);

        String contentType = file.getContentType();

        if (contentType != null && contentType.startsWith("image/")) {
            String analyzerText = llmService.describeImage(tempFile, prompt);
            return ResponseEntity.ok(new SuccessResponse<>(200, "Image analyzed successfully", analyzerText));
        }

        return ResponseEntity.badRequest().body(new SuccessResponse<>(400, "Unsupported file type", null));
    }
}
