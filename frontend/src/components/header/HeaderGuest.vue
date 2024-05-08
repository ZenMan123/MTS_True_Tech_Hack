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

    <div class="header_12">
      <div class="header_1">
        <div class="header_1_1">
          <div class="face"><a href="#">Частным лицам</a></div>
          <div><a href="#">Малый бизнес и ИП</a></div>
          <div><a href="#">Корпоративный бизнес</a></div>
        </div>

        <div class="header_1_2">
          <div><a href="#">Офисы и банкоматы</a></div>
          <div class="city"><a href="#">Москва</a></div>
        </div>
      </div>

      <div class="header_2">
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
  text-decoration: none;
  color: rgb(150, 159, 168);
}

li {
  display: inline;
  text-decoration: none;
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
}

.header_12 {
  width: 100%;
}

.header_1 {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-direction: row;
  font-size: 13px;
  border-bottom: 1px solid rgb(226, 229, 235);
}

.header_1_1 {
  display: flex;
  align-items: center;
  flex-direction: row;
}

.header_1_2 {
  display: flex;
  align-items: center;
  flex-direction: row;
}

.city {
  margin-right: 0;
  margin-left: 1.5rem;
}

.header_1_1 div {
  display: flex;
  align-items: center;
  margin-right: 1.5rem;
}

.header_1 div div {
  padding: 1rem 0;
}


.header_2 {
  display: grid;
  grid-area: 2 / 2 / auto / 4;
  grid-template-columns: 1fr max-content;
  align-items: center;
  font-size: 17px;
  margin: 2rem 0;
}

.header_2 li {
  margin-right: 1.5rem;
}

.header_2 a {
  color: black;
  font-weight: 600;
}

.logo {
  max-width: 8rem;
  margin-right: 5rem;
  margin-top: 1rem;
}

</style>