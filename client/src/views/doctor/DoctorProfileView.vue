<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import TableHeaderButton from "@/components/TableHeaderButton.vue";
import { Doctor } from "@/data/types/Entities";
import { DoctorSidebarOptions } from "@/data/types/SidebarOptions";
import {
  changeDoctorPassword,
  getDoctorById,
  updateDoctor,
} from "@/services/doctor_service";
import { computed, onMounted, ref, watch } from "vue";
import InfoField from "@/components/InfoField.vue";
import CustomButton from "@/components/CustomButton.vue";
import LoadingSpinner from "@/components/LoadingSpinner.vue";
import Swal from "sweetalert2";

const viewOnly = ref(true);
const security = ref(false);
const doctor = ref<Doctor>();
const firstName = ref();
const lastName = ref();
const email = ref();
const phoneno = ref();
const address = ref();
const dateOfEmployment = ref();
const isLoading = ref(false);

const currentPassword = ref<string>("");
const newPassword = ref<string>("");
const confirmPassword = ref<string>("");

const passwordsMatch = computed(() => {
  return newPassword.value === confirmPassword.value;
});

async function getDoctorDetails() {
  isLoading.value = true;
  await getDoctorById(1).then((res) => {
    doctor.value = res;
    isLoading.value = false;
  });
}

watch(doctor, (newDoctor) => {
  if (newDoctor) {
    firstName.value = newDoctor.firstName;
    lastName.value = newDoctor.lastName;
    email.value = newDoctor.email;
    phoneno.value = newDoctor.phoneNo;
    address.value = newDoctor.address;
    dateOfEmployment.value = newDoctor.dateOfEmployment;
  }
});

onMounted(() => {
  getDoctorDetails();
});

async function updateProfile() {
  await updateDoctor(
    1,
    firstName.value,
    lastName.value,
    email.value,
    phoneno.value,
    address.value,
    dateOfEmployment.value
  );
}

async function handlePasswordChange() {
  if (passwordsMatch.value) {
    isLoading.value = true;
    try {
      await changeDoctorPassword(1, currentPassword.value, newPassword.value);
      currentPassword.value = "";
      newPassword.value = "";
      confirmPassword.value = "";
      isLoading.value = false;
      Swal.fire({
        titleText: "Password changed successfully",
        icon: "success",
      });
    } catch (error: any) {
      Swal.fire({
        titleText: error.message,
        icon: "error",
      });
      isLoading.value = false;
    }
  } else {
    Swal.fire({
      titleText: "Passwords doesn't match",
      icon: "error",
    });
    currentPassword.value = "";
    newPassword.value = "";
    confirmPassword.value = "";
  }
}
</script>

<template>
  <div class="container">
    <Sidebar :options="DoctorSidebarOptions" />

    <div class="header">
      <TableHeaderButton
        label="View Profile"
        :active="viewOnly"
        @click="
          {
            viewOnly = true;
            security = false;
          }
        "
      />
      <TableHeaderButton
        label="Edit Profile"
        :active="!viewOnly && !security"
        @click="
          {
            viewOnly = false;
            security = false;
          }
        "
      />
      <TableHeaderButton
        label="Security Settings"
        :active="security && !viewOnly"
        @click="
          {
            viewOnly = false;
            security = true;
          }
        "
      />
    </div>

    <div v-if="!isLoading && !security" class="details">
      <img src="../../assets/default-avatar.png" alt="" />
      <div class="doctor-info">
        <InfoField
          uuid="first-name"
          label="First Name"
          v-model:input-value="firstName"
          :is-readonly="viewOnly"
        />
        <InfoField
          uuid="last-name"
          label="Last Name"
          v-model:input-value="lastName"
          :is-readonly="viewOnly"
        />
        <InfoField
          uuid="email"
          label="Email"
          v-model:input-value="email"
          :is-readonly="viewOnly"
        />
        <InfoField
          uuid="phone"
          label="Phone Number"
          v-model:input-value="phoneno"
          :is-readonly="viewOnly"
        />
        <InfoField
          uuid="address"
          label="Address"
          v-model:input-value="address"
          :is-readonly="viewOnly"
        />
        <InfoField
          uuid="doe"
          label="Date of Employment"
          v-model:input-value="dateOfEmployment"
          :is-readonly="viewOnly"
          type="text"
        />
      </div>

      <div v-if="!viewOnly" class="button">
        <CustomButton
          text="Save"
          :is-main="false"
          height="50"
          width="100"
          font-size="20"
          @action-triggered="updateProfile"
        />
      </div>
    </div>
    <div
      v-else-if="isLoading"
      class="details"
      style="width: 85vw; position: relative"
    >
      <LoadingSpinner />
    </div>

    <div v-else class="password-change">
      <div class="info-fields-group">
        <InfoField
          uuid="currentPassword"
          label="Current Password"
          v-model:inputValue="currentPassword"
          :isReadonly="false"
          font-size="30"
          type="password"
          variant="ACCOUNT_INFORMATION"
        />
        <InfoField
          uuid="newPassword"
          label="New Password"
          v-model:inputValue="newPassword"
          :isReadonly="false"
          font-size="30"
          type="password"
          variant="ACCOUNT_INFORMATION"
        />
        <InfoField
          uuid="confirmPassword"
          label="Confirm Password"
          v-model:inputValue="confirmPassword"
          :isReadonly="false"
          font-size="30"
          type="password"
          variant="ACCOUNT_INFORMATION"
        />

        <CustomButton
          :isMain="false"
          text="Save Changes"
          width="240"
          height="60"
          fontSize="18"
          @click="handlePasswordChange"
        />
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;
  position: relative;

  .header {
    position: absolute;
    top: 0;
    right: 0;
    background-color: @gray;
    height: 7vh;
    width: 50vw;
    border-bottom-left-radius: 40px;

    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 30px;

    span {
      color: @white;
      font-size: large;
    }
  }

  .details {
    width: 85vw;
    height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 9vh;

    img {
      height: 200px;
    }

    .doctor-info {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      width: 90%;
      column-gap: 2vw;
      row-gap: 5vh;
    }

    .button {
      position: absolute;
      bottom: 50px;
    }
  }

  .password-change {
    margin-left: 10vw;
    .info-fields-group {
      width: 70vw;
      height: 100vh;
      gap: 60px;
      display: flex;
      justify-content: center;
      align-items: space-between;
      flex-direction: column;
    }
  }
}
</style>
