<script>
export default {
  name: "IndexLogged",
  props: ["user"]
}
</script>

<template>
  <div>
    <p>Телефон: {{ user.phoneNumber }}</p>
    <p>Баланс: {{ user.balance }}</p>
  </div>
</template>

<style scoped>

</style>