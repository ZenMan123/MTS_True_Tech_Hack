import io
import json
import os
import ssl
import urllib
import wave
import zipfile
import vosk
import queue
import sounddevice as sd
import soundfile as sf
from text_to_num import alpha2digit
from typing import List


class SpeechToText:
    def __init__(self, model_path="vosk-model-small-ru-0.22", chunk_size=8000):
        self.DEFAULT_MODEL_URL = "https://alphacephei.com/vosk/models/vosk-model-small-ru-0.22.zip"
        self.model_path = model_path
        self._ensure_model_exists()
        self.que = queue.Queue(1)
        vosk.SetLogLevel(0)
        self.model = vosk.Model(model_path)
        self.samplerate = None
        self.rec = None
        self.chunk_size = chunk_size

    def _ensure_model_exists(self) -> None:
        if not os.path.exists(self.model_path):
            print("Модель не найдена, начало загрузки...")
            model_zip_path = os.path.join("vosk-model-small-ru-0.22.zip")
            ssl._create_default_https_context = ssl._create_stdlib_context

            with urllib.request.urlopen(self.DEFAULT_MODEL_URL) as response, \
                    open(model_zip_path, 'wb') as out_file:
                out_file.write(response.read())

            with zipfile.ZipFile(model_zip_path, 'r') as zip_ref:
                zip_ref.extractall()
            os.remove(model_zip_path)
            print("Модель успешно загружена.")

    def _get_samplerate(self, wav_bytes: bytes) -> int:
        return int.from_bytes(wav_bytes[24:28], byteorder='little', signed=False)

    def _convert_to_mono(self, input_file: str, output_file: str) -> None:
        data, samplerate = sf.read(input_file)
        mono_data = data[:, 0]
        sf.write(output_file, mono_data, samplerate)

    def recognize_live_audio(self) -> None:
        self.samplerate = int(sd.query_devices(None, 'input')['default_samplerate'])
        self.rec = vosk.KaldiRecognizer(self.model, self.samplerate)
        with sd.RawInputStream(samplerate=self.samplerate, blocksize=self.chunk_size, dtype='int16',
                               channels=1, callback=lambda *it: self.que.put(bytes(it[0]))):
            print("Начало прослушивания:")
            while True:
                data = self.que.get()
                if self.rec.AcceptWaveform(data):
                    result = json.loads(self.rec.Result())
                    if result["text"]:
                        print(alpha2digit(result["text"], 'ru', ordinal_threshold=0))
                        print("Говори дальше:")

    def recognize_wav_bytes_2(self, wav_bytes: bytes) -> List[str]:
        self.samplerate = self._get_samplerate(wav_bytes)
        self.rec = vosk.KaldiRecognizer(self.model, self.samplerate)
        res = []
        offset = 44
        while offset < len(wav_bytes):
            data = wav_bytes[offset:offset + self.chunk_size]
            offset += self.chunk_size
            if self.rec.AcceptWaveform(data):
                result = json.loads(self.rec.Result())
                if result["text"]:
                    res.append(alpha2digit(result["text"], 'ru', ordinal_threshold=0))
        print(res)
        return res

    def recognize_wav_bytes(self, wav_bytes: bytes) -> List[str]:
        wf = wave.open(io.BytesIO(wav_bytes), "rb")
        self.rec = vosk.KaldiRecognizer(self.model, wf.getframerate())
        res = []
        while True:
            data = wf.readframes(self.chunk_size)
            if len(data) == 0:
                break
            if self.rec.AcceptWaveform(data):
                result = json.loads(self.rec.Result())
                if result["text"]:
                    res.append(alpha2digit(result["text"], 'ru', ordinal_threshold=0))
        print(res)
        return res


if __name__ == '__main__':
    model = SpeechToText()
    # model.recognize_live_audio()
    with open("Test.wav", "rb") as file:
        file_bytes = file.read()
        model.recognize_wav_bytes(file_bytes)
    # offset test:
    #   print(len(file_bytes))
    # with wave.open('Test.wav', 'rb') as wav_file:
    #    print(len(wav_file.readframes(wav_file.getnframes())))
