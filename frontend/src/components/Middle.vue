<script>
import MiddleGuest from "@/components/middle/guest/MiddleGuest.vue";
import MiddleLogged from "@/components/middle/logged/MiddleLogged.vue";
import Listener from "@/components/middle/Listener.vue";

export default {
  name: "Middle",
  props: ["user"],
  components: {Listener, MiddleGuest, MiddleLogged},
  data() {
    return {
      buttonList: []
    }
  },
  mounted() {
    const links = document.querySelectorAll('a');
    const buttons = document.querySelectorAll('button');
    const elementsWithHoverEffect = document.querySelectorAll('.hover-effect');
    const elements = [...links, ...buttons, ...elementsWithHoverEffect];
    elements.forEach((button, index) => {
      button.id = 'button_' + index;
    });
    this.buttonList = elements.map(element => {
      return {
        button_text: element.textContent || element.innerText,
        button_id: element.id || null
      };
    });
    console.log("Char " + this.buttonList)
  }
}
</script>
<template>
  <main>
    <MiddleLogged v-if="user" :user="user"/>
    <MiddleGuest v-else/>
    <Listener :buttonList="buttonList"/>
  </main>
</template>

<style scoped>

</style>