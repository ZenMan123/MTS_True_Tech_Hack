<script>
import axios from 'axios';

export default {
  name: "History",
  props: ["user"],
  data() {
    return {
      payments: [],
    };
  },
  mounted() {
    axios.get('/getHistory', {
      params: { userId: this.user.userId }
    })
        .then(response => {
          this.payments = response.data;
        })
        .catch(error => {
          console.error('Error fetching payment history:', error);
        });
  }
};
</script>

<template>
  <div class="history">
    <div class="header">
      <h2>История операций</h2>
      <div class="print">
        <img src="../../../../assets/img/logoMtsBank.png" alt="print">
        Распечатать
      </div>
    </div>
    <div class="content">
      <ul>
        <li><a>За сегодня</a></li>
        <li><a>За эту неделю</a></li>
        <li><a>За всё время</a></li>
      </ul>
      <hr>
      <table>
        <thead>
        <tr>
          <th>Тип платежа</th>
          <th>Сумма</th>
          <th>Дата и время</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="payment in payments" :key="payment.date">
          <td>
            <div class="category">
              <img src="../../../../assets/img/logoMtsBank.png" alt="logo">
              {{ payment.type }}
            </div>
          </td>
          <td>{{ payment.amount }}</td>
          <td>{{ payment.date }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.print img {
  max-width: 4rem;
}

.content img {
  max-width: 5rem;
}
</style>
