<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import TableHeaderButton from "@/components/TableHeaderButton.vue";
import { Doctor } from "@/data/types/Entities";
import { DoctorSidebarOptions } from "@/data/types/SidebarOptions";
import { getDoctorById, updateDoctor } from "@/services/doctor_service";
import { onMounted, ref, watch } from "vue";
import InfoField from "@/components/InfoField.vue";
import CustomButton from "@/components/CustomButton.vue";

const viewOnly = ref(true);
const doctor = ref<Doctor>();
const firstName = ref();
const lastName = ref();
const email = ref();
const phoneno = ref();
const address = ref();
const dateOfEmployment = ref();

async function getDoctorDetails() {
  await getDoctorById(1).then((res) => {
    doctor.value = res;
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
</script>

<template>
  <div class="container">
    <Sidebar :options="DoctorSidebarOptions" />

    <div class="header">
      <TableHeaderButton
        label="View Profile"
        :active="viewOnly"
        @click="viewOnly = true"
      />
      <TableHeaderButton
        label="Edit Profile"
        :active="!viewOnly"
        @click="viewOnly = false"
      />
    </div>

    <div class="details">
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
    gap: 12vh;

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
}
</style>
