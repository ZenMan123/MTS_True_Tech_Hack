package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.exception.ParseFileException;
import org.example.service.AudioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AudioController {
    private final AudioService audioService;

    @PostMapping("/api/upload-audio")
    public ResponseEntity<String> handleAudioUpload(@RequestParam("audio") MultipartFile audioFile) {
        File outFile = audioService.parseFileToWav(audioFile);
        AudioInputStream ais = null;
        try {
            ais = AudioSystem.getAudioInputStream(outFile);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new ParseFileException("Exception while get audioInputStream", e);
        }
        audioService.playSound(ais);
        //TODO: MLService.sendMessage()
        return ResponseEntity.ok("Аудио успешно воспроизведено!");
    }
}
