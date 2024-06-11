<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import { Appointment, Doctor } from "@/data/types/Entities";
import { CustomerSidebarOptions } from "@/data/types/SidebarOptions";
import { onMounted, ref, watch } from "vue";
import DoctorsDropdown from "./appointment-content/DoctorsDropdown.vue";
import AppointmentContent from "./appointment-content/AppointmentContent.vue";
import { useLoadAppointments } from "@/store/useLoadAppointments";
import { useUserProfile } from "@/store/useUserProfile";
import { getUserIdAndToken } from "@/services/authentication_service";

const {
  doctors,
  selectedDoctor,
  calendarDate,
  calendarViewtype,
  appointments,
  unavailabilities,
  loadDoctors,
  fetchAppointments,
} = useLoadAppointments();

const { fetchUserProfile, retrieveUserAvatar } = useUserProfile();

const dropdownOpen = ref<boolean>(false);

function handleOnOpenDropdown(event: Event) {
  event.stopPropagation();
  dropdownOpen.value = !dropdownOpen.value;
}

function handleDoctorSelected(doctor: Doctor) {
  selectedDoctor.value = doctor;
  dropdownOpen.value = false;
}

function handleDateChange(date: Date) {
  calendarDate.value = date;
}

function handleViewTypeChange(type: string) {
  calendarViewtype.value = type;
}

async function handleUpdateAppointments() {
  await fetchAppointments(true);
}

watch([selectedDoctor, calendarDate, calendarViewtype], async () => {
  await fetchAppointments();
});

onMounted(async () => {
  if (doctors.value === null) {
    await loadDoctors();
    await fetchUserProfile();
    await retrieveUserAvatar();
  } else {
    await fetchAppointments();
  }
});
</script>

<template>
  <div class="container">
    <Sidebar :options="CustomerSidebarOptions" />
    <div class="appointment-container">
      <div
        class="header"
        :class="{
          'is-open': dropdownOpen,
        }"
        @click="handleOnOpenDropdown($event)"
      >
        <DoctorsDropdown
          :doctors="doctors"
          :isOpen="dropdownOpen"
          :selectedDoctor="selectedDoctor"
          @onOpenDropdown="handleOnOpenDropdown($event)"
          @onDoctorSelected="handleDoctorSelected"
        />
      </div>
      <div class="appointment-content">
        <AppointmentContent
          :selectedDoctor="selectedDoctor"
          :appointments="appointments"
          :calendarDate="calendarDate"
          :unavailabilites="unavailabilities"
          :calendarViewType="calendarViewtype"
          @onDateChange="handleDateChange"
          @onViewTypeChange="handleViewTypeChange"
          @on-appointment-created="handleUpdateAppointments"
        />
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
@import (reference) "@/assets/styles.less";
.container {
  display: flex;
  justify-content: space-between;

  .appointment-container {
    display: flex;
    flex-direction: column;
    width: 90vw;
    height: 100vh;

    .header {
      cursor: pointer;
      align-self: flex-end;
      background-color: @gray;
      min-height: 70px;
      width: 75vw;
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
      flex-direction: column;
      height: 100%;
    }
  }
}
</style>
