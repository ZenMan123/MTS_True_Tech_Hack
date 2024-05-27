<template>
  <div></div>
</template>

<script>
import axios from "axios";

export default {
  name: "Listener",
  props: ["user"],
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
      recording: false,
      buttonList: []
    };
  },
  methods: {
    collectButtons() {
      const links = document.querySelectorAll('a');
      const buttons = document.querySelectorAll('button');
      const elementsWithHoverEffect = document.querySelectorAll('.hover-effect');
      const elements = [...links, ...buttons, ...elementsWithHoverEffect];
      elements.forEach((button, index) => {
        button.id = 'button_' + index;
      });
      this.buttonList = elements.map(element => {
        return {
          button_text: element.textContent || element.innerText,
          button_id: element.id || null
        };
      });
      console.log("Char " + this.buttonList)
    },
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
      this.collectButtons()
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
            utterance.onend = function() {
              this.$root.$emit("onUtteranceEnded", true)
            }.bind(this)
            window.speechSynthesis.speak(utterance)
            const button_id = response.data['choose_response']
            let fields = response.data['fields']
            const task_type = response.data['task_type'] === "\"PAY_MONEY\"" ? 1 : 0
            console.log("button_id: " + button_id)
            console.log("fields: " + fields)
            console.log("task_type: " + task_type)
            if (fields !== undefined) {
              fields = JSON.parse(fields)
              this.$root.$emit("onChangePage", "Payment")
              setTimeout(() => {
                this.$root.$emit("setType", "Payments")
              }, 1000)
              setTimeout(() => {
                this.$root.$emit("onFieldsFilled", task_type, fields)
              }, 1000)
            }
            if (button_id !== undefined) {
              console.log("Нажимаем " + button_id)
              const button = document.getElementById(button_id)
              button.click()
            }
          })
          .catch((error) => console.error('Ошибка при отправке аудио на сервер: ', error))
    }
  }
};
</script>