<template>
  <header>
    <div class="first-nav">
      <a href="#" @click.prevent="changePage('Index')">
        <img class="logo" src="../assets/img/logo.png" alt="MTS Bank" title="MTS Bank"/>
      </a>
      <ul>
        <li>Частным лицам</li>
        <li>Малый бизнес и ИП</li>
        <li>Корпоративный бизнес</li>
      </ul>
      <div class="enter-or-register-box">
        <template v-if="user">
          {{ user.login }}
          |
          <a href="#" @click.prevent="onLogout">Выйти</a>
        </template>
        <template v-else>
          <a href="#" @click.prevent="changePage('Enter')">Войти</a>
        </template>
      </div>
    </div>
    <nav>
      <ul>
        <li><a href="#" @click.prevent="changePage('Index')">Домой</a></li>
        <li><a href="#" @click.prevent="changePage('Payment')">Платежи</a></li>
        <li><a href="#" @click.prevent="changePage('Fees')">Оплата</a></li>
        <li><a href="#" @click.prevent="changePage('History')">История</a></li>
      </ul>
    </nav>
  </header>
</template>

<script>
export default {
  name: "Header",
  props: ["user"],
  methods: {
    changePage: function (page) {
      this.$root.$emit("onChangePage", page);
    },
    onLogout: function () {
      this.$root.$emit("onLogout");
      this.changePage("Index");
    }
  },
}
</script>

<style scoped>
.logo {
  max-width: 5rem;
}
</style>