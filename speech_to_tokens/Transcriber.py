import os
import tempfile
from whisper.normalizers import BasicTextNormalizer
import numpy as np
from faster_whisper import WhisperModel
# import whisper
# import ssl
from pynput import keyboard
import sounddevice as sd
from scipy.io.wavfile import write

#команду нужно прописать находясь в папке speech_to_tokens
# ct2-transformers-converter --model mitchelldehaven/whisper-medium-ru --output_dir mitchelldehaven/whisper-medium-ru --quantization int8 --force
# команда для скачивания модели, перед этим установить библиотеки сверху
# позже напишу requirments
class Transcriber:
    def __init__(self, model_size="mitchelldehaven/whisper-medium-ru", sample_rate=44100, key=keyboard.Key.space):
        self.model_size = model_size
        self.sample_rate = sample_rate
        self.normalizer = BasicTextNormalizer()
        self.model = WhisperModel(model_size, device="cpu", compute_type="int8")
        # модель конкретно под русский, пока не научился парсить нормально числа
        # ssl._create_default_https_context = ssl._create_unverified_context
        # self.model = whisper.load_model("medium")
        self.isRecording = False
        self.key = key

    def on_press(self, key):
        if key == self.key:
            if not self.isRecording:
                self.isRecording = True
                print("Говорите:")

    def on_release(self, key):
        if key == self.key:
            if self.isRecording:
                self.isRecording = False
                print("\nЗапись прекращена")
                return False

    def process(self):
        track = np.array([], dtype="float64").reshape(0, 1)
        framesPerBuff = self.sample_rate * 10
        with keyboard.Listener(on_press=self.on_press, on_release=self.on_release) as listener:
            while True:
                if self.isRecording:
                    chunk = sd.rec(framesPerBuff, samplerate=self.sample_rate, channels=1, dtype='float64')
                    sd.wait()
                    track = np.vstack([track, chunk])
                if not self.isRecording and len(track) > 0:
                    break
            listener.join()
        sd.play(track, self.sample_rate)
        sd.wait()
        return track

    def saveTemp(self, track):
        temp = tempfile.NamedTemporaryFile(delete=False, suffix=".wav")
        write(temp.name, self.sample_rate, track)
        return temp.name

    def transcribe(self, file):
        segments, _ = self.model.transcribe(
            file,
            language="ru",
            beam_size=5
        )
        segments = list(segments)  # нельзя удалять, не вычисляется пока не проитерируется
        for segment in segments:
            print("[%.2fs -> %.2fs] %s" % (segment.start, segment.end, segment.text))
        os.remove(file)
        return segments

    def run(self):
        print("Оля сделай кнопку")
        while True:
            rec = self.process()
            file = self.saveTemp(rec)
            self.transcribe(file)
            print("\nможно нажать еще раз пробел для записи")


if __name__ == "__main__":
    print(sd.query_devices())
    model = Transcriber()
    model.run()
