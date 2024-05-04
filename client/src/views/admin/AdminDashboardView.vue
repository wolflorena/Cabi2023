<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import Sidebar from "@/components/Sidebar.vue";
import MonthScheduler from "@/components/MonthScheduler.vue";
import DayScheduler from "@/components/DayScheduler.vue";
import CustomCheckbox from "@/components/CustomCheckbox.vue";
import AddButton from "@/components/AddButton.vue";
import CustomDropdown from "@/components/CustomDropdown.vue";
import CustomModal from "@/components/CustomModal.vue";
import DatePicker from "@/components/DatePicker.vue";
import { AdminSidebarOptions } from "@/data/types/SidebarOptions";
import {
  getAllDoctors,
  getAvailableDates,
  getAvailableHours,
} from "@/services/doctor_service";
import { getAllServices } from "@/services/service_service";
import type {
  Doctor,
  SelectedDoctor,
  Service,
  Patient,
} from "@/data/types/Entities";
import { createAppointment } from "@/services/appointments_service";
import { getAllPatients } from "@/services/customer_service";

const doctors = ref<Doctor[]>([]);
const patients = ref<Patient[]>([]);
const services = ref<Service[]>([]);
const selectedDoctors = ref<SelectedDoctor[]>([]);
const showCalendars = ref(true);
const showModal = ref(false);
const showMonthCalendar = ref(true);
const daySelected = ref(new Date());
const selectedContent = ref("");

const selectedDoctor = ref();
const selectedService = ref();
const availableDates = ref<string[]>([]);
const selectedDate = ref();
const selectedHour = ref();
const selectedPatient = ref();
const availableHours = ref<string[]>([]);

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
  loadServices();
  fetchPatients();
}

function closeModal() {
  showModal.value = false;
  selectedDate.value = "";
  selectedHour.value = "";
  selectedDoctor.value = "";
  selectedService.value = "";
  selectedPatient.value = "";
}

async function loadServices() {
  await getAllServices().then((res: any) => {
    services.value = res;
  });
}

//TODO: @wolflorena create a modal date picker for selectig the date for appointment
async function fetchAvailableDates() {
  await getAvailableDates(selectedDoctor.value, selectedService.value).then(
    (res: any) => {
      availableDates.value = res;
    }
  );
  console.log(availableDates.value);
}

async function fetchAvailableHours() {
  await getAvailableHours(
    selectedDoctor.value,
    selectedService.value,
    selectedDate.value
  ).then((res: any) => {
    availableHours.value = res;
  });
  console.log(availableHours.value);
}

async function fetchPatients() {
  await getAllPatients().then((res) => (patients.value = res));
}

async function addAppointment() {
  if (
    selectedDate.value &&
    selectedHour.value &&
    selectedDoctor.value &&
    selectedService.value
  ) {
    await createAppointment(
      selectedDate.value,
      selectedHour.value,
      selectedDoctor.value,
      selectedService.value,
      selectedPatient.value
    ).then((res) => console.log(res));
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
    />
    <DayScheduler
      v-else
      :selected-calendars="selectedDoctors"
      :day-selected="daySelected"
      @toggle-calendar="toggleCalendar"
    />
  </div>

  <CustomModal
    :show="showModal"
    @button2="closeModal"
    @button1="addAppointment"
    title="New appointment"
  >
    <div class="selection">
      <div class="option">
        <label>Select doctor *</label>
        <select v-model="selectedDoctor">
          <option disabled value="">Select doctor</option>
          <option v-for="doctor in doctors" :key="doctor.id" :value="doctor.id">
            Dr. {{ doctor.firstName }} {{ doctor.lastName }}
          </option>
        </select>
      </div>

      <div class="option">
        <label>Select type of treatment *</label>
        <select v-model="selectedService" @change="fetchAvailableDates">
          <option disabled value="">Select type of treatment</option>
          <option
            v-for="service in services"
            :key="service.serviceId"
            :value="service.serviceId"
          >
            {{ service.name }}
          </option>
        </select>
      </div>

      <div class="option">
        <label>Select date *</label>
        <select v-model="selectedDate" @change="fetchAvailableHours">
          <option disabled value="">Select date</option>
          <option v-for="date in availableDates" :key="date" :value="date">
            {{ date }}
          </option>
        </select>
      </div>

      <div class="option">
        <label>Select hour *</label>
        <select v-model="selectedHour">
          <option disabled value="">Select hour</option>
          <option v-for="hour in availableHours" :key="hour" :value="hour">
            {{ hour }}
          </option>
        </select>
      </div>

      <div class="option">
        <label>Select patient name</label>
        <select v-model="selectedPatient">
          <option disabled value="">Select patient</option>
          <option
            v-for="patient in patients"
            :key="patient.customerId"
            :value="patient.customerId"
          >
            {{ patient.firstName }} {{ patient.lastName }}
          </option>
        </select>
      </div>
    </div>
  </CustomModal>
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
.selection {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;

  .option {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 20px;

    label {
      color: @sugar;
      font-size: 18px;
    }

    select {
      width: 160px;
      background-color: @font-gray;
      border-radius: 10px;
      outline: none;
      color: @font-darker-gray;
      font-size: 15px;
      padding: 2px;

      &:focus {
        background-color: @sugar;
      }
    }
  }
}
</style>
