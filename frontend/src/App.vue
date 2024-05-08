<template>
  <div id="app">
    <Header :userId="userId" :users="users"/>
    <Middle :userId="userId" :users="users"/>
    <Footer/>
  </div>
</template>

<script>

import Header from "@/components/header/Header.vue";
import Middle from "@/components/Middle.vue";
import Footer from "@/components/Footer.vue";

export default {
  name: 'App',
  components: {
    Footer,
    Middle,
    Header
  },
  data: function () {
    return this.$root.$data;
  },
  beforeCreate() {
    this.$root.$on("onEnter", (login) => {
      const users = Object.values(this.users).filter(u => u.login === login);
      if (users.length === 0) {
        this.$root.$emit("onEnterValidationError", "No such user");
      } else {
        this.userId = users[0].id;
        this.$root.$emit("onChangePage", "IndexLogged");
      }
    });

    this.$root.$on("onLogout", () => this.userId = null);
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
