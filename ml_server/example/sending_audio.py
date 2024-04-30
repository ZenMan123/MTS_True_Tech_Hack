import requests
import json


def send_audio():
    with open('Артефакт Макаки.mp3', 'rb') as file:
        files = {'audio_file': file, "json": json.dumps({"some_data": ["data", "one", "two"]})}
        url = 'http://127.0.0.1:5000/api/choose_button_by_audio'
        req = requests.get(url, files=files)

    print(req.status_code)
    print(req.text)


send_audio()
