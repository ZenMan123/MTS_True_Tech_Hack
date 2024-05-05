<template>
  <header>
    <div>
      <div class="enter-or-register-box">
          <template v-if="user">
            {{ user.login }}
            |
            <div>
              <a href="#" @click.prevent="changePage('Index')">
                  <img class="logo" src="../assets/img/logoMtsBank.png" alt="MTS Bank" title="MTS Bank"/>
              </a>
              <div class="header_1">
                <div class="customer"><a href="#">Частным клиентам</a></div>
                <div class="line_vert"></div>
                <div class="all_sites"><a href="">Все сайты</a></div>
                <a class="button" href="#" @click.prevent="onLogout">Выйти</a>
              </div>
              
              <div class="header_2">
                
                <nav>
                  <ul>
                    <li><a href="#" @click.prevent="changePage('Index')">Главная</a></li>
                    <li><a href="#" @click.prevent="changePage('Payment')">Платежи и переводы</a></li>
                    <li><a href="#" @click.prevent="changePage('History')">История</a></li>
                    <li><a href="#" @click.prevent="changePage('Banks_products')">Банковские продукты</a></li>
                    <li><a href="#" @click.prevent="changePage('Offers')">Предложения</a></li>
                  </ul>
                </nav>
              </div>
            </div>
          </template>

          <template v-else>
            <div class="header">
              <div>
                <a href="#" @click.prevent="changePage('Index')">
                  <img class="logo" src="../assets/img/logoMtsBank.png" alt="MTS Bank" title="MTS Bank"/>
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
      </div>
    </div>

  </header>
</template>

<script>
export default {
  name: "Header",
  // props: ["user"],
  // methods: {
  //   changePage: function (page) {
  //     this.$root.$emit("onChangePage", page);
  //   },
  //   onLogout: function () {
  //     this.$root.$emit("onLogout");
  //     this.changePage("Index");
  //   }
  // },
}
</script>

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

  .face {
    border-bottom: 1px solid #e30613;
    display: flex;
    align-items: center;
    height: inherit;
  }

  .line_vert{
    height: 26px;
    margin: 0 20px;
    border-left: 1px solid #dcdcdc;
  }

  .all_sites{
    color: rgb(128, 128, 128);
    font-size: 14px;
  }

  .header{
    display: flex;
    flex-direction: row;
  }

  .header_12{
    width: 100%;
  }

  .header_1{
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-direction: row;
    font-size: 13px;
    border-bottom: 1px solid rgb(226, 229, 235);
  }

  .header_1_1{
    display: flex;
    align-items: center;
    flex-direction: row;
  }

  .header_1_2{
    display: flex;
    align-items: center;
    flex-direction: row;
  }

  .city{
    margin-right: 0;
    margin-left: 1.5rem;
  }
  
  .header_1_1 div {
    display: flex;
    align-items: center;
    margin-right: 1.5rem;
  }

  .header_1 div div{
    padding: 1rem 0;
  }

  

  .header_2{
    display: grid;
    grid-area: 2 / 2 / auto / 4;
    grid-template-columns: 1fr max-content;
    align-items: center;
    font-size: 17px;
    margin: 2rem 0;
  }

  .header_2 li{
    margin-right: 1.5rem;
  }

  .header_2 a{
    color: black;
    font-weight: 600;
  }

  .logo {
    max-width: 8rem;
    margin-right: 5rem;
    margin-top: 1rem;
  }

  .button{
    cursor: pointer;
    padding: 5px 30px;
    border: 1px solid #e30613;
    border-radius: 5px;
    color: #e30613;
    transition: .2s;
  }

</style>