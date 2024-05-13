package org.example.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.client.MLClient;
import org.example.dto.response.ClassifyResponse;
import org.example.exception.ParseFileException;
import org.example.service.AudioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AudioController {
    private static final String DIALOG_TYPE = "dialog_type";
    private static final String DIALOG_STATE = "dialog_state";

    private static final String IN_DIALOG = "in_dialog";
    private final MLClient mlClient;
    private final AudioService audioService;

    @GetMapping("api/start-dialogue")
    public void startDialogue(HttpSession session) {
        session.setAttribute(IN_DIALOG, true);
    }

    @PostMapping("/api/upload-audio")
    public ResponseEntity<String> handleAudioUpload(@RequestParam("audio") MultipartFile audioFile, HttpSession session) {
        File outFile = audioService.parseFileToWav(audioFile);
        AudioInputStream ais = null;
        try {
            ais = AudioSystem.getAudioInputStream(outFile);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new ParseFileException("Exception while get audioInputStream", e);
        }
        ClassifyResponse classify = mlClient.getClassify(outFile);
        System.out.println(session.getAttribute(DIALOG_TYPE));
        session.setAttribute(DIALOG_TYPE, classify.toString());
        switch (classify.category()) {
            case "PAY_MONEY" -> {
                session.setAttribute(DIALOG_TYPE, "PAY_MONEY");
                /*
                    MultipartFile file = frontend.sendPayMoney()
                    TransferMoneyRequest request = mlClient.
                 */
                if (session.getAttribute(DIALOG_STATE) == null) {
                    mlClient.continueDialogue(outFile, new HashMap<String, String>());
                } else {
                    mlClient.continueDialogue(outFile, (Map<String, String>) session.getAttribute(DIALOG_STATE));
                }
            }
            default -> session.setAttribute(DIALOG_TYPE, "ХУЙ");
        }
        return ResponseEntity.ok(classify.toString());
    }

    @GetMapping("/api/test")
    public String smth(HttpSession session) {
        return (session.getAttribute(DIALOG_TYPE).toString());
    }

    @PostMapping("/api/pay-money")
    public ResponseEntity<String> payMoney(@RequestParam("audio") MultipartFile audioFile, HttpSession session) {
        File outFile = audioService.parseFileToWav(audioFile);
        return ResponseEntity.ok(session.getAttribute(DIALOG_TYPE).toString());
    }
}
