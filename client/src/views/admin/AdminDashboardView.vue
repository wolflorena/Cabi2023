<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import Sidebar from "@/components/Sidebar.vue";
import MonthScheduler from "@/components/MonthScheduler.vue";
import DayScheduler from "@/components/DayScheduler.vue";
import CustomCheckbox from "@/components/CustomCheckbox.vue";
import AddButton from "@/components/AddButton.vue";
import CustomDropdown from "@/components/CustomDropdown.vue";
import AddAppointmentModal from "@/components/AddAppointmentModal.vue";
import DatePicker from "@/components/DatePicker.vue";
import { AdminSidebarOptions } from "@/data/types/SidebarOptions";
import { getAllDoctors } from "@/services/doctor_service";

import type { Doctor, SelectedDoctor } from "@/data/types/Entities";
import { createAppointment } from "@/services/appointments_service";

const doctors = ref<Doctor[]>([]);
const selectedDoctors = ref<SelectedDoctor[]>([]);
const showCalendars = ref(true);
const showModal = ref(false);
const showMonthCalendar = ref(true);
const daySelected = ref(new Date());
const selectedContent = ref("");
const updateAppointments = ref(false);

type Day = {
  date: number;
  fullDate: string;
  currentMonth: boolean;
  isWeekend: boolean;
};

function selectDoctors(doctor: Doctor) {
  if (
    !selectedDoctors.value.some(
      (selectedDoctor) => doctor.id === selectedDoctor.id
    )
  )
    selectedDoctors.value.push({
      ...doctor,
      checked: false,
      color: generateRandomPurpleColor(),
    });
}

watch(
  selectedDoctors,
  () => {
    sortSelectedDoctors();
  },
  { deep: true }
);

function sortSelectedDoctors() {
  selectedDoctors.value.sort((a, b) => {
    if (a.checked && !b.checked) {
      return -1;
    } else if (!a.checked && b.checked) {
      return 1;
    } else {
      return a.firstName.localeCompare(b.firstName);
    }
  });
}

function toggleCalendars() {
  showCalendars.value = !showCalendars.value;
}

function toggleCalendar() {
  showMonthCalendar.value = !showMonthCalendar.value;
  daySelected.value = new Date();
}

function generateRandomPurpleColor(): string {
  const red = Math.floor(Math.random() * (255 - 128) + 128).toString(16);
  const green = Math.floor(Math.random() * 101).toString(16);
  const blue = Math.floor(Math.random() * (255 - 128) + 128).toString(16);

  const color = `#${red.padStart(2, "0")}${green.padStart(
    2,
    "0"
  )}${blue.padStart(2, "0")}`;

  return color;
}

function openDailyCalendar(day: Day) {
  daySelected.value = new Date(day.fullDate);
  showMonthCalendar.value = false;
}

const handleContentChanged = (newContent: string) => {
  selectedContent.value = newContent;
};

async function loadDoctors() {
  await getAllDoctors().then((res: any) => {
    doctors.value = res;
  });
}

function prepareAddAppointmentModal() {
  showModal.value = true;
}

function closeModal() {
  showModal.value = false;
}

async function addAppointment(
  date: string,
  hour: string,
  doctor: number,
  service: number,
  patient: number
) {
  updateAppointments.value = false;
  if (date && hour && doctor && service && patient) {
    await createAppointment(date, hour, doctor, service, patient).then((res) =>
      console.log(res)
    );
    updateAppointments.value = true;
    showModal.value = false;
  }
}

onMounted(() => {
  loadDoctors();
});
</script>

<template>
  <div class="container">
    <Sidebar
      :options="AdminSidebarOptions"
      @content-changed="handleContentChanged"
    />
    <span> {{ selectedContent }}</span>
    <div class="settings">
      <AddButton @click="prepareAddAppointmentModal" />
      <DatePicker @select-day="openDailyCalendar" />

      <CustomDropdown
        :doctors="doctors"
        label="Add calendar..."
        placeholder="Search for a doctor"
        @select="selectDoctors"
      />

      <div class="calendars">
        <div class="title">
          <h4>Available calendars</h4>
          <button @click="toggleCalendars" v-show="showCalendars">
            <font-awesome-icon icon="chevron-up" id="icon" />
          </button>
          <button @click="toggleCalendars" v-show="!showCalendars">
            <font-awesome-icon icon="chevron-down" id="icon" />
          </button>
        </div>
        <div class="doctors">
          <div
            class="checkbox-wrapper-4"
            v-for="doctor in selectedDoctors"
            :key="doctor.id"
            v-show="showCalendars"
          >
            <CustomCheckbox
              v-model="doctor.checked"
              :text="`Dr. ${doctor.firstName} ${doctor.lastName}`"
              :color="doctor.color"
              :uuid="`checkbox-doctor-${doctor.id}`"
            />
          </div>
        </div>
      </div>
    </div>
    <MonthScheduler
      v-if="showMonthCalendar"
      :selected-calendars="selectedDoctors"
      @toggle-calendar="toggleCalendar"
      :appointments-update="updateAppointments"
    />
    <DayScheduler
      v-else
      :selected-calendars="selectedDoctors"
      :day-selected="daySelected"
      @toggle-calendar="toggleCalendar"
    />
  </div>

  <AddAppointmentModal
    :visible="showModal"
    @add-appointment="addAppointment"
    @close="closeModal"
  />
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";
.container {
  display: flex;
  align-items: center;
  height: 100vh;

  .settings {
    height: 100vh;
    width: 20vw;
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 20px;

    padding: 0 15px;

    .calendars {
      color: @font-dark-gray;
      font-weight: bolder;
      width: 19vw;

      .title {
        display: flex;
        align-items: center;
        justify-content: space-between;

        button {
          background-color: transparent;
          border: none;

          #icon {
            color: @font-dark-gray;
          }
        }
      }
      .doctors {
        height: 10vh;
        display: flex;
        flex-direction: column;

        max-height: 20vh;
        overflow: scroll;
        scrollbar-color: @gray;

        .checkbox-wrapper-4 * {
          box-sizing: border-box;
        }
      }
    }
  }
}
</style>
