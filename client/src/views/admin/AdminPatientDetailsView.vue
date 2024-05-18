<script setup lang="ts">
import { useRoute } from "vue-router";
import Sidebar from "@/components/Sidebar.vue";
import { AdminSidebarOptions } from "@/data/types/SidebarOptions";
import { ref, onMounted, watch } from "vue";
import { Patient } from "@/data/types/Entities";
import { getById } from "@/services/customer_service";
import router from "@/router";
import InfoField from "@/components/InfoField.vue";

const route = useRoute();
const patientId = ref(route.params.id);
const patient = ref<Patient>();

async function getPatientDetails(patientId: number) {
  await getById(patientId).then((res) => {
    patient.value = res;
  });
}

onMounted(() => {
  getPatientDetails(+patientId.value);
});

function goBack() {
  router.back();
}
</script>

<template>
  <div class="container">
    <Sidebar :options="AdminSidebarOptions" />
    <div class="header">
      <span>
        <button @click="goBack">
          <font-awesome-icon icon="chevron-left" id="icon" />
        </button>

        Table view</span
      >
      <span>View Patient Profile</span>
    </div>

    <div class="details">
      <img src="../../assets/default-avatar.png" alt="" />
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

      button {
        background-color: transparent;
        border: none;
        color: @white;
      }
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

    .patient-info {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      width: 90%;
      column-gap: 2vw;
      row-gap: 5vh;
    }
  }
}
</style>
