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
        Распечатать
      </div>
    </div>
    <div class="content">
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

.history {
  background-color: #fff;
  padding: 1rem;
  margin: 2rem 4rem;
  border-radius: 20px;
}

.header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding: 0 1rem;
}

.content ul{
  display: flex;
  flex-direction: row;
  list-style-type: none;
  padding-left: 1rem;
}

.content ul li{
  margin-right: 1rem;
}

table {
  padding: 1rem;
  width: 100%;
} 

thead th{
  padding-bottom: 1rem;
}


tbody td {
  align-items: center;
  text-align: center; 
  vertical-align: middle;
  padding-bottom: 1rem;
}

</style>
