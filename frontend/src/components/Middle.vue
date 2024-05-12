<script>
import MiddleGuest from "@/components/middle/guest/MiddleGuest.vue";
import MiddleLogged from "@/components/middle/logged/MiddleLogged.vue";
import Listener from "@/components/middle/Listener.vue";

export default {
  name: "Middle",
  props: ["user"],
  components: {Listener, MiddleGuest, MiddleLogged},
}
</script>
<template>
  <main>
    <MiddleLogged v-if="user" :user="user"/>
    <MiddleGuest v-else/>
    <Listener/>
  </main>
</template>

<style scoped>

</style>