<template>
  <nav>
    <button class="btn" id="btn-1" v-on:click="method1">Кнопка 1</button>
    <button class="btn" id="btn-2" v-on:click="method2">Кнопка 2</button>
  </nav>
</template>

<script>
export default {
  name: "AppHeader",
  methods: {
    method1() {
      const btn = document.getElementById("btn-1")
      btn.textContent = btn.textContent === "clicked" ? "Кнопка 1" : "clicked"
    },
    method2() {
      window.speechSynthesis.cancel()
      window.speechSynthesis.speak(
          new SpeechSynthesisUtterance(
              Array.from(
                  document.getElementsByClassName("btn"))
                  .map(btn => btn.textContent)
                  .join(" ")
          )
      )
    }
  }
}
</script>

<style scoped>
nav {
  display: flex;
  justify-content: space-around;
  background: #63a6ff;
  padding: 1em 1em;
}

.btn {
  font-size: 1.5em;
}
</style>