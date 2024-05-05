from flask import Flask, request, jsonify
import json

from speech_processing.speech_to_text import SpeechToText
from text_processing.buttons_prediction.word2vec_based_button_predictor import Word2VecButtonPredictorModel

app = Flask(__name__)
speech_to_text_model = SpeechToText()
text_model = Word2VecButtonPredictorModel()


@app.route('/api/choose_button_by_audio', methods=["GET"])
def choose_button_by_audio():
    audio_file = request.files['audio_file']
    audio_file.stream.seek(0)
    audio_bytes = audio_file.read()
    text = ' '.join(speech_to_text_model.recognize_wav_bytes(audio_bytes))

    buttons_data = json.loads(request.files["json"].read())
    button = text_model.predict(text, buttons_data)

    result = {
        "status": "OK",
        "recommended_button_id": button["button_id"],
    }

    return jsonify(result)


if __name__ == '__main__':
    app.run()
