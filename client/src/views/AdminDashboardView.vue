<script setup lang="ts">
import { ref, computed, watch, onMounted } from "vue";
import Sidebar from "@/components/Sidebar.vue";
import MonthScheduler from "@/components/MonthScheduler.vue";
import DayScheduler from "@/components/DayScheduler.vue";
import CustomCheckbox from "@/components/CustomCheckbox.vue";
import CustomDropdown from "@/components/CustomDropdown.vue";
import DatePicker from "@/components/DatePicker.vue";
import { AdminSidebarOptions } from "@/data/types/SidebarOptions";
import { getAllDoctors } from "@/services/doctor_service";
import type { Doctor } from "@/data/types/Entities";

const doctors = ref<Doctor[]>([]);
const selectedDoctors = ref<SelectedDoctor[]>([]);
const showCalendars = ref(true);
const showMonthCalendar = ref(true);
const daySelected = ref(new Date());
const selectedContent = ref("");

type SelectedDoctor = Doctor & { checked: boolean; color: string };

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
    res.forEach((element: any) => doctors.value.push(element));
  });
}

onMounted(() => {
  loadDoctors();
  console.log(doctors.value);
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
      <button class="add-button">
        <font-awesome-icon id="icon" icon="circle-plus" />
        <span>Add appointment</span>
      </button>

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
      v-show="showMonthCalendar"
      :selected-calendars="selectedDoctors"
      @toggle-calendar="toggleCalendar"
    />
    <DayScheduler
      v-show="!showMonthCalendar"
      :selected-calendars="selectedDoctors"
      :day-selected="daySelected"
      @toggle-calendar="toggleCalendar"
    />
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";
.container {
  display: flex;
  align-items: center;

  .settings {
    height: 100vh;
    width: 20vw;
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 20px;

    padding: 0 15px;
    .add-button {
      background-color: @green;
      border: none;
      border-radius: 20px;
      width: 19vw;
      height: 40px;
      cursor: pointer;
      margin-bottom: 20px;

      display: flex;
      align-items: center;
      gap: 20px;

      #icon {
        height: 25px;
        color: @gray;
      }

      span {
        font-size: 15px;
        color: @gray;
      }
    }
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
@/data/types/SidebarOptions@/data/types/SidebarOptions
