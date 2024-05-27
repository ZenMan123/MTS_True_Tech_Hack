package org.example.client;

import org.example.dto.response.ButtonResponse;
import org.example.dto.response.ClassifyResponse;
import org.example.dto.response.UpdateFeature;
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

    public UpdateFeature updateOneFeature(File file, Map<String, String> dialogueState,
                                          String feature) {
        MultiValueMap<String, Object> body;
        body = new LinkedMultiValueMap<>();
        body.add("audio_file", new FileSystemResource(file));
        body.add("dialogue_state", dialogueState);
        body.add("feature", feature);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body);
        ResponseEntity<UpdateFeature> response = restTemplate.postForEntity(
                String.format(BASE_URL, "api/update_one_dialogue_feature"),
                requestEntity,
                UpdateFeature.class);
        return response.getBody();
    }

    public ButtonResponse chooseButton(File file, String buttons) {
        MultiValueMap<String, Object> body;
        body = new LinkedMultiValueMap<>();
        body.add("audio_file", new FileSystemResource(file));
        body.add("json", buttons);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body);
        ResponseEntity<ButtonResponse> response = restTemplate.postForEntity(
                String.format(BASE_URL, "api/choose_button_by_audio"),
                requestEntity,
                ButtonResponse.class);
        return response.getBody();
    }
}
