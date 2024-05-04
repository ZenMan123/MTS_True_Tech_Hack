from copy import deepcopy
from textwrap import dedent
from typing import List, Dict

import nltk
from transformers import AutoTokenizer, AutoModelForCausalLM
import torch

from text_processing.base_text_model import BaseTextModel
from text_processing.word2vec import Word2VecModel


class LLMModel(BaseTextModel):
    def __init__(self):
        self.model_name = "microsoft/Phi-3-mini-128k-instruct"
        self.tokenizer = AutoTokenizer.from_pretrained(self.model_name, device_map="mps")
        self.model = AutoModelForCausalLM.from_pretrained(
            self.model_name,
            torch_dtype=torch.float16,
            device_map='mps',
            trust_remote_code=True
        )
        super().__init__()


    def _predict_button_from_3(self, request: str, buttons: List[Dict]) -> Dict:
        buttons_description = ' '.join([f"{i + 1}) {buttons[i]['button_text']}" for i in range(len(buttons))])
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
        buttons_text = [el["button_text"] for el in buttons]

        input_ids = self.tokenizer(prompt, return_tensors="pt").input_ids
        input_ids = input_ids.to("mps")

        generation_output = self.model.generate(
            input_ids=input_ids, max_new_tokens=8,
        )
        phrase = self.tokenizer.decode(generation_output[0][len(input_ids[0]):])
        predicted_button = phrase.strip(' ').strip('\n').strip("<|end|>")

        if predicted_button in buttons_text:
            for button in buttons:
                if button["button_text"] == predicted_button:
                    return button
            assert "You shall not pass!!!"

        word2vec_model = Word2VecModel()

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

        tmp = deepcopy(buttons)
        while len(tmp) > 1:
            grouped = self.group_by(tmp)
            print(grouped)
            tmp = [self._predict_button_from_3(request, group) for group in grouped]

        return tmp[0]


if __name__ == '__main__':
    res = LLMModel().predict_button(
        "взять кредит",
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
