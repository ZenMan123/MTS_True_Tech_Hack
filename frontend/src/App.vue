<template>
  <div id="app">
    <AppHeader/>
    <Header :user="user"/>
    <Middle :user="user"/>
    <Footer/>
  </div>
</template>

<script>

import AppHeader from "@/components/AppHeader.vue";
import Header from "@/components/Header.vue";
import Middle from "@/components/Middle.vue";
import Footer from "@/components/Footer.vue";

export default {
  name: 'App',
  components: {
    AppHeader,
    Footer,
    Middle,
    Header
  },
  data: function () {
    return {
      user: null
    }
  },
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
