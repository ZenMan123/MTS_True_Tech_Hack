<script>
import IndexGuest from "@/components/middle/guest/middleComponents/IndexGuest.vue";
import Enter from "@/components/middle/guest/middleComponents/Enter.vue";
import Cards from "@/components/middle/guest/middleComponents/Cards.vue";
import Credits from "@/components/middle/guest/middleComponents/Credits.vue";
import Deposits_and_accounts from "@/components/middle/guest/middleComponents/Deposits_and_accounts.vue";
import Investment from "@/components/middle/guest/middleComponents/Investment.vue";
import Mortgage from "@/components/middle/guest/middleComponents/Mortgage.vue";
import Premium from "@/components/middle/guest/middleComponents/Premium.vue";
import Private from "@/components/middle/guest/middleComponents/Private.vue";

export default {
  name: "MiddleGuest",
  components: {IndexGuest, Enter, Cards, Credits, Deposits_and_accounts, Investment, Mortgage, Premium, Private},
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
  <div>
    <IndexGuest v-if="page==='IndexGuest'"/>
    <Enter v-if="page==='Enter'"/>
    <Cards v-if="page==='Cards'"/>
    <Credits v-if="page==='Credits'"/>
    <Deposits_and_accounts v-if="page==='Deposits_and_accounts'"/>
    <Investment v-if="page==='Investment'"/>
    <Mortgage v-if="page==='Mortgage'"/>
    <Premium v-if="page==='Premium'"/>
    <Private v-if="page==='Private'"/>

  </div>
</template>

<style scoped>

</style>