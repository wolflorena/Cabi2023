<script setup lang="ts">
import { useRoute } from "vue-router";
import Sidebar from "../../components/Sidebar.vue";
import { AdminSidebarOptions } from "../../data/types/SidebarOptions";
import { ref, onMounted, watch } from "vue";
import { Patient } from "@/data/types/Entities";
import { getById } from "@/services/customer_service";
import router from "@/router";

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
        <div class="info-field">
          <label for="first-name">First Name</label>
          <input
            type="text"
            id="first-name"
            :value="patient?.firstName"
            readonly
          />
        </div>
        <div class="info-field">
          <label for="last-name">Last Name</label>
          <input
            type="text"
            id="last-name"
            :value="patient?.lastName"
            readonly
          />
        </div>
        <div class="info-field">
          <label for="email">Email</label>
          <input type="email" id="email" :value="patient?.email" readonly />
        </div>
        <div class="info-field">
          <label for="phone">Phone Number</label>
          <input type="tel" id="phone" :value="patient?.phoneNo" readonly />
        </div>
        <div class="info-field">
          <label for="dob">Date of Birth</label>
          <input type="text" id="dob" :value="patient?.dateOfBirth" readonly />
        </div>
        <div class="info-field">
          <label for="occupation">Occupation</label>
          <input
            type="text"
            id="occupation"
            :value="patient?.occupation"
            readonly
          />
        </div>
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

      .info-field {
        display: flex;
        flex-direction: column;

        label {
          color: @blue;
          margin-bottom: 5px;
          font-weight: bold;
          font-size: larger;
        }

        input {
          padding: 8px;
          height: 4vh;
          border: 0.5px solid @gray;
          border-radius: 4px;
          background-color: transparent;
          color: @black;
          font-size: 18px;
        }

        input[readonly] {
          cursor: not-allowed;
          opacity: 0.8;
        }
      }
    }
  }
}
</style>
