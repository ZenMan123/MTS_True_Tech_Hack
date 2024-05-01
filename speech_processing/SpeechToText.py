import json
import os
import wave
import vosk
import queue
import sounddevice as sd
import soundfile as sf
from text_to_num import alpha2digit


class SpeechToText:
    def __init__(self, model_path="vosk-model-small-ru-0.4"):
        self.que = queue.Queue(1)
        vosk.SetLogLevel(0)
        self.model = vosk.Model(model_path)
        self.samplerate = None
        self.rec = None

    def _convert_to_mono(input_file, output_file):
        data, samplerate = sf.read(input_file)
        mono_data = data[:, 0]
        sf.write(output_file, mono_data, samplerate)

    def recognize_live_audio(self):
        self.samplerate = int(sd.query_devices(None, 'input')['default_samplerate'])
        self.rec = vosk.KaldiRecognizer(self.model, self.samplerate)
        with sd.RawInputStream(samplerate=self.samplerate, blocksize=8000, dtype='int16',
                               channels=1, callback=lambda *it: self.que.put(bytes(it[0]))):
            print("Начало прослушивания:")
            while True:
                data = self.que.get()
                if self.rec.AcceptWaveform(data):
                    result = json.loads(self.rec.Result())
                    if result["text"]:
                        print(alpha2digit(result["text"], 'ru', ordinal_threshold=0))
                        print("Говори дальше:")

    def recognize_wav_file(self, wav_file_path):
        wf = wave.open(wav_file_path, "rb")
        self.rec = vosk.KaldiRecognizer(self.model, wf.getframerate())
        res = []
        while True:
            data = wf.readframes(8000)
            if len(data) == 0:
                break
            if self.rec.AcceptWaveform(data):
                result = json.loads(self.rec.Result())
                if result["text"]:
                    res.append(alpha2digit(result["text"], 'ru', ordinal_threshold=0))
        print(res)
        #os.remove(wav_file_path) удаление файла после использования
        return res


if __name__ == '__main__':
    model = SpeechToText()
    # model.recognize_live_audio()
    model.recognize_wav_file("Test.wav")
