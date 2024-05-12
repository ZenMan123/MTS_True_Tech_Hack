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
    this.$root.$on("onEnter", (phone_number) => {
      if (phone_number === "") {
        this.$root.$emit("onEnterValidationError", "Login is required");
        return;
      }
      axios.post("http://localhost:8090/api/jwts", {
        phone_number
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

      axios.get("http://localhost:8090/api/users/auth", {
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