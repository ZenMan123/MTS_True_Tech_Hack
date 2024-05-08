<script>
export default {
  name: "IndexLogged",
  props: ["userId", "users"]
}
</script>

<template>
  <div>
    <p>Телефон: {{ users[userId].login }}</p>
    <p>Баланс: {{ users[userId].balance }}</p>
  </div>
</template>

<style scoped>

</style>