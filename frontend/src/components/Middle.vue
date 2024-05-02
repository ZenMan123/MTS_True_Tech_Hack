<script>
import Index from "@/components/middle/Index.vue";
import Enter from "@/components/middle/Enter.vue";
import Payments from "@/components/middle/Payments.vue";
import Fees from "@/components/middle/Fees.vue";
import History from "@/components/middle/History.vue";

export default {
  name: "Middle",
  props: ["user"],
  components: {Index, Enter, Payments, Fees, History},
  data: function () {
    return {
      page: "Index"
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
  <main>
    <Index v-if="page==='Index'"/>
    <Enter v-if="page==='Enter'"/>
    <Payments v-if="page==='Payments'"/>
    <Fees v-if="page==='Fees'"/>
    <History v-if="page==='History'"/>
  </main>
</template>

<style scoped>

</style>