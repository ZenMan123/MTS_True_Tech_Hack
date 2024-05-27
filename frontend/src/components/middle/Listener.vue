<template>
  <div></div>
</template>

<script>
import axios from "axios";

export default {
  name: "Listener",
  props: ["buttonList", "user"],
  created() {
    window.addEventListener('keydown', this.handleStartRecording);
    window.addEventListener('keyup', this.handleStopRecording);
  },
  destroyed() {
    window.removeEventListener('keydown', this.handleStartRecording);
    window.removeEventListener('keyup', this.handleStopRecording);
  },
  data() {
    return {
      mediaRecorder: null,
      chunks: [],
      recording: false
    };
  },
  methods: {
    async handleStartRecording(event) {
      if (event.key === ' ') {
        event.preventDefault();
        if (!this.recording) {
          try {
            const stream = await navigator.mediaDevices.getUserMedia({audio: true});
            this.mediaRecorder = new MediaRecorder(stream, {audioBitsPerSecond: 16000});
            this.mediaRecorder.ondataavailable = event => {
              this.chunks.push(event.data);
            };
            this.mediaRecorder.onstop = () => {
              const blob = new Blob(this.chunks, {type: 'audio/wav'});
              this.sendRecording(blob);
              this.chunks = [];
            };
            this.mediaRecorder.start();
            this.recording = true;
          } catch (error) {
            console.error('Ошибка при доступе к микрофону:', error);
          }
        }
      }
    },
    handleStopRecording(event) {
      if (event.key === ' ' && this.mediaRecorder && this.recording) {
        event.preventDefault();
        this.mediaRecorder.stop();
        this.recording = false;
      }
    },
    async sendRecording(blob) {
      console.log(this.buttonList)
      const formData = new FormData();
      formData.append('audio', blob);
      formData.append('buttonList', JSON.stringify(this.buttonList));
      formData.append('id', this.user.userId)
      console.log(formData.get('audio'))
      console.log(formData.get('buttonList'))
      console.log(formData.get('id'))
      axios.post('http://localhost:8090/api/upload-audio', formData, {withCredentials: true})
          .then((response) => {
            console.log(response.data)
            window.speechSynthesis.cancel()
            const utterance = new SpeechSynthesisUtterance(response.data['text_to_speak'])
            window.speechSynthesis.speak(utterance)
            const button_id = response.data['button_id']
            let fields = response.data['fields']
            console.log("type::: " + JSON.stringify(response.data['task_type']))
            const task_type = response.data['task_type'] === "\"PAY_MONEY\"" ? 1 : 0
            console.log("button_id: " + button_id)
            console.log("fields: " + fields)
            console.log("task_type: " + task_type)
            if (fields) {
              fields = JSON.parse(fields)
              console.log("кидаем в шину " + fields)
              this.$root.$emit("onChangePage", "Payment")
              setTimeout(() => {
                this.$root.$emit("setType", "Payments")
              }, 1000)
              setTimeout(() => {
                this.$root.$emit("onFieldsFilled", task_type, fields)
              }, 1000)
            }
            if (button_id) {
              console.log("Нажимаем")
              const button = document.getElementById(button_id)
              button.click()
            }
          })
          .catch((error) => console.error('Ошибка при отправке аудио на сервер: ', error))
    }
  }
};
</script>