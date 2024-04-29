from typing import List

import gensim.downloader
from gensim.models import KeyedVectors
import nltk
import pymorphy2

from text_processing.config import RUSSIAN_WORD2VEC_GENSIM_PATH, RUSSIAN_WORD2VEC_LOCAL_PATH

# Loading necessary data
try:
    word2vec_model = KeyedVectors.load(RUSSIAN_WORD2VEC_LOCAL_PATH)
except FileNotFoundError:
    word2vec_model = gensim.downloader.load(RUSSIAN_WORD2VEC_GENSIM_PATH)
    word2vec_model.save(RUSSIAN_WORD2VEC_LOCAL_PATH)

if not nltk.data.find("tokenizers/punkt"):
    nltk.download("punkt")
morph = pymorphy2.MorphAnalyzer()


def clear_text(text: str) -> str:
    return ''.join(el.lower() for el in text if el.isalpha() or el == ' ' or el.isdigit())


def add_part_of_speech(word: str) -> str:
    analyze = morph.parse(word)[0]
    speech_part = analyze.tag.POS
    normal_form = analyze.normal_form.replace('ё', 'е')

    if speech_part == "INFN":
        speech_part = "VERB"

    return f"{normal_form}_{speech_part}"


def get_closest_word(text: str, candidates: List[str]) -> str:
    words = [(add_part_of_speech(word), word) for word in nltk.word_tokenize(clear_text(text))]

    parsed_candidates = []
    for candidate in candidates:
        parsed_words = []
        for word in nltk.word_tokenize(clear_text(candidate)):
            parsed_words.append(add_part_of_speech(word))
        parsed_candidates.append((parsed_words, candidate))

    best_candidate, best_similarity = parsed_candidates[0][1], 0
    for candidate in parsed_candidates:
        max_similarity = 0

        for candidate_word in candidate[0]:
            for word in words:
                try:
                    similarity = word2vec_model.similarity(word[0], candidate_word)
                except KeyError:
                    similarity = 0
                max_similarity = max(max_similarity, similarity)

            if max_similarity > best_similarity:
                best_similarity = max_similarity
                best_candidate = candidate[1]

    return best_candidate


print(get_closest_word("посмотреть свой счет",
                       ['оформление', 'переводы', 'мой баланс']))
