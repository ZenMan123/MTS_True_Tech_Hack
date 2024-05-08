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
  <div class="enter_form_box">
    <div class="body">
      <form @submit.prevent="onEnter">
        <div class="field">
          <div class="name">
            <label for="login">Введите номер телефона любого оператора</label>
          </div>
          <div class="value">
            <input autofocus id="login" name="login" v-model="login"/>
          </div>
          <div class="button-field">
          <input type="submit" value="Далее">
        </div>
        </div>
        <!-- <div class="field error">{{ error }}</div> -->
        
      </form>
    </div>
  </div>
</template>

<style scoped>
.enter_form_box{
  display: flex;
  flex-direction: column;
  -webkit-box-align: center;
  align-items: center;
  -webkit-box-pack: justify;
  justify-content: space-between;
  height: 100%;
  width: 100%;
  padding: 2rem;
  margin: 0px;
}
.field {
  position: relative;
    padding: 3rem;
    border-radius: 8px;
    background-color: #ffffff;
    width: 15rem;
}
.name {
  padding-bottom: 0.125rem;
  font-style: normal;
  font-weight: 800;
  font-size: 1rem;
  line-height: 1.75rem;
}

.value {
  position: relative;
  background-color: rgb(255, 255, 255);
  border: 2px solid rgb(187, 193, 199);
  box-sizing: border-box;
  transition: border-color 200ms ease 0s;
  -webkit-tap-highlight-color: transparent;
  border-radius: 8px;
}
input {
  position: relative;
    width: 100%;
    padding-left: 16px;
    padding-right: 16px;
    border: 0px;
    box-sizing: border-box;
    font-family: "MTS Sans", Arial, Helvetica, sans-serif;
    font-size: 17px;
    line-height: 24px;
    color: rgb(0, 0, 0);
    background-color: rgb(255, 255, 255);
    pointer-events: all;
    outline: none;
    appearance: none;
    -webkit-tap-highlight-color: transparent;
    border-radius: 8px;
}
.button-field input{
  width: 100%;
    padding: 0.875rem 0.75rem;
    margin-top: 1.25rem;
    font-style: normal;
    font-weight: 500;
    font-size: 1rem;
    line-height: 1.5rem;
    border-radius: 0.25rem;
    border: none;
    font-feature-settings: "tnum", "lnum";
    outline: none;
    display: flex;
    -webkit-box-pack: center;
    justify-content: center;
    transition: background-color 0.1s ease 0s;
    cursor: pointer;
    color: rgb(255, 255, 255);
    background-color: rgb(227, 6, 17);
    -webkit-tap-highlight-color: transparent;
}
</style>