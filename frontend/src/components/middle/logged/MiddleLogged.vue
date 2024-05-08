<script>
import IndexLogged from "@/components/middle/logged/middleComponents/IndexLogged.vue";
import Bank_products from "@/components/middle/logged/middleComponents/Bank_products.vue";
import History from "@/components/middle/logged/middleComponents/History.vue";
import Offers from "@/components/middle/logged/middleComponents/Offers.vue";
import Payment from "@/components/middle/logged/middleComponents/Payment.vue";

export default {
  name: "MiddleLogged",
  components: {IndexLogged, Bank_products, History, Offers, Payment},
  props: ["userId", "users"],
  data: function () {
    return {
      page: "IndexGuest"
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
    <IndexLogged v-if="page==='IndexLogged'" :userId="userId" :users="users"/>
    <Bank_products v-if="page==='Bank_products'"/>
    <History v-if="page==='History'"/>
    <Offers v-if="page==='Offers'"/>
    <Payment v-if="page==='Payment'"/>
  </div>
</template>

<style scoped>
.background{
  background-color: #f2f3f7;
  padding: 0 2.5rem;
}
</style>