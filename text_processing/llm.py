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

    def predict(self, text: str) -> str:
        input_ids = self.tokenizer(text, return_tensors="pt").input_ids
        input_ids = input_ids.to(self.device_map)

        generation_output = self.model.generate(
            input_ids=input_ids, max_new_tokens=8,
        )

        return self.tokenizer.decode(generation_output[0][len(input_ids[0]):]).strip(' ').strip('\n').strip("<|end|>")


phi_3_model = LLMModel("microsoft/Phi-3-mini-128k-instruct")
