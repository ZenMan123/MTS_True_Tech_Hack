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
// {{ payment.type }}
// </div>
// </td>
// <td>{{ payment.user_id }}</td>
// <td>{{ payment.amount }}</td>
// <td>{{ payment.date }}</td>
  data: function () {
    return {
      user: {phoneNumber: '88005553535', balance: '500', payments: [{type: 'Перевод', user_id: '1', amount: '500', date: '09.05.2024'}]}
    }
  },
  beforeCreate() {
    this.$root.$on("onEnter", (phoneNumber) => {
      if (phoneNumber === "") {
        this.$root.$emit("onEnterValidationError", "Login is required");
        return;
      }
      axios.post("/api/jwts", {
        phoneNumber
      }).then(response => {
        localStorage.setItem("jwt", response.data);
        this.$root.$emit("onJwt", response.data);
      }).catch(error => {
        if (error.response.status === 400) {
          this.$root.$emit("onEnterValidationError", error.response.data);
        } else {
          this.$root.$emit("onEnterValidationError", "Unexpected error. Please, try again.");
        }
      })
    });

    this.$root.$on("onJwt", (jwt) => {
      localStorage.setItem("jwt", jwt);

      axios.get("/api/users/auth", {
        params: {
          jwt
        }
      }).then(response => {
        this.user = response.data;
        this.$root.$emit("onChangePage", "IndexLogged");
      }).catch(() => this.$root.$emit("onLogout"))
    });

    this.$root.$on("onLogout", () => {
      localStorage.removeItem("jwt");
      this.user = null;
    });
  },
  beforeMount() {
    if (localStorage.getItem("jwt") && !this.user) {
      this.$root.$emit("onJwt", localStorage.getItem("jwt"));
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
