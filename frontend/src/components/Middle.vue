<script>
import MiddleGuest from "@/components/middle/guest/MiddleGuest.vue";
import MiddleLogged from "@/components/middle/logged/MiddleLogged.vue";

export default {
  name: "Middle",
  props: ["user"],
  components: {MiddleGuest, MiddleLogged},
}
</script>

<template>
  <main>
    <MiddleLogged v-if="user" :user="user"/>
    <MiddleGuest v-else/>
  </main>
</template>

<style scoped>

</style>