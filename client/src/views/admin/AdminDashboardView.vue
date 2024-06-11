<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import Sidebar from "@/components/Sidebar.vue";
import MonthScheduler from "@/components/MonthScheduler.vue";
import DayScheduler from "@/components/DayScheduler.vue";
import CustomCheckbox from "@/components/CustomCheckbox.vue";
import CustomDropdown from "@/components/CustomDropdown.vue";
import AddAppointmentModal from "@/components/AddAppointmentModal.vue";
import DatePicker from "@/components/DatePicker.vue";
import { AdminSidebarOptions } from "@/data/types/SidebarOptions";
import { createDoctorAccount, getAllDoctors } from "@/services/doctor_service";
import CustomModal from "@/components/CustomModal.vue";
import type { Doctor, SelectedDoctor } from "@/data/types/Entities";
import { createAppointment } from "@/services/appointments_service";
import ActionButton from "@/components/ActionButton.vue";
import Swal from "sweetalert2";

const doctors = ref<Doctor[]>([]);
const selectedDoctors = ref<SelectedDoctor[]>([]);
const showCalendars = ref(true);
const showModal = ref(false);
const showMonthCalendar = ref(true);
const daySelected = ref(new Date());
const selectedContent = ref("");
const updateAppointments = ref(false);
const showAddDoctorModal = ref(false);
const errorMessage = ref("");

const doctorEmail = ref("");
const doctorFirstName = ref("");
const doctorLasttName = ref("");

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
    await createAppointment(date, hour, doctor, service, patient).then(
      (res) => {
        if (res) {
          Swal.fire({
            titleText: "Appointment have been created successfully",
            icon: "success",
          });
        }
      }
    );
    updateAppointments.value = true;
    showModal.value = false;
  }
}

async function addDoctorAccount() {
  if (doctorEmail.value && doctorFirstName.value && doctorLasttName.value) {
    await createDoctorAccount(
      doctorEmail.value,
      doctorFirstName.value,
      doctorLasttName.value
    )
      .then((res) => {
        if (res) {
          Swal.fire({
            titleText: "An email has been sent to the doctor.",
            icon: "success",
          });
        }
      })
      .catch((error) => {
        Swal.fire({
          titleText: error.message,
          icon: "error",
        });
      });
  } else {
    errorMessage.value = "All fields are required";
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
      <ActionButton
        button-style="full"
        icon-token="circle-plus"
        @click="prepareAddAppointmentModal"
      />
      <ActionButton
        button-style="full"
        icon-token="circle-plus"
        text="Add doctor account"
        @click="showAddDoctorModal = true"
      />
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

  <CustomModal
    :show="showAddDoctorModal"
    title="Create Doctor Account"
    @button2="showAddDoctorModal = false"
    @button1="addDoctorAccount"
  >
    <div class="selection">
      <div class="option">
        <label>Doctor Email</label>
        <input type="text" v-model="doctorEmail" />
      </div>

      <div class="option">
        <label>Doctor First Name</label>
        <input type="text" v-model="doctorFirstName" />
      </div>

      <div class="option">
        <label>Doctor Last Name</label>
        <input type="text" v-model="doctorLasttName" />
      </div>
    </div>
  </CustomModal>
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

    input {
      width: 160px;
      background-color: @font-gray;
      border-radius: 10px;
      outline: none;
      color: @font-darker-gray;
      font-size: 15px;
      padding: 5px;
      border: none;
    }
  }
}
</style>
