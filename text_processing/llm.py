from typing import List, Union

from transformers import AutoTokenizer, AutoModelForCausalLM
import torch


class LLMModel:
    def __init__(self, model_name: str):
        self.device_map = "mps" if torch.backends.mps.is_available() else "cpu"
        self.model_name = model_name
        self.tokenizer = AutoTokenizer.from_pretrained(
            self.model_name,
            device_map=self.device_map,
        )
        self.model = AutoModelForCausalLM.from_pretrained(
            self.model_name,
            torch_dtype=torch.float16,
            device_map=self.device_map,
            trust_remote_code=True,
            attn_implementation='eager',
        )

    @staticmethod
    def _process_one_output(text: str) -> str:
        return text.strip(' \n').replace("<|endoftext|>", "").replace("<|end|>", "")

    def _process_output(self, data: Union[str, List[str]]) -> Union[str, List[str]]:
        if isinstance(data, str):
            return self._process_one_output(data)
        return [self._process_one_output(text) for text in data]

    def predict(self, data: Union[str, List[str]], max_new_tokens=8) -> Union[str, List[str]]:
        input_ids = self.tokenizer(data, padding=True, return_tensors="pt").input_ids
        input_ids = input_ids.to(self.device_map)

        generation_output = self.model.generate(
            input_ids=input_ids, max_new_tokens=max_new_tokens,
        )

        result = self._process_output(self.tokenizer.batch_decode([
            generation_output[i][len(input_ids[i]):] for i in range(len(input_ids))
        ]))

        if isinstance(data, str):
            return result[0]

        return result


phi_3_model = LLMModel("microsoft/Phi-3-mini-128k-instruct")
