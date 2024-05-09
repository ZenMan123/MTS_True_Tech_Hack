<template>
  <div class="enter-or-register-box">
    <template v-if="user">
      <HeaderLogged :user="user"/>
    </template>
    <template v-else>
      <HeaderGuest/>
    </template>
  </div>
</template>

<script>
import HeaderGuest from "@/components/header/HeaderGuest.vue";
import HeaderLogged from "@/components/header/HeaderLogged.vue";

export default {
  name: "Header",
  props: ["user"],
  components: {HeaderGuest, HeaderLogged}
}
</script>

<style scoped>

</style>