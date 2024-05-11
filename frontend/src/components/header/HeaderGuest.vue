<script>
export default {
  name: "HeaderGuest",
  methods: {
    changePage: function (page) {
      this.$root.$emit("onChangePage", page);
    },
  },
}
</script>

<template>
  <div class="header">
    <div>
      <a href="#" @click.prevent="changePage('IndexGuest')">
        <img class="logo" src="../../assets/img/logoMtsBank.png" alt="MTS Bank" title="MTS Bank"/>
      </a>
    </div>

    <div class="header_up_down">
      <div class="header_up">
        <div class="header_up_company">
          <div class="face"><a href="#">Частным лицам</a></div>
          <div><a href="#">Малый бизнес и ИП</a></div>
          <div><a href="#">Корпоративный бизнес</a></div>
        </div>

        <div class="header_up_place">
          <div><a href="#">Офисы и банкоматы</a></div>
          <div class="city"><a href="#">Москва</a></div>
        </div>
      </div>

      <div class="header_down">
        <div>
          <nav>
            <ul>
              <li><a href="#" @click.prevent="changePage('Cards')">Карты</a></li>
              <li><a href="#" @click.prevent="changePage('Deposits_and_accounts')">Вклады и счета</a></li>
              <li><a href="#" @click.prevent="changePage('Credits')">Кредиты</a></li>
              <li><a href="#" @click.prevent="changePage('Mortgage')">Ипотека</a></li>
              <li><a href="#" @click.prevent="changePage('Investment')">Инвестиции</a></li>
              <li><a href="#" @click.prevent="changePage('Premium')">Премиум</a></li>
              <li><a href="#" @click.prevent="changePage('Private')">Private</a></li>
              <li><a href="#" @click.prevent="changePage('#')">Ещё</a></li>
            </ul>
          </nav>
        </div>
        <div><a href="#" @click.prevent="changePage('Enter')">Войти</a></div>

      </div>
    </div>
  </div>
</template>

<style scoped>

a {
  color: rgb(150, 159, 168);
}

li {
  display: inline;
  color: var(--black-color);
  font-family: "Arial Narrow", sans-serif;
}

.face {
  border-bottom: 1px solid #e30613;
  display: flex;
  align-items: center;
  height: inherit;
}

.header {
  display: flex;
  flex-direction: row;
  padding: 0 2.5rem;
}

.header_up_down {
  width: 100%;
}

.header_up {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-direction: row;
  font-size: 13px;
  border-bottom: 1px solid rgb(226, 229, 235);
}

.header_up_company {
  display: flex;
  align-items: center;
  flex-direction: row;
}

.header_up_place {
  display: flex;
  align-items: center;
  flex-direction: row;
}

.city {
  margin-right: 0;
  margin-left: 1.5rem;
}

.header_up_company div {
  display: flex;
  align-items: center;
  margin-right: 1.5rem;
}

.header_up div div {
  padding: 1rem 0;
}


.header_down {
  display: grid;
  grid-area: 2 / 2 / auto / 4;
  grid-template-columns: 1fr max-content;
  align-items: center;
  font-size: 17px;
  margin: 2rem 0;
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
  margin-right: 5rem;
  margin-top: 1rem;
}

</style>