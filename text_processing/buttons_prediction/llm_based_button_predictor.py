from textwrap import dedent
from typing import List, Dict

from text_processing.buttons_prediction.base_button_predictor import BaseButtonPredictorModel
from text_processing.buttons_prediction.word2vec_based_button_predictor import Word2VecButtonPredictorModel


class LlmBasedButtonPredictor(BaseButtonPredictorModel):
    def __init__(self):
        super().__init__()

    def _predict_button_from_3(self, request: str, buttons: List[Dict]) -> Dict:
        buttons_description = ' '.join([f"{i + 1}) {buttons[i]['button_text']}" for i in range(len(buttons))])
        buttons_text = [el["button_text"] for el in buttons]

        request = f"На сайте интернет-банка есть кнопки: {buttons_description}. чтобы {request} надо нажать кнопку"
        prompt = dedent(f"""
        <|user|> 
        На сайте интернет-банка есть кнопки: 1) оформление 2) переводы 3) мой баланс. чтобы посмотреть сколько денег на счете 
        какую надо нажать кнопку? В ответе напиши только текст кнопки
        <|end|>
        <|assistant|>
        мой баланс
        <|end|>
        <|user|> 
        На сайте интернет-банка есть кнопки: 1) оформление 2) переводы 3) мой баланс. чтобы перевести деньги другу какую надо 
        нажать кнопку? В ответе напиши только текст кнопки
        <|end|>
        <|assistant|>
        переводы
        <|end|>
        <|user|> 
        {request}
        <|end|>
        <|assistant|>
        """)

        predicted_button = self.model.predict(prompt)

        if predicted_button in buttons_text:
            for button in buttons:
                if button["button_text"] == predicted_button:
                    return button
            assert "You shall not pass!!!"

        word2vec_model = Word2VecButtonPredictorModel()

        return word2vec_model.predict_button(predicted_button, buttons)

    @staticmethod
    def group_by(arr: List, k: int = 3):
        grouped = []
        for i in range(len(arr)):
            if i % k == 0:
                grouped.append([])
            grouped[-1].append(arr[i])
        return grouped

    def predict_button(self, request: str, buttons: List[Dict]) -> Dict:
        self.validate_buttons(buttons)

        # tmp = deepcopy(buttons)
        # while len(tmp) > 1:
        #     grouped = self.group_by(tmp)
        #     tmp = [self._predict_button_from_3(request, group) for group in grouped]

        # return tmp[0]

        for button in buttons:
            for el in button["button_text"].lower().split():
                if el in request.lower().split():
                    return button

        return buttons[0]
