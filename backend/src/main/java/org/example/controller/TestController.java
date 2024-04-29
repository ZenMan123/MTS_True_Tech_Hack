package org.example.controller;


import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

@RestController("api/test")
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;

    @GetMapping("hello")
    public String test() {
        return "Hello World";
    }

    @GetMapping("userPN/{phoneNumber}")
    public User findUserByPhoneUser(@PathVariable("phoneNumber") String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    @GetMapping("userID/{id}")
    public User findUserById(@PathVariable("id") Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping("userBalance/{id}")
    public double getUserBalanceById(@PathVariable("id") Long id) {
        return userRepository.getBalanceById(id).orElse(-1.0);
    }

    @PostMapping("userUpd/{id}")
    public void updateUserBalance(@PathVariable("id") Long id, @RequestBody double value) {
        userRepository.updateBalanceById(id, value);
    }

    @PostMapping("userRegister")
    public void registerUser(@RequestBody UserDto user) {
        userRepository.registerUser(user);
    }

    @PostMapping("/upload-audio")
    public String handleAudioUpload(@RequestParam("audio") MultipartFile audioFile) {
        try {
            File tempFile = File.createTempFile(audioFile.getOriginalFilename(), "-temp");
            audioFile.transferTo(tempFile);
            MultimediaObject multimediaObject = new MultimediaObject(tempFile);
            tempFile.delete();
            File outFile = File.createTempFile(audioFile.getOriginalFilename(), ".wav");
            Encoder encoder = new Encoder();
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("wav");
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("pcm_s16le");
            attrs.setAudioAttributes(audio);
            encoder.encode(multimediaObject, outFile, attrs);
            AudioInputStream ais = AudioSystem.getAudioInputStream(outFile);
            //обработка
            return "Аудио успешно воспроизведено!";
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            return "Ошибка воспроизведения аудио: " + e.getMessage();
        } catch (EncoderException e) {
            throw new RuntimeException(e);
        }
    }
}
