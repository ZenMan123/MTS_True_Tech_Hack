<script>
export default {
  name: "HeaderLogged",
  props: ["user"],
  methods: {
    changePage: function (page) {
      this.$root.$emit("onChangePage", page);
    },
    onLogout: function () {
      this.$root.$emit("onLogout");
      this.changePage("IndexGuest");
    }
  },
}
</script>

<template>
  
    <div class="header">
      <div class="header_up">
        <div class="header_up_company">
          <div class="customer"><a href="#">Частным клиентам</a></div>
          <div class="line_vert"></div>
          <div class="all_sites"><a href="">Все сайты</a></div>
        </div>

        <div class="header_up_profile">
          {{ user.phone_number }}
          <a class="button_leave" href="#" @click.prevent="onLogout">Выйти</a>
        </div>
       
      </div>

      <div class="header_down">
        <a href="#" @click.prevent="changePage('IndexLogged')">
          <img class="logo" src="../../assets/img/logoMtsBank.png" alt="MTS Bank" title="MTS Bank"/>
        </a>
        <nav>
          <ul>
            <li><a href="#" @click.prevent="changePage('IndexLogged')">Главная</a></li>
            <li><a href="#" @click.prevent="changePage('Payment')">Платежи и переводы</a></li>
            <li><a href="#" @click.prevent="changePage('History')">История</a></li>
            <li><a href="#" @click.prevent="changePage('Banks_products')">Банковские продукты</a></li>
            <li><a href="#" @click.prevent="changePage('Offers')">Предложения</a></li>
          </ul>
        </nav>
      </div>
    </div>
  
</template>

<style scoped>

a {
  text-decoration: none;
  color: rgb(150, 159, 168);
}

li {
  display: inline;
  text-decoration: none;
  color: var(--black-color);
  font-family: "Arial Narrow", sans-serif;
}

.customer {
  border-top: 3px solid #e30613;
  display: flex;
  align-items: center;
  height: inherit;
}

.line_vert {
  height: 10px;
  margin: 0 20px;
  border-left: 1px solid #dcdcdc;
}

.all_sites {
  color: rgb(128, 128, 128);
  font-size: 14px;
}


.header_up {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 2.5rem;
  flex-direction: row;
  font-size: 14px;
  background-color: #f7f7f7;
}

.header_up_company div {
  display: flex;
  align-items: center;
  margin-right: 1.5rem;
}

.header_up div div {
  padding: 1rem 0;
}

.header_up_company {
  display: flex;
  align-items: center;
}

.header_up_profile{
  display: flex;
  align-items: center;
}

.header_down {
  display: flex;
  align-items: center;
  font-size: 17px;
  margin: 0.5rem 0;
  padding: 0 2.5rem;
}

.header_down li {
  margin-right: 1.5rem;
}

.header_down a {
  color: black;
  font-weight: 600;
}

.logo {
  max-width: 8rem;
  margin-right: 2rem;
  margin-top: 1rem;
}

.button_leave {
  cursor: pointer;
  padding: 5px 30px;
  border: 1px solid #e30613;
  border-radius: 5px;
  color: #e30613;
  transition: .2s;
  margin-left: 1rem;
}

</style>