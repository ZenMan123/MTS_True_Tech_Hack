from flask import Flask, request, jsonify
import json

from speech_processing.speech_to_text import SpeechToText
from text_processing.buttons_prediction.llm_based_button_predictor import LlmBasedButtonPredictor
from text_processing.llm import phi_3_model
from text_processing.task_dialogue import TaskDialogueModel

app = Flask(__name__)
speech_to_text_model = SpeechToText()
button_predictor_model = LlmBasedButtonPredictor(phi_3_model)


def get_text_from_audio():
    audio_file = request.files['audio_file']
    audio_file.stream.seek(0)
    audio_bytes = audio_file.read()
    text = ' '.join(speech_to_text_model.recognize_wav_bytes(audio_bytes))
    return text


@app.route('/api/continue_dialogue', methods=["GET"])
def continue_dialogue():
    text = get_text_from_audio()

    dialogue_state = json.loads(request.files["dialogue_state"].read())
    dialogue_model = TaskDialogueModel(dialogue_state, phi_3_model)

    unknown_features = dialogue_model.update_dialogue_state(text)

    result = {
        "status": "OK" if len(unknown_features) == 0 else "UNKNOWN_FEATURES",
        "unknown_features": unknown_features,
        "known_features": dialogue_model.get_dialogue_state(),
    }

    return jsonify(result)


@app.route('/api/update_one_dialogue_feature', methods=["GET"])
def update_one_dialogue_feature():
    text = get_text_from_audio()

    dialogue_state = json.loads(request.files["dialogue_state"].read())
    feature = json.loads(request.files["feature"].read())

    dialogue_model = TaskDialogueModel(dialogue_state, phi_3_model)

    predicted_feature = dialogue_model.update_one_feature(text, feature)

    result = {
        "status": "OK" if predicted_feature else "UNKNOWN_FEATURES",
        "unknown_features": [feature],
        "known_features": dialogue_model.get_dialogue_state(),
    }

    return jsonify(result)


@app.route('/api/choose_button_by_audio', methods=["GET"])
def choose_button_by_audio():
    text = get_text_from_audio()

    buttons_data = json.loads(request.files["json"].read())
    button = button_predictor_model.predict_button(text, buttons_data)

    result = {
        "status": "OK",
        "recommended_button_id": button["button_id"],
    }

    return jsonify(result)


if __name__ == '__main__':
    app.run()
