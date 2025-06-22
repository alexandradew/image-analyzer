package io.github.alexandradew.image_analyzer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LlmService {

    @Value("${llmdata.textmodel}")
    private String llmtextmodel;

    @Value("${llmdata.imagemodel}")
    private String llmimagemodel;

    @Value("${llmdata.url}")
    private String llmurl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String askLlm(String extractedText, String question) {
        String fullPrompt = "Document content:\n" + extractedText + "\n\nQuestion:\n" + question;

        Map<String, Object> request = new HashMap<>();
        request.put("model", llmtextmodel);
        request.put("prompt", fullPrompt);
        request.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                llmurl, entity, Map.class
        );

        if (response.getBody() == null || response.getBody().get("response") == null) {
            return "No response from LLM";
        }
        return response.getBody().get("response").toString();
    }

    public String describeImage(File imageFile, String prompt) throws IOException {
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        Map<String, Object> request = new HashMap<>();
        request.put("model", llmimagemodel);
        request.put("prompt", prompt);
        request.put("images", List.of(base64Image));
        request.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                llmurl, entity, Map.class
        );

        if (response.getBody() == null || response.getBody().get("response") == null) {
            return "No response from LLM";
        }
        return response.getBody().get("response").toString();
    }
}

