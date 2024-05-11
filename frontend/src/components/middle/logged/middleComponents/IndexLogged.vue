<script>
export default {
  name: "IndexLogged",
  props: ["user"]
}
</script>

<template>
  <div>
    <p>Id: {{user.userId}}}</p>>
    <p>Телефон: {{ user.phone_number }}</p>
    <p>Баланс: {{ user.balance }}</p>
    <p> История: {{ user.payments}}</p>
  </div>
</template>

<style scoped>

</style>