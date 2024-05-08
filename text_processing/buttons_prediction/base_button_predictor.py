import abc
from typing import Dict, List


class BaseButtonPredictorModel(abc.ABC):
    @staticmethod
    def clear_text(text: str) -> str:
        return ''.join(el.lower() for el in text if el.isalpha() or el == ' ' or el.isdigit())

    @staticmethod
    def validate_buttons(buttons: List[Dict]):
        assert all("button_id" in el for el in buttons), "All elements must contain 'button_id' field"
        assert all("button_text" in el for el in buttons), "All elements must contain 'button_text' field"
        assert not any("parsed_button_data" in el for el in buttons), \
            "Your dict must not contain 'parsed_button_data' field"

    @abc.abstractmethod
    def predict_button(self, request: str, buttons: List[Dict]) -> Dict:
        """returns button by the given request and buttons"""
