import requests
import json


def send_audio():
    buttons_data = [
        {
            "button_text": "оформление",
            "button_id": 1,
        },
        {
            "button_text": "переводы",
            "button_id": 2,
        },
        {
            "button_text": "мой баланс",
            "button_id": 3,
        }
    ]
    with open('Test.wav', 'rb') as file:
        files = {'audio_file': file, "json": json.dumps(buttons_data)}
        url = 'http://127.0.0.1:5000/api/choose_button_by_audio'
        req = requests.get(url, files=files)

    print(req.text)


send_audio()
