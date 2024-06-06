<script setup lang="ts">
import CustomButton from "@/components/CustomButton.vue";
import CustomInput from "@/components/CustomInput.vue";
import CustomPhoneNumberInput from "@/components/CustomPhoneNumberInput.vue";
import { registerCustomer } from "@/services/authentication_service";
import { format, formatISO } from "date-fns";
import Swal from "sweetalert2";
import { computed, ref, watch } from "vue";
import { useRouter } from "vue-router";
const firstName = ref<string>("");
const lastName = ref<string>("");
const email = ref<string>("");
const password = ref<string>("");
const checkPassword = ref<string>("");
const dateOfBirth = ref<string>("");
const phoneNumber = ref<string>("");
const errorMessage = ref<string>("");

const router = useRouter();

const formattedPhoneNumber = computed({
  get() {
    return formatPhoneNumber(phoneNumber.value);
  },
  set(value: string) {
    phoneNumber.value = value.replace(/\D/g, ""); // Remove non-digit characters
  },
});

const isPasswordStrong = computed(() => {
  const strongPasswordPattern =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
  return strongPasswordPattern.test(password.value);
});

const isEmailValid = computed(() => {
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailPattern.test(email.value);
});

async function handleRegister() {
  if (
    !firstName.value ||
    !lastName.value ||
    !email.value ||
    !password.value ||
    !checkPassword.value ||
    !dateOfBirth.value
  ) {
    errorMessage.value = "Complete all the necessary fields.";
    Swal.fire({
      position: "top-end",
      icon: "error",
      title: errorMessage.value,
      showConfirmButton: false,
      timer: 1800,
    });
    return;
  }

  if (password.value !== checkPassword.value) {
    errorMessage.value = "Passwords do not match.";
    Swal.fire({
      position: "top-end",
      icon: "error",
      title: errorMessage.value,
      showConfirmButton: false,
      timer: 1800,
    });
    return;
  }

  if (!isEmailValid.value) {
    errorMessage.value = "Invalid email format.";
    Swal.fire({
      position: "top-end",
      icon: "error",
      title: errorMessage.value,
      showConfirmButton: false,
      timer: 1800,
    });
    return;
  }

  if (!isPasswordStrong.value) {
    errorMessage.value =
      "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character.";
    Swal.fire({
      position: "top-end",
      icon: "error",
      title: errorMessage.value,
      showConfirmButton: false,
    });
    return;
  }

  const formattedDateOfBirth = formatISO(new Date(dateOfBirth.value)).split(
    "T"
  )[0];
  try {
    const resp = await registerCustomer({
      firstName: firstName.value,
      lastName: lastName.value,
      email: email.value,
      password: password.value,
      phoneNo: phoneNumber.value,
      dateOfBirth: formattedDateOfBirth,
    });
    if (resp.ok) {
      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "Your account have been created successfully",
        showConfirmButton: false,
        timer: 2000,
      });
      router.push("/login");
    }
  } catch (error: any) {
    console.error(error.message);
  }
}

function formatPhoneNumber(value: string): string {
  const digits = value.replace(/\D/g, "");
  const match = digits.match(/^(\d{4})(\d{3})(\d{3})$/);

  if (match) {
    return `${match[1]}-${match[2]}-${match[3]}`;
  }

  return digits;
}
</script>

<template>
  <div class="container">
    <div class="signup-container">
      <img src="@/assets/logo.png" alt="" />
      <div class="form">
        <div class="inputs">
          <CustomInput
            label-text="First name *"
            v-model:model-value="firstName"
          />
          <CustomInput
            label-text="Last name *"
            v-model:model-value="lastName"
          />
          <CustomInput label-text="Email *" v-model:model-value="email" />
          <CustomInput
            label-text="Password *"
            type="password"
            v-model:model-value="password"
          />
          <CustomInput
            label-text="Check password *"
            type="password"
            v-model:model-value="checkPassword"
          />
          <CustomPhoneNumberInput
            label-text="Phone Number *"
            v-model:model-value="formattedPhoneNumber"
          />
          <CustomInput
            label-text="Date of birth *"
            type="date"
            v-model:model-value="dateOfBirth"
          />
        </div>
        <CustomButton
          text="Signup"
          width="200"
          height="30"
          @click.prevent="handleRegister"
        />
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
    background-color: @dark-gray;
    padding-top: 10px;
    padding-bottom: 30px;
    border-radius: 48px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-evenly;

    width: 30vw;
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
