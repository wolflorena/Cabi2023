<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import { Doctor } from "@/data/types/Entities";
import { CustomerSidebarOptions } from "@/data/types/SidebarOptions";
import { getAllDoctors } from "@/services/doctor_service";
import { onMounted, ref } from "vue";
import DoctorsDropdown from "./appointment-content/DoctorsDropdown.vue";
import AppointmentContent from "./appointment-content/AppointmentContent.vue";

const doctors = ref<Doctor[]>([]);

const dropdownOpen = ref<boolean>(false);

function handleOnOpenDropdown() {
  dropdownOpen.value = !dropdownOpen.value;
}

async function loadDoctors() {
  await getAllDoctors().then((res: any) => {
    doctors.value = res;
  });
}

function handleDoctorSelected(doctor: Doctor) {
  console.log(doctor.appointments);
  dropdownOpen.value = false;
}

onMounted(() => {
  loadDoctors();
});
</script>

<template>
  <div class="container">
    <Sidebar :options="CustomerSidebarOptions" />
    <div class="profile-container">
      <div
        class="header"
        :class="{
          'is-open': dropdownOpen,
        }"
      >
        <DoctorsDropdown
          :doctors="doctors"
          :isOpen="dropdownOpen"
          @onOpenDropdown="handleOnOpenDropdown"
          @onDoctorSelected="handleDoctorSelected"
        />
      </div>
      <div class="appointment-content">
        <AppointmentContent />
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
@import (reference) "@/assets/styles.less";
.container {
  display: flex;
  justify-content: space-between;
  .profile-container {
    display: flex;
    flex-direction: column;
    width: 90vw;
    height: 100vh;

    .header {
      align-self: flex-end;
      background-color: @gray;
      height: 70px;
      width: 80%;
      border-bottom-left-radius: 40px;
      transition: border-bottom-left-radius 0.3s ease, opacity 0.3s ease;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      user-select: none;
    }

    .is-open {
      border-bottom-left-radius: 0;
    }

    .appointment-content {
      display: flex;
      height: 100%;
    }
  }
}
</style>
