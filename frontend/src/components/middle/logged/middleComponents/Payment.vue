<script>
import Payments from "@/components/middle/logged/middleComponents/payment/Payments.vue";
import Transactions from "@/components/middle/logged/middleComponents/payment/Transactions.vue";
import axios from 'axios'; // Import axios for making HTTP requests

export default {
  name: "Payment",
  props: ["user"],
  components: {Payments, Transactions},
  data() {
    return {
      type: null,
      balance: null // Add a new data property to store the wallet balance
    };
  },
  methods: {
    changeType(type) {
      this.type = type;
    },
    fetchBalance() {
      axios.get('/getBalance', { // Use axios.get
        params: {userId: this.user.userId} // Pass user ID as a parameter
      })
          .then(response => {
            this.balance = response.data;
          })
          .catch(error => {
            console.error('Error fetching balance:', error);
          });
    }
  },
  mounted() {
    this.$root.$on("setType", type => this.type = type)
    // Fetch the wallet balance when the component is mounted
    this.fetchBalance();
  }
}


</script>

<template>
  <div class="body_page">
    <div class="user_data">
      <div class="my_phone">
        <div class="title_2">Мой телефон</div>
        <div>{{ user.phone_number }}</div>
      </div>
      <div class="cards">
        <div class="title_2">Карты</div>
        <div class="my_card">
          <div>MC*0525</div>
          <div>**** **** **** 0525</div>
        </div>
        <div class="my_wallet">
          <div>Мой кошелек</div>
          <div v-if="balance !== null">{{ balance }} руб.</div>
          <div v-else>Загрузка баланса...</div>
        </div>
        <div class="offer_link_card">Привязать карту другого банка</div>
      </div>
      <div class="loans">
        <div class="title_2">Кредиты</div>
        <div>Оформите заявку на кредит по выгодным ставкам за 5 минут</div>
      </div>
    </div>
    <div class="elements_of_main_page">
      <div class="title">
        <div class="title_1">Платежи и переводы</div>
      </div>
      <div class="payments_or_transfers">
        <div class="buttons">
          <div>
            <a href="#" @click="changeType('Transactions')">Платежи</a>
            <a href="#" @click="changeType('Payments')">Переводы</a>
          </div>
          <div class="container_payments">
            <component :is="type" :user="user"></component>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<style scoped>
.title {
  margin-bottom: 1rem;
}

.title_1 {
  font-size: 32px;
  font-weight: 800;
}

.title_2 {
  font-size: 20px;
  font-weight: 600;
}

.body_page {
  display: grid;
  grid-template-columns: repeat(12, 1fr);
  max-width: 100vw;
  grid-column-gap: 52px;
  padding: 4rem 0;
}

.user_data {
  display: flex;
  grid-column: 1 / 4;
  flex-direction: column;
}

.card {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-gap: 24px;
  gap: 24px;
}

.user_data div, .card div {
  display: flex;
  flex-direction: column;
  width: inherit;
  margin-bottom: 1rem;
  background-color: #fff;
  border-radius: 24px;
  justify-content: space-between;
  cursor: pointer;
  align-items: inherit;
}

.my_phone, .cards, .loans, .card div {
  padding: 1.5rem;
}

.my_card div {
  margin: 0;
  padding: 0.5rem 0;
}

.elements_of_main_page {
  display: flex;
  flex-direction: column;
  grid-column: 4 / 13;
}

.container_payments {
  margin-top: 2rem;
}

.buttons {
  margin: 1rem 0;
}

.buttons a {
  color: white;
  font-size: 14px;
  font-weight: 700;
  background-color: black;
  padding: 0.5rem 1rem;
  margin-right: 0.5rem;
  border-radius: 20px;
}

.buttons a:focus {
  background: rgb(227, 6, 17);
}
</style>