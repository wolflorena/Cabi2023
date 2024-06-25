<script setup lang="ts">
import { ref, computed, onMounted, watch } from "vue";
import { getAllForCalendar } from "@/services/appointments_service";
import type {
  SelectedDoctor,
  AppointmentCalendar,
  Vacation,
} from "@/data/types/Entities";
import LoadingSpinner from "./LoadingSpinner.vue";
import { getVacationsForCalendar } from "@/services/doctor_unavailability_service";

const props = withDefaults(
  defineProps<{
    selectedCalendars: SelectedDoctor[];
    appointmentsUpdate: boolean;
    fullWidth?: boolean;
    width?: string;
  }>(),
  {
    fullWidth: false,
    width: "65vw",
  }
);

const isLoading = ref(false);

type Day = {
  date: number;
  fullDate: string;
  isToday: boolean;
};

const currentDate = ref(new Date());
const appointments = ref<AppointmentCalendar[]>([]);
const vacations = ref<Vacation[]>([]);

// A list of the days of the week, used for displaying the calendar header
const weekdays = ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"];

const currentMonthYear = computed(() => {
  return currentDate.value.toLocaleString("default", {
    month: "long",
    year: "numeric",
  });
});

type HasDoctorIdAndTime = {
  doctorId: number;
  [key: string]: any;
};

function filterAndSortByTime<T extends HasDoctorIdAndTime>(
  data: T[],
  selectedDoctors: number[],
  getTime: (item: T) => string
): T[] {
  return data
    .filter((item) => selectedDoctors.includes(item.doctorId))
    .sort((a, b) => {
      const timeA = convertTime12to24(getTime(a));
      const timeB = convertTime12to24(getTime(b));

      return timeA - timeB;
    });
}

const selectedAppointments = computed(() => {
  const selectedDoctors = props.selectedCalendars
    .filter((doctor) => doctor.checked)
    .map((doctor) => doctor.id);

  return filterAndSortByTime(
    appointments.value,
    selectedDoctors,
    (item: AppointmentCalendar) => item.time
  );
});

const selectedUnavailabilities = computed(() => {
  const selectedDoctors = props.selectedCalendars
    .filter((doctor) => doctor.checked)
    .map((doctor) => doctor.id);

  return filterAndSortByTime(
    vacations.value,
    selectedDoctors,
    (item: Vacation) => item.startTime
  );
});

onMounted(() => {
  loadAppointments();
  loadUnavailabilities();
});

// Function that calculates the structure of days in the current month for display in the calendar
function weeksInMonth(date: Date): Day[][] {
  const month = date.getMonth();
  const year = date.getFullYear();
  const today = new Date();

  const firstDayOfMonth = new Date(year, month, 0).getDay();

  const days: Day[][] = [[]];
  let weekIndex = 0;

  for (let i = 0; i < firstDayOfMonth; i++) {
    days[weekIndex].push({ date: 0, fullDate: "", isToday: false });
  }

  const lastDay = new Date(year, month + 1, 0).getDate();
  for (let date = 1; date <= lastDay; date++) {
    const fullDate = new Date(year, month, date);
    const isToday =
      fullDate.toDateString() === today.toDateString() &&
      fullDate.getMonth() === today.getMonth() &&
      fullDate.getFullYear() === today.getFullYear();

    const previousFullDate = new Date(fullDate);
    previousFullDate.setDate(fullDate.getDate() + 1);
    const dayObj: Day = {
      date,
      fullDate: previousFullDate.toISOString().split("T")[0],
      isToday,
    };

    if (days[weekIndex].length === 7) {
      weekIndex++;
      days[weekIndex] = [];
    }

    days[weekIndex].push(dayObj);
  }

  while (days[weekIndex] && days[weekIndex].length < 7) {
    days[weekIndex].push({ date: 0, fullDate: "", isToday: false });
  }
  return days;
}

function navigate(monthIncrement: number): void {
  const current = currentDate.value;
  currentDate.value = new Date(
    current.getFullYear(),
    current.getMonth() + monthIncrement,
    1
  );
}

function getDoctorColor(doctorId: number): string {
  const doctor = props.selectedCalendars.find(
    (calendar) => calendar.id === doctorId
  );
  return doctor ? doctor.color : "#ffffff";
}

function convertTime12to24(time?: string): number {
  if (time) {
    const [timePart, modifier] = time.split(" ");
    let [hours, minutes] = timePart.split(":").map((num) => parseInt(num, 10));

    if (hours === 12) {
      hours = 0;
    }

    if (modifier === "PM") {
      hours += 12;
    }

    return hours * 60 + minutes;
  }
  return 0;
}

async function loadAppointments() {
  isLoading.value = true;
  await getAllForCalendar().then((res) => {
    appointments.value = res;
    isLoading.value = false;
  });
}

async function loadUnavailabilities() {
  isLoading.value = true;
  await getVacationsForCalendar().then((res) => {
    vacations.value = res;
    isLoading.value = false;
  });
}

// Function for toggling the calendar view
function toggleCalendar() {
  emit("toggle-calendar");
}

// Emitter for communication with the parent of the component
const emit = defineEmits(["toggle-calendar"]);

watch(
  () => props.appointmentsUpdate,
  () => {
    if (props.appointmentsUpdate) loadAppointments();
  }
);
</script>

