<script>
import axios from "axios";

export default {
  name: "Payments",
  props: ["user"],
  data() {
    return {
      toPhoneNumber: "",
      amount: 0,
    };
  },
  methods: {
    sendTransfer() {
      if (!this.toPhoneNumber || !this.amount) {
        return;
      }
      const data = {
        from_id: this.user.userId,
        to_phone_number: this.toPhoneNumber,
        value: this.amount,
      };
      console.info("data: " + data)
      axios.post("http://localhost:8090/transfer", data)
          .then(() => {
            console.log("Перевод выполнен");
            this.toPhoneNumber = "";
            this.amount = 0;
          })
          .catch(error => {
            console.error("Ошибка при отправке перевода", error);
          });
    },
  },
};
</script>

<template>
  <div class="container_payments">
    <div class="input-group">
      <label for="toPhoneNumber">Номер телефона получателя</label>
      <input type="text" id="toPhoneNumber" v-model="toPhoneNumber"/>
    </div>

    <div class="input-group">
      <label for="amount">Сумма</label>
      <input type="number" id="amount" v-model="amount"/>
    </div>
    <button @click="sendTransfer">Отправить</button>
  </div>
</template>

<style scoped>
/* Стили для компонента "Payments" */

.container_payments{
  padding-top: 2rem;
}

.input-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 1rem;
}
.input-group label{
  margin-bottom: 0.5rem;
}
input {
  position: relative;
    width: 50%;
    padding-left: 16px;
    padding-right: 16px;
    border: 0px;
    box-sizing: border-box;
    font-family: "MTS Sans", Arial, Helvetica, sans-serif;
    font-size: 17px;
    line-height: 2.5rem;
    color: rgb(0, 0, 0);
    background-color: rgb(255, 255, 255);
    -webkit-tap-highlight-color: transparent;
    border-radius: 8px;
}

button{
  width: 50%;
  padding: 0.875rem 0.75rem;
  margin-top: 1.25rem;
  font-style: normal;
  font-weight: 500;
  font-size: 1rem;
  line-height: 1.5rem;
  border-radius: 0.25rem;
  border: none;
  font-feature-settings: "tnum", "lnum";
  outline: none;
  display: flex;
  -webkit-box-pack: center;
  justify-content: center;
  transition: background-color 0.1s ease 0s;
  cursor: pointer;
  color: rgb(255, 255, 255);
  background-color: rgb(227, 6, 17);
  -webkit-tap-highlight-color: transparent;
}
</style>
