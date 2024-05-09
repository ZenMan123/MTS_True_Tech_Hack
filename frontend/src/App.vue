<template>
  <div id="app">
    <Header :user="user"/>
    <Middle :user="user"/>
    <Footer/>
  </div>
</template>

<script>

import Header from "@/components/header/Header.vue";
import Middle from "@/components/Middle.vue";
import Footer from "@/components/Footer.vue";
import axios from "axios";

export default {
  name: 'App',
  components: {
    Footer,
    Middle,
    Header
  },
  data: function () {
    return {
      user: null
    }
  },
  beforeCreate() {
    this.$root.$on("onEnter", (phoneNumber) => {
      if (phoneNumber === "") {
        this.$root.$emit("onEnterValidationError", "Login is required");
        return;
      }
      alert(phoneNumber)
      axios.post("/api/jwts", {
        phoneNumber
      }).then(response => {
        alert(response.data)
      }).catch(error => {
        if (error.response.status === 400) {
          this.$root.$emit("onEnterValidationError", error.response.data);
        } else {
          this.$root.$emit("onEnterValidationError", "Unexpected error. Please, try again.");
        }
      })
      // const users = Object.values(this.users).filter(u => u.login === login);
      // if (users.length === 0) {
      //   this.$root.$emit("onEnterValidationError", "No such user");
      // } else {
      //   this.user = users[0];
      //   this.$root.$emit("onChangePage", "IndexLogged");
      // }
    });

    this.$root.$on("onLogout", () => this.user = null);
  },
  beforeMount() {

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
