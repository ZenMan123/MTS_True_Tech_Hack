package org.example.client;

import org.example.dto.response.ClassifyResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class MLClient {

    public ClassifyResponse getClassify(File file) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> body;
        body = new LinkedMultiValueMap<>();
        body.add("audio_file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body);
        System.out.println(requestEntity.getHeaders() + " " + requestEntity.getBody());
        ResponseEntity<ClassifyResponse> response = restTemplate.postForEntity("http://localhost:5000/api/classify_request",
                requestEntity,
                ClassifyResponse.class);
        return response.getBody();
    }
}
