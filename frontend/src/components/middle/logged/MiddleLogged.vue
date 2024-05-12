<script>
import IndexLogged from "@/components/middle/logged/middleComponents/IndexLogged.vue";
import Bank_products from "@/components/middle/logged/middleComponents/Bank_products.vue";
import History from "@/components/middle/logged/middleComponents/History.vue";
import Offers from "@/components/middle/logged/middleComponents/Offers.vue";
import Payment from "@/components/middle/logged/middleComponents/Payment.vue";

export default {
  name: "MiddleLogged",
  components: {IndexLogged, Bank_products, History, Offers, Payment},
  props: ["user"],
  data: function () {
    return {
      page: "IndexLogged"
    }
  },
  beforeCreate() {
    this.$root.$on("onChangePage", (page) => {
      this.page = page
    })
  }
}
</script>

<template>
  <div class="background">
    <IndexLogged v-if="page==='IndexLogged'" :user="user"/>
    <Bank_products v-if="page==='Bank_products'"/>
    <History v-if="page==='History'" :user="user"/>
    <Offers v-if="page==='Offers'"/>
    <Payment v-if="page==='Payment'" :user="user"/>
  </div>
</template>

<style scoped>
.background {
  background-color: #f2f3f7;
  padding: 2rem 2.5rem;
}
</style>