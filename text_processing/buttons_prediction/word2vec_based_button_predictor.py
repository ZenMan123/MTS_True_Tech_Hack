from typing import List, Dict

import gensim.downloader
import pymorphy2
from gensim.models import KeyedVectors
import nltk

from text_processing.buttons_prediction.base_button_predictor import BaseButtonPredictorModel
from text_processing.config import RUSSIAN_WORD2VEC_GENSIM_PATH, RUSSIAN_WORD2VEC_LOCAL_PATH


class Word2VecButtonPredictorModel(BaseButtonPredictorModel):
    def __init__(self):
        # Loading necessary data
        try:
            self.word2vec_model = KeyedVectors.load(RUSSIAN_WORD2VEC_LOCAL_PATH)
        except FileNotFoundError:
            self.word2vec_model = gensim.downloader.load(RUSSIAN_WORD2VEC_GENSIM_PATH)
            self.word2vec_model.save(RUSSIAN_WORD2VEC_LOCAL_PATH)

        if not nltk.data.find("tokenizers/punkt"):
            nltk.download("punkt")
        self.morph = pymorphy2.MorphAnalyzer()

        super().__init__()

    def add_part_of_speech(self, word: str) -> str:
        analyze = self.morph.parse(word)[0]
        speech_part = analyze.tag.POS
        normal_form = analyze.normal_form.replace('ё', 'е')

        if speech_part == "INFN":
            speech_part = "VERB"

        return f"{normal_form}_{speech_part}"

    def predict_button(self, request: str, buttons: List[Dict]) -> Dict:
        self.validate_buttons(buttons)

        words = [(self.add_part_of_speech(word), word) for word in nltk.word_tokenize(self.clear_text(request))]

        parsed_buttons = []
        for button in buttons:
            parsed_words = []
            for word in nltk.word_tokenize(self.clear_text(button["button_text"])):
                parsed_words.append(self.add_part_of_speech(word))
            parsed_buttons.append((parsed_words, button))

        best_button, best_similarity = parsed_buttons[0][1], 0
        for parsed_button in parsed_buttons:
            max_similarity = 0

            for button_word in parsed_button[0]:
                for word in words:
                    try:
                        similarity = self.word2vec_model.similarity(word[0], button_word)
                    except KeyError:
                        similarity = 0
                    max_similarity = max(max_similarity, similarity)

                if max_similarity > best_similarity:
                    best_similarity = max_similarity
                    best_button = parsed_button[1]

        return best_button


if __name__ == '__main__':
    res = Word2VecButtonPredictorModel().predict_button(
        "посмотреть свой счет",
        [
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
    )
    print(res)
