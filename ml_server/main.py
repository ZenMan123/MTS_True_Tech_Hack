from flask import Flask, request, jsonify
import json

from speech_processing.speech_to_text import SpeechToText
from text_processing.buttons_prediction.llm_based_button_predictor import LlmBasedButtonPredictor
# from text_processing.llm import phi_3_model
from text_processing.task_dialogue import TaskDialogueModel
from text_processing.text_classifier.text_classifier import TextClassifier

app = Flask(__name__)
speech_to_text_model = SpeechToText()
button_predictor_model = LlmBasedButtonPredictor()
text_classify_model = TextClassifier()


def get_text_from_audio():
    audio_file = request.files['audio_file']
    audio_file.stream.seek(0)
    audio_bytes = audio_file.read()
    text = ' '.join(speech_to_text_model.recognize_wav_bytes(audio_bytes))
    return text


@app.route('/api/classify_request', methods=["POST"])
def classify_request():
    text = get_text_from_audio()

    category = text_classify_model.classify_feedback(text)

    result = {
        "status": "OK",
        "category": category,
    }

    return jsonify(result)


@app.route('/api/continue_dialogue', methods=["POST"])
def continue_dialogue():
    text = get_text_from_audio()

    dialogue_state = json.loads(request.form["dialogue_state"])
    dialogue_model = TaskDialogueModel(dialogue_state)

    dialogue_model.update_dialogue_state(text)

    result = {
        "status": "OK",
        "dialogue_state": dialogue_model.get_dialogue_state(),
    }

    return jsonify(result)


@app.route('/api/update_one_dialogue_feature', methods=["POST"])
def update_one_dialogue_feature():
    text = get_text_from_audio()
    print(request.form)
    dialogue_state = json.loads(request.form["dialogue_state"])
    feature = request.form["feature"]

    dialogue_model = TaskDialogueModel(dialogue_state)

    predicted_feature = dialogue_model.update_one_feature(text, feature)

    result = {
        "status": "OK" if predicted_feature else "UNKNOWN_FEATURES",
        "dialogue_state": dialogue_model.get_dialogue_state(),
        "predicted_feature": predicted_feature,
    }

    return jsonify(result)


@app.route('/api/choose_button_by_audio', methods=["POST"])
def choose_button_by_audio():
    text = get_text_from_audio()

    buttons_data = json.loads(request.form["json"])
    button = button_predictor_model.predict_button(text, buttons_data)

    result = {
        "status": "OK",
        "recommended_button_id": button["button_id"],
    }

    return jsonify(result)


if __name__ == '__main__':
    app.run(debug=True)
