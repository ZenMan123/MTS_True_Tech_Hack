<script>
import MiddleGuest from "@/components/middle/guest/MiddleGuest.vue";
import MiddleLogged from "@/components/middle/logged/MiddleLogged.vue";

export default {
  name: "Middle",
  props: ["userId", "users"],
  components: {MiddleGuest, MiddleLogged},
}
</script>

<template>
  <main>
    <MiddleLogged v-if="userId" :userId="userId" :users="users"/>
    <MiddleGuest v-else/>
  </main>
</template>

<style scoped>

</style>