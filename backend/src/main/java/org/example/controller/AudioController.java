package org.example.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.MLClient;
import org.example.dto.response.ClassifyResponse;
import org.example.dto.response.UpdateFeature;
import org.example.exception.ParseFileException;
import org.example.service.AudioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AudioController {
    private static final String DIALOG_TYPE = "dialog_type";
    private static final String DIALOG_STATE = "dialog_state";

    private static final String IN_DIALOG = "in_dialog";
    public static final String REQUESTED_FEATURE = "requested_feature";
    private final MLClient mlClient;
    private final AudioService audioService;

    @GetMapping("api/start-dialogue")
    public void startDialogue(HttpSession session) {
        session.setAttribute(IN_DIALOG, true);
    }

    @PostMapping("/api/upload-audio")
    @CrossOrigin
    public ResponseEntity<Map<String,String>> handleAudioUpload(@RequestParam("audio") MultipartFile audioFile, HttpSession session,
                                                    @RequestParam("buttonList") String buttons
    ) throws IOException {
        File outFile = audioService.parseFileToWav(audioFile);
        AudioInputStream ais = null;
        try {
            ais = AudioSystem.getAudioInputStream(outFile);
            //audioService.playSound(ais);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new ParseFileException("Exception while get audioInputStream", e);
        }
        log.info("Buttons {}", buttons);
        Map<String, String> response = new HashMap<>(Map.of("answer", "ok"));
        if (session.getAttribute(IN_DIALOG) == null) {
            session.setAttribute(IN_DIALOG, true);
            ClassifyResponse classify = mlClient.getClassify(outFile);
            session.setAttribute(DIALOG_TYPE, classify.toString());
            log.info("Classify, {}", classify);
            switch (classify.category()) {
                case "PAY_MONEY" -> {
                    session.setAttribute(DIALOG_TYPE, "PAY_MONEY");
                    Map<String, String> dialogueState = new HashMap<>();
                    dialogueState.put("номер телефона", null);
                    dialogueState.put("сумма перевода", null);
                    dialogueState.put("банк получателя", null);
                    session.setAttribute(DIALOG_STATE, dialogueState);
                    session.setAttribute(REQUESTED_FEATURE, "номер телефона");
                    /* frontend вернуть текст 'уточните номер телефона' */
                    response.replace("answer", "уточните номер телефона");
                }
                case "CHECK_BALANCE" -> {
                    session.setAttribute(DIALOG_TYPE, "CHECK_BALANCE");
                /*
                       frontend.sendBalance()
                 */
                }
                default -> session.setAttribute(DIALOG_TYPE, "ХУЙ");
            }
        } else {
            Map<String, String> dialogueState = (Map<String, String>) session.getAttribute(DIALOG_STATE);
            String feature = (String) session.getAttribute(REQUESTED_FEATURE);
            UpdateFeature updateFeature = mlClient.updateOneFeature(outFile, dialogueState, feature);
            Map<String, String> updated = updateFeature.dialogueState();
            session.setAttribute(DIALOG_STATE, updated);
            for (String key : updated.keySet()) {
                if (updated.get(key) == null) {
                    session.setAttribute(REQUESTED_FEATURE, key);
                }
            }

        }
        //Files.delete(outFile.toPath());
        return ResponseEntity.ok(response);
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
