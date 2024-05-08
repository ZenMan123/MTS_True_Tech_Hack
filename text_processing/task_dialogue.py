from textwrap import dedent
from typing import Dict, List, Optional

from text_processing.llm import LLMModel, phi_3_model


class TaskDialogueModel:
    def __init__(self, dialogue_state: Dict[str, Optional[str]], model: LLMModel):
        self.dialogue_state = dialogue_state
        self.model = model

    def extract_feature_value(self, request: str, feature: str) -> Optional[str]:
        prompt = dedent(f"""
        <|user|> 
        Найди в этом тексте параметр номер телефона: "номер телефона 88005353535". 
        В ответе напиши только параметр номер телефона. Если в тексте нет такого параметра, напиши "No such parameter".
        <|end|>
        <|assistant|>
        88005353535
        <|end|>
        <|user|> 
        Найди в этом тексте параметр номер телефона: "сумма перевода 5000". 
        В ответе напиши только параметр номер телефона. Если в тексте нет такого параметра, напиши "No such parameter".  
        <|end|>
        <|assistant|>
        No such parameter
        <|end|>
        <|user|> 
        Найди в этом тексте параметр номер телефона: "8917123456". 
        В ответе напиши только параметр номер телефона. Если в тексте нет такого параметра, напиши "No such parameter".    
        <|end|>
        <|assistant|>
        8917123456
        <|end|>
        <|user|> 
        Найди в этом тексте параметр сумма перевода: "перевести на номер 89174014543525". 
        В ответе напиши только параметр номер телефона. Если в тексте нет такого параметра, напиши "No such parameter".    
        <|end|>
        <|assistant|>
        No such parameter
        <|end|>
        <|user|> 
        Найди в этом тексте параметр сумма перевода: "перевести 5000 рублей". 
        В ответе напиши только параметр сумма перевода. Если в тексте нет такого параметра, напиши "No such parameter".
        <|end|>
        <|assistant|>
        5000
        <|end|>
        <|user|> 
        Найди в этом тексте параметр сумма перевода: "100500 рублей". 
        В ответе напиши только параметр сумма перевода. Если в тексте нет такого параметра, напиши "No such parameter".
        <|end|>
        <|assistant|>
        100500
        <|end|>
        <|user|>
        Найди в этом тексте параметр {feature}: {request}
        В ответе напиши только параметр {feature}. Если в тексте нет такого параметра, напиши "No such parameter".
        <|end|>
        <|assistant|>
        """)
        predicted_feature = self.model.predict(prompt, 20).split('\n')[0]

        if (predicted_feature.lower() == "None" or predicted_feature.lower() == "no such parameter" or
                predicted_feature.lower() not in request.lower() or len(predicted_feature) > 16):
            return None

        return predicted_feature

    def update_dialogue_state(self, request: str) -> List[str]:
        unknown_features = []
        for key, value in self.dialogue_state.items():
            if value is not None:
                continue
            predicted_value = self.extract_feature_value(request, key)
            if predicted_value:
                self.dialogue_state[key] = predicted_value
            else:
                unknown_features.append(key)
        return unknown_features

    def update_one_feature(self, request: str, feature: str) -> Optional[str]:
        predicted_value = self.extract_feature_value(request, feature)
        self.dialogue_state[feature] = predicted_value
        return predicted_value

    def get_dialogue_state(self) -> Dict[str, Optional[str]]:
        return self.dialogue_state


if __name__ == '__main__':
    state = {
        "Номер телефона": None,
        "Сумма перевода": None,
        "Банк получателя": None,
    }
    dialogue_model = TaskDialogueModel(state, phi_3_model)

    dialogue_model.update_dialogue_state("переведи на номер 88001535343")
    print(dialogue_model.get_dialogue_state())

    dialogue_model.update_dialogue_state("сумма перевода 300 рублей на банк Тинькофф")
    print(dialogue_model.get_dialogue_state())

    dialogue_model.update_dialogue_state("300 рублей")
    print(dialogue_model.get_dialogue_state())
