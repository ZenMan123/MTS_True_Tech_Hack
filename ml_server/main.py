from flask import Flask, request, jsonify
import json

app = Flask(__name__)


@app.route('/api/choose_button_by_audio', methods=["GET"])
def choose_button_by_audio():
    audio_file = request.files['audio_file']
    audio_file.stream.seek(0)
    audio_bytes = audio_file.read()

    content = json.loads(request.files["json"].read())

    result = {
        "status": "OK",
        "recommended_button_id": 1,
    }

    return jsonify(result)


if __name__ == '__main__':
    app.run()