<template>
  <div class="scheduler" :class="{ fullWidth }">
    <div class="header">
      <div class="controls">
        <button class="calendar-pick" @click="toggleCalendar">Month</button>
        <button @click="navigate(-1)" class="header-button">
          <font-awesome-icon icon="chevron-left" />
        </button>
        <button @click="navigate(1)" class="header-button">
          <font-awesome-icon icon="chevron-right" />
        </button>
      </div>

      <span class="current-date">{{ currentMonthYear }}</span>
    </div>

    <div class="calendar-container">
      <table class="calendar" v-if="!isLoading">
        <thead>
          <tr class="weekdays">
            <th v-for="day in weekdays" :key="day" class="weekday">
              {{ day }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="week in weeksInMonth(currentDate)" :key="week[0].fullDate">
            <td
              v-for="day in week"
              :key="day.fullDate"
              class="day"
              :class="{ outer: day.date === 0 }"
            >
              <div class="day-header">
                <span
                  class="date-number"
                  :class="{ today: day.isToday }"
                  v-show="day.date !== 0"
                >
                  {{ day.date }}
                </span>
              </div>

              <div class="appointments">
                <div
                  v-for="appointment in selectedAppointments.filter((appointment: AppointmentCalendar) => {
    return appointment.date === day.fullDate;
  })"
                  :key="appointment.appointmentId"
                  class="appointment"
                >
                  <div
                    id="circle"
                    :style="{
                      backgroundColor: getDoctorColor(appointment.doctorId),
                    }"
                  ></div>
                  <span id="hour">{{
                    appointment.time.substring(
                      0,
                      appointment.time.lastIndexOf(":")
                    )
                  }}</span>
                  <span id="details">{{ appointment.serviceName }}</span>
                </div>
                <div
                  v-for="vacation in selectedUnavailabilities.filter((vacation: Vacation) => {
                    return (
                      
                      (vacation.startDate <= day.fullDate &&
                        vacation.endDate >= day.fullDate)
                    );
                  })"
                  :key="vacation.doctorId"
                  class="appointment vacation"
                >
                  <div
                    id="circle"
                    :style="{
                      backgroundColor: getDoctorColor(vacation.doctorId),
                    }"
                  ></div>
                  <span v-if="vacation.startTime" id="hour">{{
                    vacation.startTime.substring(
                      0,
                      vacation.startTime.lastIndexOf(":")
                    ) + " - "
                  }}</span>

                  <span v-if="vacation.endTime" id="hour">{{
                    vacation.endTime.substring(
                      0,
                      vacation.endTime.lastIndexOf(":")
                    )
                  }}</span>
                  <span id="details">{{ vacation.reason }}</span>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>

      <LoadingSpinner v-else />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";
.scheduler {
  width: 65vw;
  position: absolute;
  top: 0;
  right: 0;

  .header {
    background-color: @gray;
    height: 5vh;
    width: 100%;
    border-bottom-left-radius: 40px;

    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    user-select: none;

    .controls {
      position: absolute;
      left: 2vw;
      .calendar-pick {
        background-color: transparent;
        color: @white;
        border: 1px solid @white;
        margin-right: 10px;
        padding: 5px;
      }

      .header-button {
        background-color: transparent;
        border: none;
        color: @white;
      }
    }

    .current-date {
      color: white;
    }
  }

  &.fullWidth {
    width: 80vw;

    .calendar-container {
      .calendar {
        .day {
          width: calc(90vw / 7);
        }
      }
    }
  }
}

.calendar-container {
  padding: 0px 10px;
  height: 88vh;

  .calendar {
    border-collapse: collapse;
    padding-right: 5px;

    .weekdays {
      .weekday {
        color: @font-dark-gray;
        user-select: none;
      }
    }
    .day {
      height: calc(88vh / 5);
      max-height: calc(88vh / 5);
      width: calc(65vw / 7);

      border: 1px solid @border-gray;
      position: relative;

      .day-header {
        width: 100%;
        position: absolute;
        top: 5%;
        text-align: center;

        .date-number {
          color: @font-gray;
          font-weight: bold;
          user-select: none;

          &.today {
            color: @green;
          }
        }
      }
      &.outer {
        background-color: @light-gray;
      }

      .appointments {
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        margin-top: 2vh;
        max-height: calc(88vh / 5 - 4vh);
        overflow-y: scroll;
        gap: 2px;
        .appointment {
          max-width: calc(65vw / 7);

          padding: 2px;
          display: flex;
          flex-wrap: wrap;
          align-items: center;
          gap: 0 2px;

          #circle {
            height: 10px;
            width: 10px;
            border-radius: 50%;
            background-color: @red;
          }

          #hour {
            color: @font-gray;
            font-weight: bold;
            font-size: 11px;
          }

          #details {
            color: @black;
            font-size: 12px;
            overflow: hidden;
            text-overflow: ellipsis;
          }

          &.vacation {
            background-color: rgba(0, 0, 0, 0.2);
            border-radius: 5px;
          }
        }

        &::-webkit-scrollbar {
          width: 5px;
        }

        &::-webkit-scrollbar-thumb {
          background: #424d65;
          border-radius: 5px;
        }

        &::-webkit-scrollbar-track {
          background-color: transparent;
        }

        scrollbar-width: thin;
        scrollbar-color: #424d65 transparent;
      }
    }
  }
}
</style>
