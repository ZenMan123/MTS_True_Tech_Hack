package org.example.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.MLClient;
import org.example.dto.response.ClassifyResponse;
import org.example.dto.response.UpdateFeature;
import org.example.exception.ParseFileException;
import org.example.service.AudioService;
import org.example.service.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
@Scope("session")
public class AudioController {
    private final UserService userService;
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

    //    @PostMapping("/api/upload-audio")
//    @CrossOrigin
//    @SuppressWarnings("ALL")
//    public ResponseEntity<Map<String, String>> handleAudioUpload(
//            @RequestParam("audio") MultipartFile audioFile, HttpSession session,
//            @RequestParam("buttonList") String buttons,
//            @RequestParam("id") Long id
//
//    ) {
//        if (session.getAttribute("x") == null) {
//            session.setAttribute("x", 5);
//        }
//        return ResponseEntity.ok(Map.of("text_to_speak", "Вы уволены"));
//    }
    @PostMapping("/api/upload-audio")
    @CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
    @SuppressWarnings("ALL")
    public ResponseEntity<Map<String, String>> handleAudioUpload(@RequestParam("audio") MultipartFile audioFile,
                                                                 HttpSession session,
                                                                 @RequestParam("buttonList") String buttons,
                                                                 @RequestParam("id") Long id
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
        Map<String, String> response = new HashMap<>(Map.of("text_to_speak", "ok"));
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
                    response.replace("text_to_speak", "уточните номер телефона");
                }
                case "CHECK_BALANCE" -> {
                    session.setAttribute(DIALOG_TYPE, "CHECK_BALANCE");
                    response.replace("text_to_speak", "Ваш баланс " + userService.getBalance(id));
                }
                case "PAY_BILLS" -> {
                    session.setAttribute(DIALOG_TYPE, "PAY_BILLS");
                    Map<String, String> dialogueState = new HashMap<>();
                    dialogueState.put("номер телефона", null);
                    dialogueState.put("сумма перевода", null);
                    dialogueState.put("банк получателя", null);
                    session.setAttribute(DIALOG_STATE, dialogueState);
                    session.setAttribute(REQUESTED_FEATURE, "номер телефона");
                    response.replace("text_to_speak", "уточните номер телефона");
                    break;
                }
                case "GOTO_BUTTON" -> {

                }
                default -> session.setAttribute(DIALOG_TYPE, "UNKNOWN_STATE");
            }
        } else {
            Map<String, String> dialogueState = (Map<String, String>) session.getAttribute(DIALOG_STATE);
            String feature = (String) session.getAttribute(REQUESTED_FEATURE);
            mlClient.getClassify(outFile);
            UpdateFeature updateFeature = mlClient.updateOneFeature(outFile, dialogueState, feature);
            Map<String, String> updated = (Map<String, String>) session.getAttribute(DIALOG_STATE);
            if (Objects.equals(updateFeature.status(), "OK")) {
                updated = updateFeature.dialogueState();
                session.setAttribute(DIALOG_STATE, updated);
            }
            boolean all = true;
            for (String key : updated.keySet()) {
                if (updated.get(key) == null) {
                    all = false;
                    session.setAttribute(REQUESTED_FEATURE, key);
                    response.put("text_to_speak", "уточните " + key);
                }
            }
            if (all) {
                response.put("text_to_speak", toText(session));
                session.setAttribute(IN_DIALOG, null);
                session.setAttribute(DIALOG_STATE, null);
                session.setAttribute(DIALOG_TYPE, null);
                session.setAttribute(REQUESTED_FEATURE, null);
            }
        }
        //Files.delete(outFile.toPath());
        return ResponseEntity.ok(response);
    }


    private String toText(HttpSession session) {
        String dialogueType = (String) session.getAttribute(DIALOG_TYPE);
        Map<String, String> dialogueState = (Map<String, String>) session.getAttribute(DIALOG_STATE);
        StringBuilder sb = new StringBuilder();
        sb.append("Тип операции: ").append(typeToSpeak(dialogueType)).append(". ");
        sb.append("Заполненные поля: ");
        for (Map.Entry<String, String> entry : dialogueState.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        return sb.toString();
    }

    private String typeToSpeak(String type) {
        return switch (type) {
            case "PAY_MONEY" -> "Перевод денег";
            case "PAY_BILL" -> "Оплата платежа";
            case "CHECK_BALANCE" -> "Проверка баланса";
            case "GOTO_BUTTON" -> "Переход по кнопке";
            default -> "Неизвестная операция";
        };
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

    @GetMapping("/api/test1")
    @CrossOrigin
    public String test(HttpSession session) {
        Object prev = session.getAttribute("test");
        session.setAttribute("test", "Hello");
        return prev == null ? "null" : prev.toString();
    }

    @GetMapping("/api/test2")
    @CrossOrigin
    public String test2(HttpSession session) {
        return (String) session.getAttribute("test");
    }
}
