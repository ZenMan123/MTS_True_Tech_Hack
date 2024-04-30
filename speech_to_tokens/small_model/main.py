import vosk
import queue
import sounddevice as sd

que = queue.Queue(1)
vosk.SetLogLevel(0)
model = vosk.Model("vosk-model-small-ru-0.4")
samplerate = int(sd.query_devices(None, 'input')['default_samplerate'])
rec = vosk.KaldiRecognizer(model, samplerate)

with sd.RawInputStream(samplerate=samplerate, blocksize=8000, dtype='int16',
                       channels=1, callback=lambda *it: que.put(bytes(it[0]))):
    data = []
    while True:
        data = que.get()
        if rec.AcceptWaveform(data):
            print(rec.FinalResult())
