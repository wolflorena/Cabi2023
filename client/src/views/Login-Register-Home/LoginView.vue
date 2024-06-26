<script setup lang="ts">
import { ref, watch } from "vue";
import CustomButton from "@/components/CustomButton.vue";
import CustomInput from "@/components/CustomInput.vue";
import PasswordInput from "@/components/PasswordInput.vue";
import { useRouter } from "vue-router";
import { loginService } from "@/services/authentication_service";
import { jwtDecode } from "jwt-decode";
import Swal from "sweetalert2";
const emailText = ref("");
const passwordText = ref("");
const error = ref("");

const router = useRouter();

function handlePasswordTextChanged(password: string) {
  passwordText.value = password;
}

async function login() {
  try {
    if (emailText.value && passwordText.value) {
      const response = await loginService(emailText.value, passwordText.value);

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || "Login failed");
      }

      // Store the JWT token in localStorage
      localStorage.setItem("jwtToken", data.jwtToken);

      // Decode the token to extract role information
      const decodedToken: any = jwtDecode(data.jwtToken);

      const role = decodedToken.role;

      // Redirect based on the role
      if (role === "ROLE_CUSTOMER") {
        router.push("/profile");
      } else if (role === "ROLE_ADMIN") {
        router.push("/admin/dashboard");
      } else if (role === "ROLE_DOCTOR") {
        router.push("/doctor/dashboard");
      } else {
        throw new Error("Unknown role");
      }
    } else {
      error.value = "Please enter your email and password.";
    }
  } catch (err: any) {
    if (err.message === "Authentication failed: Account is deactivated") {
      Swal.fire({
        titleText:
          "Your account have been deactivated. Please contact support.",
        icon: "error",
      });
    } else if (err.message === "Authentication failed: Bad credentials") {
      Swal.fire({
        titleText: "Bad Credentials",
        text: "Please check your details",
        icon: "error",
        position: "top-end",
      });
    } else {
      error.value = "" + err;
      emailText.value = "";
      passwordText.value = "";
    }
  }
}

function redirectToRegister() {
  router.push("/register");
}
</script>

<template>
  <div class="container">
    <div class="img-container">
      <img class="img-right" src="@/assets/form.jpeg" alt="" />
    </div>
    <div class="login-container">
      <img src="@/assets/logo.png" alt="" />
      <form class="form" @submit.prevent="login">
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
        <CustomButton
          text="Login"
          width="200"
          height="30"
          @click.prevent="login"
        />
        <div class="register-form-container">
          <span
            :style="{
              fontSize: '14px',
              justifySelf: 'flex-end',
            }"
          >
            No account? we have you covered
          </span>
          <CustomButton
            color="#2c3343"
            :isMain="false"
            text="Register"
            width="200"
            height="30"
            @click.prevent="redirectToRegister"
          />
        </div>
      </form>
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

      .register-form-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 14px;
      }
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
