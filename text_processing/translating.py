from googletrans import Translator

translator = Translator()


def ru_to_en(text: str) -> str:
    res = translator.translate(text, dest="en")
    return res.text
