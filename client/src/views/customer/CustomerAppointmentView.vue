<script setup lang="ts">
import CustomDropdown from "@/components/CustomDropdown.vue";
import InputDropdown from "@/components/InputDropdown.vue";
import Sidebar from "@/components/Sidebar.vue";
import { Doctor } from "@/data/types/Entities";
import { CustomerSidebarOptions } from "@/data/types/SidebarOptions";
import { getAllDoctors } from "@/services/doctor_service";
import { ref } from "vue";
import DoctorsDropdown from "./appointment-content/DoctorsDropdown.vue";
const doctors = ref<Doctor[]>([]);
async function loadDoctors() {
  await getAllDoctors().then((res: any) => {
    doctors.value = res;
  });
}
</script>

<template>
  <div class="container">
    <Sidebar :options="CustomerSidebarOptions" />
    <div class="profile-container">
      <div class="header">
        <DoctorsDropdown :doctors="doctors" :isOpen="true" />
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

      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      user-select: none;
    }
  }
}
</style>
