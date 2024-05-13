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
from speech_processing.config import *


class SpeechToText:
    def __init__(self):
        self.absolute_path = MODEL_DIR + "/" + MODEL_NAME
        self._ensure_model_exists()
        self.que = queue.Queue(1)
        vosk.SetLogLevel(-1)
        self.model = vosk.Model(self.absolute_path)
        self.samplerate = None
        self.rec = None

    def _ensure_model_exists(self) -> None:
        if not os.path.exists(self.absolute_path):
            os.makedirs(os.path.dirname(self.absolute_path), exist_ok=True)
            print("Модель не найдена, начало загрузки...")
            model_zip_path = os.path.join(MODEL_DIR, MODEL_NAME_ZIP)
            ssl._create_default_https_context = ssl._create_stdlib_context

            with urllib.request.urlopen(DEFAULT_MODEL_URL) as response, \
                    open(model_zip_path, 'wb') as out_file:
                out_file.write(response.read())

            with zipfile.ZipFile(model_zip_path, 'r') as zip_ref:
                zip_ref.extractall(os.path.dirname(self.absolute_path))
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
        with sd.RawInputStream(samplerate=self.samplerate, blocksize=CHUNK_SIZE, dtype='int16',
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
            data = wav_bytes[offset:offset + CHUNK_SIZE]
            offset += CHUNK_SIZE
            if self.rec.AcceptWaveform(data):
                result = json.loads(self.rec.Result())
                if result["text"]:
                    res.append(alpha2digit(result["text"], 'ru', ordinal_threshold=0))
        print(res)
        return res

    def recognize_wav_bytes(self, wav_bytes: bytes) -> List[str]:
        wf = wave.open(io.BytesIO(wav_bytes), "rb")
        self.rec = vosk.KaldiRecognizer(self.model, wf.getframerate())
        self.rec.SetWords(True)
        res = []
        prev = ""
        while True:
            data = wf.readframes(CHUNK_SIZE)
            if len(data) == 0:
                break
            if self.rec.AcceptWaveform(data):
                result = json.loads(self.rec.Result())
                res.append(alpha2digit(result["text"], 'ru', ordinal_threshold=0))
            else:
                temp_txt_dict = json.loads(self.rec.PartialResult())
                curr = temp_txt_dict['partial']
                if len(curr) > 0:
                    prev = curr
        if prev and len(res) == 0:
            res.append(alpha2digit(prev, 'ru', ordinal_threshold=0))
        # print(res)
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
