<template>
  <div></div>
</template>

<script>
import axios from "axios";

export default {
  name: "Listener",
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
            this.mediaRecorder = new MediaRecorder(stream);
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
      const formData = new FormData();
      formData.append('audio', blob);
      axios.post('http://localhost:8090/api/upload-audio', formData)
          .then(() => console.log('Аудио успешно отправлено на сервер.'))
          .catch(() => console.error('Ошибка при отправке аудио на сервер:'))
    }
  }
};
</script>