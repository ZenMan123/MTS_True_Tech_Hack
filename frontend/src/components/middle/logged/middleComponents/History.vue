<script>
export default {
  name: "History",
  props: ["user"],
}
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
          <th>Категория</th>
          <th>Параметр оплаты</th>
          <th>Сумма</th>
          <th>Дата и время</th>
          <th>Статус</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="payment in user.payments" :key="payment.date">
          <td>
            <div class="category">
              <img src="../../../../assets/img/logoMtsBank.png" alt="logo">
              МС*0525 ->
              {{ payment.type }}
            </div>
          </td>
          <td>{{ payment.user_id }}</td>
          <td>{{ payment.amount }}</td>
          <td>{{ payment.date }}</td>
          <td>Успешно</td>
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