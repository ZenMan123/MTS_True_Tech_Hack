<script>
export default {
  name: "Enter",
  data: function () {
    return {
      login: "",
      error: ""
    }
  },
  methods: {
    onEnter: function () {
      this.$root.$emit("onEnter", this.login);
    }
  },
  beforeCreate() {
    this.$root.$on("onEnterValidationError", (error) => this.error = error);
  }
}
</script>

<template>
  <div class="enter form-box">
    <div class="header">Вход</div>
    <div class="body">
      <form @submit.prevent="onEnter">
        <div class="field">
          <div class="name">
            <label for="login">Введите номер телефона любого оператора</label>
          </div>
          <div class="value">
            <input autofocus id="login" name="login" v-model="login"/>
          </div>
        </div>
        <div class="field error">{{ error }}</div>
        <div class="button-field">
          <input type="submit" value="Enter">
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>

</style>