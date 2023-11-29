<script setup lang="ts">
import CustomButton from "@/components/CustomButton.vue";
import CustomInput from "@/components/CustomInput.vue";
</script>

<template>
  <div class="container">
    <div class="signup-container">
      <img src="@/assets/logo.png" alt="" />
      <div class="form">
        <div class="inputs">
          <CustomInput label-text="First name" />
          <CustomInput label-text="Last name" />
          <CustomInput label-text="Email" />
          <CustomInput label-text="Password" type="password" />
          <CustomInput label-text="Check password" type="password" />
          <CustomInput label-text="Ocupation" />
          <CustomInput label-text="Date of birth" type="date" />
        </div>
        <CustomButton text="Signup" width="200" height="30" />
      </div>
    </div>
    <div class="img-container">
      <img class="img-right" src="@/assets/form.jpeg" alt="" />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;
}

.img-container {
  height: 100vh;
  width: 40%;
}

.img-right {
  height: 100%;
  width: 100%;
}
.signup-container {
  display: flex;
  flex-direction: column;
  align-items: center;

  height: 100vh;
  background-color: @sugar;
  width: 60%;

  img {
    width: 300px;
  }

  .form {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-evenly;

    width: 40vw;
    height: 60%;

    .inputs {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

      width: 100%;
      height: 100%;
      gap: 20px;
    }
  }
}
</style>
