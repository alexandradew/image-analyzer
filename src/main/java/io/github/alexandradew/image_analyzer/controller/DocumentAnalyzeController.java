package io.github.alexandradew.image_analyzer.controller;

import java.io.File;
import java.io.IOException;

import io.github.alexandradew.image_analyzer.dto.SuccessResponse;
import io.github.alexandradew.image_analyzer.service.LlmService;
import io.github.alexandradew.image_analyzer.service.OcrService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/document")
public class DocumentAnalyzeController {

    OcrService ocrService;
    LlmService llmService;

    public DocumentAnalyzeController(OcrService ocrService, LlmService llmService) {
        this.ocrService = ocrService;
        this.llmService = llmService;
    }

    @PostMapping("/extract")
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
            String extractedText = ocrService.extractFromImage(tempFile);
            return ResponseEntity.ok(new SuccessResponse<>(200, "Image analyzed successfully", llmService.askLlm(extractedText, prompt)));
        }

        if (contentType != null && contentType.equals("application/pdf")) {
            String extractedText = ocrService.extractFromPdf(tempFile);
            return ResponseEntity.ok(new SuccessResponse<>(200, "Pdf analyzed successfully", llmService.askLlm(extractedText, prompt)));
        }

        return ResponseEntity.badRequest().body(new SuccessResponse<>(400, "Unsupported file type", null));
    }


}

