from textwrap import dedent
from typing import Dict, List, Optional


class TaskDialogueModel:
    def __init__(self, dialogue_state: Dict[str, Optional[str]]):
        self.dialogue_state = dialogue_state

    def extract_feature_value(self, request: str, feature: str) -> Optional[str]:
        # prompt = dedent(f"""
        # Найди в этом тексте параметр {feature}: {request}
        # В ответе напиши только параметр {feature}. Если в тексте нет такого параметра, напиши "No such parameter".
        # """)
        # predicted_feature = self.model.predict(prompt, 20).split('\n')[0]

        # if (predicted_feature.lower() == "None" or predicted_feature.lower() == "no such parameter" or
        #         predicted_feature.lower() not in request.lower() or len(predicted_feature) > 16):
        #     return None

        # return predicted_feature
        return request

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


# if __name__ == '__main__':
#     state = {
#         "Номер телефона": None,
#         "Сумма перевода": None,
#         "Банк получателя": None,
#     }
#     dialogue_model = TaskDialogueModel(state, phi_3_model)

#     dialogue_model.update_one_feature("переведи на номер 88001535343", "Номер телефона")
#     print(dialogue_model.get_dialogue_state())

#     dialogue_model.update_one_feature("сумма перевода 300 рублей на банк Тинькофф", "Банк получателя")
#     print(dialogue_model.get_dialogue_state())

#     dialogue_model.update_one_feature("300 рублей", "Сумма перевода")
#     print(dialogue_model.get_dialogue_state())
