import abc
from typing import Dict, List

import nltk
import pymorphy2


class BaseTextModel(abc.ABC):
    def __init__(self):
        if not nltk.data.find("tokenizers/punkt"):
            nltk.download("punkt")
        self.morph = pymorphy2.MorphAnalyzer()

    @staticmethod
    def clear_text(text: str) -> str:
        return ''.join(el.lower() for el in text if el.isalpha() or el == ' ' or el.isdigit())

    def add_part_of_speech(self, word: str) -> str:
        analyze = self.morph.parse(word)[0]
        speech_part = analyze.tag.POS
        normal_form = analyze.normal_form.replace('ё', 'е')

        if speech_part == "INFN":
            speech_part = "VERB"

        return f"{normal_form}_{speech_part}"

    @staticmethod
    def validate_buttons(buttons: List[Dict]):
        assert all("button_id" in el for el in buttons), "All elements must contain 'button_id' field"
        assert all("button_text" in el for el in buttons), "All elements must contain 'button_text' field"
        assert not any("parsed_button_data" in el for el in buttons), \
            "Your dict must not contain 'parsed_button_data' field"

    @abc.abstractmethod
    def predict_button(self, request: str, buttons: List[Dict]) -> Dict:
        """returns button by the given request and buttons"""
