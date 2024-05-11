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
  <div>
    <h2>Платежи</h2>
    <div class="input-group">
      <label for="toPhoneNumber">Номер телефона получателя:</label>
      <input type="text" id="toPhoneNumber" v-model="toPhoneNumber"/>
    </div>
    <div class="input-group">
      <label for="amount">Сумма:</label>
      <input type="number" id="amount" v-model="amount"/>
    </div>
    <button @click="sendTransfer">Отправить</button>
  </div>
</template>

<style scoped>
/* Стили для компонента "Payments" */
</style>
