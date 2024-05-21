<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import { DoctorSidebarOptions } from "@/data/types/SidebarOptions";
import { onMounted, ref } from "vue";
import MonthScheduler from "@/components/MonthScheduler.vue";
import DayScheduler from "@/components/DayScheduler.vue";
import { SelectedDoctor } from "@/data/types/Entities";
import { getDoctorById } from "@/services/doctor_service";
import CustomButton from "@/components/CustomButton.vue";

const daySelected = ref(new Date());
const updateAppointments = ref(false);
const showMonthCalendar = ref(true);
const showUnavailability = ref(false);
const buttonText = ref("Set unavailability");

const selectedDoctor = ref<SelectedDoctor[]>([]);

async function getDoctorDetails() {
  await getDoctorById(2).then((res) => {
    selectedDoctor.value.push({ ...res, checked: true });
    console.log(selectedDoctor);
  });
}

function toggleCalendar() {
  showMonthCalendar.value = !showMonthCalendar.value;
  daySelected.value = new Date();
}

function setShowUnavailability() {
  showUnavailability.value = !showUnavailability.value;
  if (showUnavailability.value) {
    buttonText.value = "Show calendar";
  } else {
    buttonText.value = "Set unavailability";
  }
  showMonthCalendar.value = false;
}

onMounted(() => {
  getDoctorDetails();
});
</script>

<template>
  <div class="container">
    <Sidebar :options="DoctorSidebarOptions" />

    <MonthScheduler
      v-if="showMonthCalendar"
      :selected-calendars="selectedDoctor"
      @toggle-calendar="toggleCalendar"
      :appointments-update="updateAppointments"
      :full-width="true"
    />
    <DayScheduler
      v-else-if="!showUnavailability"
      :selected-calendars="selectedDoctor"
      :day-selected="daySelected"
      @toggle-calendar="toggleCalendar"
      :full-width="true"
    />
    <div class="unavailability" v-else></div>

    <div class="button">
      <CustomButton
        :text="buttonText"
        color="#84ce95"
        :is-main="false"
        height="30"
        width="150"
        @action-triggered="setShowUnavailability"
      />
    </div>
  </div>
</template>

<style scoped lang="less">
.button {
  position: absolute;
  top: 7px;
  right: 10px;
}
</style>
