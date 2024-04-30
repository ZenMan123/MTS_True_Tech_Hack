let stream = null
navigator.mediaDevices.getUserMedia({audio: true})
    .then((_stream) => {
        stream = _stream
    })
    .catch((err) => {
        console.error(`Not allowed or not found: ${err}`)
    })

let chunks = []
let mediaRecorder = null
let audioBlob = null

const listener = document.getElementById('listener')
listener.addEventListener('click', startRecord)

async function startRecord() {
    if (!navigator.mediaDevices && !navigator.mediaDevices.getUserMedia) {
        return console.warn('Not supported')
    }
    if (!mediaRecorder) {
        listener.textContent = "СТОП"
        try {
            const stream = await navigator.mediaDevices.getUserMedia({
                audio: true
            })
            mediaRecorder = new MediaRecorder(stream)
            mediaRecorder.start()
            mediaRecorder.ondataavailable = (e) => {
                chunks.push(e.data)
            }
            mediaRecorder.onstop = mediaRecorderStop
        } catch (e) {
            console.error(e)
        }
    } else {
        mediaRecorder.stop()
    }
}

function getButtonList() {
    return Array.of(document.getElementsByClassName('btn'))
}

function mediaRecorderStop() {
    audioBlob = new Blob(chunks, {type: 'audio/wav'})
    const audioUrl = URL.createObjectURL(audioBlob)
    const audio = new Audio(audioUrl)
    audio.play()
    listener.textContent = "ЗАПИСЬ"
    mediaRecorder = null
    chunks = []

    async function saveRecord() {
        const formData = new FormData()
        let audioName = prompt('Name?')
        audioName = audioName ? Date.now() + '-' + audioName : Date.now()
        formData.append('audio', audioBlob, audioName)
        try {
            await fetch('http://localhost:8080/upload-audio', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({buttons: getButtonList(), audio: formData})
            })
            console.log('Saved')
            audioBlob = null
        } catch (e) {
            console.error(e)
        }
    }

    saveRecord()
}