<script>
export default {
  name: "Footer"
}
</script>

<template>
  <div class = "footer"> 
    <div class="footer_1">
      <div class="footer_1_1">
        <p class="number">8 800 250-0890</p>
        <p>Бесплатно для регионов РФ</p>
      </div>
      
      <div class="footer_1_2">
        <div>
          <a href="#" @click.prevent>Установите МТС Банк на смартфон</a>
          <a href="#" @click.prevent>Без App Store и Google Play</a>
        </div>
        <!-- <img src="C:\Users\olyac\Desktop\mts.svg" alt="\"> -->
      </div>
      
    </div>
    
    <div class="footer_2">
      <a href="#" @click.prevent>© 2024 ПАО «МТС». Все права защищены</a><br>
      <nav>
        <ul>
          <li> <a href="#" @click.prevent>Поддержка</a></li>
          <li><a href="#" @click.prevent>Соглашение о пользовании сервисом</a></li>
          <li><a href="#" @click.prevent>Политика обработки cookies</a></li>
        </ul>
      </nav>
    </div>
    
  </div>
  

</template>

<style scoped>
  p {
    font-size: 12px;
    margin: 0;
  }
  ul {
    position: relative;
  }
  /* header .first-nav ul li */
  li {
    display: inline;
    margin-left: 1rem;
    text-decoration: none;
    color: var(--black-color);
    font-family: "Arial Narrow", sans-serif;
  }
  a {
    text-decoration: none;
    color: #626c77;
    font-size: 12px;
  }
  .footer {
    background-color: #FFFFFF;
    margin: 2rem 0;
  }
  .footer div {
    display: flex;
    justify-content: space-between;
    /* align-items: center; */
  }
  .footer_1{
    margin-bottom: 2rem;
  }
  .footer_1 div{
    flex-direction: column;
  }
  .footer_1_1{
    align-items: start;
  }
  .footer_1_2 div{
    justify-content: space-around;
    align-items: flex-end;
  }
  
  .footer_2 {
    border-top: 1px solid #f2f3f7;
    margin-top: 2rem;
    align-items: center;
  }

  .number {
    font-size: 20px;
    font-weight: 700;
  }

</style>