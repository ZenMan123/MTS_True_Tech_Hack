const speakEl = document.getElementById('listen')
speakEl.addEventListener('click', speakText)

function speakText() {
    window.speechSynthesis.cancel()
    const buttons = document.getElementsByClassName("btn")
    const textToSpeak = Array.from(buttons).map(btn => btn.textContent).join(" ")
    const utterance = new SpeechSynthesisUtterance(textToSpeak)
    window.speechSynthesis.speak(utterance)
}