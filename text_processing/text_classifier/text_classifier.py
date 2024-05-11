from transformers import AutoTokenizer, AutoModel
import torch.nn.functional as F
import torch

from text_processing.config import EXAMPLES, THRESHOLD


class TextClassifier:
    def __init__(self):
        self.tokenizer = AutoTokenizer.from_pretrained("cointegrated/rubert-tiny")
        self.model = AutoModel.from_pretrained("cointegrated/rubert-tiny")
        self.categorie_vectors = {}
        for category, examples in EXAMPLES.items():
            inputs = self.tokenizer(examples, padding=True, truncation=True, return_tensors="pt")
            with torch.no_grad():
                outputs = self.model(**inputs)
                embeddings = outputs.last_hidden_state.mean(dim=1)
                self.categorie_vectors[category] = embeddings.mean(dim=0)

    def transform_feedback_to_vector(self, feedback):
        inputs = self.tokenizer(feedback, padding=True, truncation=True, return_tensors="pt")
        with torch.no_grad():
            outputs = self.model(**inputs)
            comment_vector = outputs.last_hidden_state.mean(dim=1)
        return comment_vector

    def classify_feedback(self, feedback):
        comment_vector = self.transform_feedback_to_vector(feedback)
        distances = {}
        for category, vector in self.categorie_vectors.items():
            cosine_similarity = F.cosine_similarity(comment_vector, vector, dim=1).item()
            distances[category] = cosine_similarity
        max_category = max(distances, key=distances.get)
        if distances[max_category] > THRESHOLD:
            return max_category
        else:
            return "другое"
