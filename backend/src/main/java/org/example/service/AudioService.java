package org.example.service;

import org.example.exception.ParseFileException;
import org.example.exception.PlaySoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.IOException;

@Service
public class AudioService {
    public File parseFileToWav(MultipartFile audioFile) {
        try {
            File tempFile = File.createTempFile(audioFile.getOriginalFilename(), "-temp");
            audioFile.transferTo(tempFile);
            MultimediaObject multimediaObject = new MultimediaObject(tempFile);
            File outFile = new File("out_" + audioFile.getOriginalFilename());
            Encoder encoder = new Encoder();
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("wav");
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("pcm_s16le");
            attrs.setAudioAttributes(audio);
            encoder.encode(multimediaObject, outFile, attrs);
            return outFile;
        } catch (EncoderException | IOException e) {
            throw new ParseFileException("Exception while parse file", e);
        }
    }

    public void playSound(AudioInputStream ais) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.setFramePosition(0);
            clip.start();
            Thread.sleep(clip.getMicrosecondLength() / 1000);
            clip.stop();
            clip.close();
        } catch (LineUnavailableException | IOException | InterruptedException e) {
            throw new PlaySoundException("Exception while try to play sound", e);
        }
    }
}
