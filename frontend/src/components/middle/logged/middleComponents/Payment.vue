<script>
import Payments from "@/components/middle/logged/middleComponents/payment/Payments.vue";
import Transactions from "@/components/middle/logged/middleComponents/payment/Transactions.vue";

export default {
  name: "Payment",
  props: ["user"],
  components: {Payments, Transactions},
  data() {
    return {type: null};
  },
  methods: {
    changeType(type) {
      this.type = type;
    }
  }
}
</script>

<template>
  <div class="body_page">
    <div class="user_data">
      <div class="my_phone">
        <div class="title_2">Мой телефон</div>
        <div>{{ user.phoneNumber }}</div>
      </div>
      <div class="cards">
        <div class="title_2">Карты</div>
        <div class="my_card">
          <div>MC*0525</div>
          <div>**** **** **** 0525</div>
        </div>
        <div class="my_wallet">
          <div>Мой кошелек</div>
          <div>{{ user.balance }} руб.</div>
        </div>
        <div class="offer_link_card">Привязать карту другого банка</div>
      </div>
      <div class="loans">
        <div class="title_2">Кредиты</div>
        <div>Оформите заявку на кредит по выгодным ставкам за 5 минут</div>
      </div>
    </div>
    <div class="elements_of_main_page">
      <div>
        <div class="title_1">Платежи и переводы</div>
        <div></div>
      </div>
      <div class="payments_or_transfers">
        <div class="buttons">
          <div>
            <a href="#" @click="changeType('Payments')">Платежи</a>
            <a href="#" @click="changeType('Transactions')">Переводы</a>
          </div>
          <div>
            <component :is="type"></component>
          </div>
        </div>

      </div>

    </div>
  </div>
</template>

<style scoped>
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

.user_data div {
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

.my_phone {
  padding: 1.5rem;
}

.cards {
  padding: 1.5rem;

}

.loans {
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
</style>