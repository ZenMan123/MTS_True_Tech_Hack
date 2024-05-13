package org.example.client;

import org.example.dto.response.ClassifyResponse;
import org.example.dto.response.ContinueDialogue;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Map;

@Component
public class MLClient {
    private static final String BASE_URL = "http://localhost:5000/%s";
    private final RestTemplate restTemplate = new RestTemplate();

    public ClassifyResponse getClassify(File file) {
        MultiValueMap<String, Object> body;
        body = new LinkedMultiValueMap<>();
        body.add("audio_file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body);
        System.out.println(requestEntity.getHeaders() + " " + requestEntity.getBody());
        ResponseEntity<ClassifyResponse> response = restTemplate.postForEntity(
                String.format(BASE_URL, "api/classify_request"),
                requestEntity,
                ClassifyResponse.class);
        return response.getBody();
    }

    public ContinueDialogue continueDialogue(File file, Map<String, String> dialogueState) {
        MultiValueMap<String, Object> body;
        body = new LinkedMultiValueMap<>();
        body.add("audio_file", new FileSystemResource(file));
        body.add("dialogue_state", dialogueState);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body);
        ResponseEntity<ContinueDialogue> response = restTemplate.postForEntity(
                String.format(BASE_URL, "api/continue_dialogue"),
                requestEntity,
                ContinueDialogue.class);
        return response.getBody();
    }
}
