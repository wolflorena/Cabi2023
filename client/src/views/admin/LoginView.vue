<script setup lang="ts">
import { ref, watch } from "vue";
import router from "../../router";
import CustomButton from "@/components/CustomButton.vue";
import CustomInput from "@/components/CustomInput.vue";
import PasswordInput from "@/components/PasswordInput.vue";

const emailText = ref("");
const passwordText = ref("");

function handlePasswordTextChanged(password: string) {
  passwordText.value = password;
}

// async function login() {
//   const hashPassword = await bcrypt.hash(
//     passwordText.value,
//     "$2a$10$QkRidA35ea0Fzm/ObrOEgO"
//   );
//   if (emailText.value && passwordText.value) {
//     loginAdmin(emailText.value, hashPassword)
//       .then((res) => {
//         console.log(localStorage.getItem("email"));
//       })
//       .catch((error) => {
//         emailText.value = "";
//         passwordText.value = "";
//       });
//   } else {
//     passwordText.value = "";
//   }
// }
</script>

<template>
  <div class="container">
    <div class="img-container">
      <img class="img-right" src="@/assets/form.jpeg" alt="" />
    </div>
    <div class="login-container">
      <img src="@/assets/logo.png" alt="" />
      <div class="form">
        <div class="inputs">
          <CustomInput
            label-text="Email"
            icon-token="user"
            v-model:model-value="emailText"
            type="text"
          />
          <PasswordInput
            label-text="Password"
            :value="passwordText"
            @password-changed="handlePasswordTextChanged"
          />
          <div class="forgot-password">
            <router-link to="/recovery" id="forgot">
              Forgot password?
            </router-link>
          </div>
        </div>
        <CustomButton text="Login" width="200" height="30" />
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;

  .img-container {
    width: 40vw;

    .img-right {
      height: 100vh;
      width: 100%;
    }
  }

  .login-container {
    display: flex;
    flex-direction: column;
    align-items: center;

    height: 100vh;
    width: 60%;
    background-color: @sugar;

    img {
      width: 300px;
    }

    .form {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: space-evenly;

      width: 60vw;
      height: 50%;

      .inputs {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;

        height: 50%;
        gap: 3rem;

        .forgot-password {
          width: 100%;
          display: flex;
          justify-content: flex-end;

          #forgot {
            text-decoration: none;
            color: @black;
            font-size: 12px;
          }
        }
      }
    }
  }
}
</style>
@/services/admins_service
