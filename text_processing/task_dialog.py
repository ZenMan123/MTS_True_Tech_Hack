from textwrap import dedent
from typing import Dict, List, Optional

from text_processing.llm import LLMModel


class TaskDialogModel:
    def __init__(self, dialog_state: Dict, model: LLMModel):
        self.dialog_state = dialog_state
        self.model = model

    def extract_feature_value(self, request: str, feature: str) -> Optional[str]:
        prompt = dedent(f"""
        <|user|> 
        Найди в этом тексте параметр номер телефона: "номер телефона 88005353535". 
        В ответе напиши только параметр номер телефона. Если такого параметра нет, напиши "None".
        <|end|>
        <|assistant|>
        88005353535
        <|end|>
        <|user|> 
        Найди в этом тексте параметр номер телефона: "сумма перевода 51481834514". 
        В ответе напиши только параметр номер телефона. Если такого параметра нет, напиши "None".        
        <|end|>
        <|assistant|>
        None
        <|end|>
        <|user|> 
        Найди в этом тексте параметр номер телефона: "8917123456". 
        В ответе напиши только параметр номер телефона. Если такого параметра нет, напиши "None".        
        <|end|>
        <|assistant|>
        8917123456
        <|end|>
        <|user|> 
        Найди в этом тексте параметр сумма перевода: "перевести 5000 рублей". 
        В ответе напиши только параметр сумма перевода. Если такого параметра нет, напиши "None".    
        <|end|>
        <|assistant|>
        5000
        <|end|>
        <|user|>
        Найди в этом тексте параметр {feature}: {request}
        В ответе напиши только параметр {feature}. Если такого параметра нет, напиши "None".
        <|end|>
        <|assistant|>
        """)
        predicted_feature = self.model.predict(prompt)
        if predicted_feature.lower() == "None":
            return None

        return predicted_feature

    def update_dialog_state(self, request: str) -> List[str]:
        unknown_features = []
        for key, value in self.dialog_state.items():
            if value is not None:
                continue
            predicted_value = self.extract_feature_value(request, key)
            if predicted_value:
                self.dialog_state[key] = value
            else:
                unknown_features.append(key)
        return unknown_features
