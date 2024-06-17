<script setup lang="ts">
import { useRoute } from "vue-router";
import Sidebar from "@/components/Sidebar.vue";
import { AdminSidebarOptions } from "@/data/types/SidebarOptions";
import { ref, onMounted } from "vue";
import { Patient } from "@/data/types/Entities";
import { getById, editStatus } from "@/services/customer_service";
import router from "@/router";
import CustomButton from "@/components/CustomButton.vue";
import InfoField from "@/components/InfoField.vue";
import { getUserIdAndToken } from "@/services/authentication_service";
import LoadingSpinner from "@/components/LoadingSpinner.vue";
import Swal from "sweetalert2";

const route = useRoute();
const patientId = ref(route.params.id);
const patient = ref<Patient>();
const isLoading = ref<boolean>(false);

async function getPatientDetails() {
  isLoading.value = true;
  const { userId, token } = getUserIdAndToken();

  await getById(+patientId.value, token).then((res: any) => {
    patient.value = res;
    console.log(patient.value?.accountStatus);
    isLoading.value = false;
  });
}

onMounted(() => {
  getPatientDetails();
});

function goBack() {
  router.back();
}

async function deactivateAccount() {
  await editStatus(+patientId.value, "INACTIVE").then((res) => {
    Swal.fire({
      title: "Account successfully deactivated.",
      icon: "success",
      confirmButtonText: "OK",
    }).then((result) => {
      if (result.isConfirmed) {
        getPatientDetails();
      }
    });
  });
}

async function reactivateAccount() {
  await editStatus(+patientId.value, "ACTIVE").then((res) => {
    Swal.fire({
      title: "Account successfully reactivated.",
      icon: "success",
      confirmButtonText: "OK",
    }).then((result) => {
      if (result.isConfirmed) {
        getPatientDetails();
      }
    });
  });
}

async function deleteAccount() {
  await editStatus(+patientId.value, "SUSPENDED").then((res) => {
    Swal.fire({
      title: "Account successfully suspended.",
      icon: "success",
      confirmButtonText: "OK",
    }).then((result) => {
      if (result.isConfirmed) {
        goBack();
      }
    });
  });
}
</script>

<template>
  <div class="container">
    <Sidebar :options="AdminSidebarOptions" />
    <div class="header">
      <span class="back">
        <button @click="goBack">
          <font-awesome-icon icon="chevron-left" id="icon" />
        </button>

        <p>Table view</p>
      </span>
      <span>Edit Patient Profile</span>
    </div>

    <div class="options" v-if="!isLoading">
      <div class="details">
        <h1>Account Information</h1>
        <div class="patient-info">
          <InfoField
            uuid="first-name"
            label="First Name"
            :input-value="patient?.firstName"
          />
          <InfoField
            uuid="last-name"
            label="Last Name"
            :input-value="patient?.lastName"
          />
          <InfoField uuid="email" label="Email" :input-value="patient?.email" />
          <InfoField
            uuid="phone"
            label="Phone Number"
            :input-value="patient?.phoneNo"
          />
          <InfoField
            uuid="dob"
            label="Date of Birth"
            :input-value="patient?.dateOfBirth"
          />
          <InfoField
            uuid="occupation"
            label="Occupation"
            :input-value="patient?.occupation"
          />
        </div>
      </div>
      <div class="actions">
        <h1>Account Actions</h1>
        <div class="patient-info">
          <div class="info-field">
            <CustomButton
              v-if="patient?.accountStatus === 'ACTIVE'"
              text="Deactivate Account"
              :is-main="false"
              height="70"
              width="300"
              font-size="20"
              @action-triggered="deactivateAccount()"
            />
            <CustomButton
              v-else
              text="Reactivate Account"
              :is-main="false"
              height="70"
              width="300"
              font-size="20"
              @action-triggered="reactivateAccount()"
            />
          </div>
          <div class="info-field">
            <CustomButton
              text="Suspend Account"
              height="70"
              width="300"
              font-size="20"
              @action-triggered="deleteAccount()"
            />
          </div>
        </div>
      </div>
    </div>
    <LoadingSpinner v-else />
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

      &.back {
        display: flex;
        align-items: center;
      }

      button {
        background-color: transparent;
        border: none;
        color: @white;
      }
    }
  }

  .options {
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 50px;
    padding-left: 50px;

    .details {
      display: flex;
      flex-direction: column;
      justify-content: center;

      h1 {
        color: @gray;
        text-transform: uppercase;
      }

      .patient-info {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        width: 100%;
        column-gap: 2vw;
        row-gap: 5vh;
      }
    }

    .actions {
      width: 76.5vw;
      display: flex;
      flex-direction: column;

      h1 {
        color: @gray;
        text-transform: uppercase;
      }

      .patient-info {
        display: flex;
        justify-content: space-between;
      }
    }
  }
}
</style>
