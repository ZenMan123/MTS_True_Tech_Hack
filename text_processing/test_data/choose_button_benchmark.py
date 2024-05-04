import time

from text_processing.base_text_model import BaseTextModel
from text_processing.llm import LLMModel
from text_processing.word2vec import Word2VecModel

buttons = {
    "Частным клиентам": [],
    "Все сайты": [],
    "Главная": [],
    "Платежи и переводы": [],
    "История": [],
    "Банковские продукты": [],
    "Предложения": [],
    "Мой телефон": [],
    "Мой кошелек": [],
    "Привязать карту другого банка": [],
    "Оформите заявку на кредит по выгодным ставкам за 5 минут": [],
    "МТС Банк": [],
    "МТС": [],
    "Интернет, ТВ и Теле...": [],
    "МГТС": [],
    "Налоги и сборы": [],
    "Тинькофф Мобайл": [],
    "На брокерский...": [],
    "Счета на оплату": [],
    "Шаблоны": [],
    "Автоплатежи": [],
    "Создать новый": [],
    "MTS Cashback": [],
    "Открыть новый продукт": [],
    "История операций": [],
    "Комиссии и лимиты": [],
    "Справки и выписки": [],
    "Офисы и банкоматы": [],
    "Выйти": []
}

base_functionality_benchmark = {
    "посмотреть свой счет": "Мой кошелек",
    "посмотреть историю": "История операций",
    "перевести деньги": "Платежи и переводы",
    "открыть вклад": "Банковские продукты",
    "взять кредит": "Оформите заявку на кредит по выгодным ставкам за 5 минут",
    "оплатить кредит": "Платежи и переводы",
    "найти ближайший банкомат": "Офисы и банкоматы",
    "пополнить счет": "Платежи и переводы",
    "получить выписку": "Справки и выписки",
    "установить лимит на снятие наличных": "Комиссии и лимиты",
    "подать заявку на кредит": "Оформите заявку на кредит по выгодным ставкам за 5 минут",
    "оформить ипотеку": "Банковские продукты",
    "открыть депозит": "Банковские продукты",
    "заказать кредитную карту": "Банковские продукты",
    "погасить кредит": "Платежи и переводы",
    "открыть накопительный счет": "Банковские продукты",
    "получить справку о доходах": "Справки и выписки",
}

assert all(value in buttons for value in base_functionality_benchmark.values())


def test_model(model: BaseTextModel):
    start = time.time()
    formatted_buttons = []
    for i, button in enumerate(buttons):
        formatted_buttons.append({
            "button_text": button,
            "button_id": i + 1,
        })

    score = 0
    for request in base_functionality_benchmark:
        button = model.predict_button(request, formatted_buttons)
        print(f"Request: {request},\nPredicted: {button['button_text']}, "
              f"Correct: {base_functionality_benchmark[request]}")
        if button["button_text"] == base_functionality_benchmark[request]:
            score += 1

    total = time.time() - start

    return score / len(base_functionality_benchmark), total


print("Word2Vec model (score, time):", test_model(Word2VecModel()))
print("LLM model (score, time):", test_model(LLMModel()))
