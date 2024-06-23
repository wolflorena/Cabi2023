<script setup lang="ts">
import {
  AppointmentCalendar,
  SelectedDoctor,
  Vacation,
} from "@/data/types/Entities";
import { ref, computed, watch, onMounted } from "vue";
import { getAllForCalendar } from "@/services/appointments_service";
import LoadingSpinner from "./LoadingSpinner.vue";
import { getVacationsForCalendar } from "@/services/doctor_unavailability_service";

const props = withDefaults(
  defineProps<{
    selectedCalendars: SelectedDoctor[];
    daySelected: Date;
    fullWidth?: boolean;
  }>(),
  {
    fullWidth: false,
  }
);

type DayCalendarAppointment = AppointmentCalendar & { endTime: string };

const currentDate = ref(props.daySelected);
const isLoading = ref(false);

const timeSlots: string[] = createTimeSlots(9, 18);
const calendarsNumber = computed(() => props.selectedCalendars.length);
const currentDateKey = computed(() => currentDate.value.toISOString());
const appointments = ref<DayCalendarAppointment[]>([]);
const vacationsAllDay = ref<Vacation[]>([]);
const vacationsTimed = ref<Vacation[]>([]);

function createTimeSlots(startHour: number, endHour: number): string[] {
  const slots: string[] = [];
  for (let hour = startHour; hour < endHour; hour++) {
    slots.push(`${hour}:00`);
  }

  return slots;
}

function getOrdinalSuffix(day: number): string {
  if (day > 3 && day < 21) return "th";
  switch (day % 10) {
    case 1:
      return "st";
    case 2:
      return "nd";
    case 3:
      return "rd";

    default:
      return "th";
  }
}

watch(
  () => props.daySelected,
  (newDay) => {
    currentDate.value = newDay;
  }
);

const currentDay = computed(() => {
  const day = currentDate.value.getDate();
  const ordinalSuffix = getOrdinalSuffix(day);
  const formatter = new Intl.DateTimeFormat("en-US", {
    weekday: "long",
    month: "long",
    day: "numeric",
  });

  let formattedDay = formatter.format(currentDate.value);
  formattedDay = formattedDay.replace(
    new RegExp(`\\b${day}\\b`),
    `${day}${ordinalSuffix}`
  );

  return formattedDay;
});

function calculatePosition(startTime: string, endTime: string) {
  const startHour = parseInt(startTime.split(":")[0]);
  const startMinutes = parseInt(startTime.split(":")[1]);
  const endHour = parseInt(endTime.split(":")[0]);
  const endMinutes = parseInt(endTime.split(":")[1]);

  const startPosition = (startHour - 9) * 60 + startMinutes;
  const endPosition = (endHour - 9) * 60 + endMinutes;
  const duration = endPosition - startPosition;

  return {
    top: `${((startPosition - 60 * (startHour - 9)) * 100) / 60}%`,
    height: `${(duration / 60) * (88 / 5)}vh`,
  };
}

function getAppointmentPosition(appointment: DayCalendarAppointment) {
  return calculatePosition(appointment.time, appointment.endTime);
}

function getVacationPosition(vacation: Vacation) {
  if (!vacation.startTime || !vacation.endTime) {
    return { height: "100%", top: "0" };
  }
  return calculatePosition(vacation.startTime, vacation.endTime);
}

function computeItemStyle<T>(
  item: T,
  slot: string,
  getTime: (item: T) => string | undefined,
  getPosition: (item: T) => Record<string, any>
): Record<string, any> {
  const itemTime = getTime(item);
  const startHour = itemTime ? parseInt(itemTime.split(":")[0]) : 0;
  const slotStartHour = parseInt(slot.split(":")[0]);

  if (itemTime && startHour !== slotStartHour) {
    return { display: "none" };
  }
  return getPosition(item);
}

function computeAppointmentStyle(item: DayCalendarAppointment, slot: string) {
  return computeItemStyle(
    item,
    slot,
    (appointment) => appointment.time,
    getAppointmentPosition
  );
}

function computeVacationStyle(vacation: Vacation, slot: string) {
  return computeItemStyle(
    vacation,
    slot,
    (vacation) => vacation.startTime,
    getVacationPosition
  );
}

// function computeAppointmentStyle(item: DayCalendarAppointment, slot: string) {
//   const startHour = parseInt(item.time.split(":")[0]);
//   const slotStartHour = parseInt(slot.split(":")[0]);

//   if (startHour !== slotStartHour) {
//     return { display: "none" };
//   }
//   return getAppointmentStyle(item);
// }

// function computeVacationStyle(vacation: Vacation, slot: string) {
//   const startHour = vacation.startTime
//     ? parseInt(vacation.startTime.split(":")[0])
//     : 0;
//   const slotStartHour = parseInt(slot.split(":")[0]);

//   if (vacation.startTime && startHour !== slotStartHour) {
//     return { display: "none" };
//   }
//   return getVacationStyle(vacation);
// }

function navigate(dayIncrement: number): void {
  const current = currentDate.value;
  currentDate.value = new Date(
    current.getFullYear(),
    current.getMonth(),
    current.getDate() + dayIncrement,
    1
  );
}

function getAppointmentsForDayScheduler(calendar: number) {
  const filteredAppointments = appointments.value.filter((appointment) => {
    const condition =
      appointment.date === toLocalISOString(currentDate.value) &&
      selectedDoctors.value.includes(appointment.doctorId) &&
      appointment.doctorId === calendar;
    return condition;
  });
  return filteredAppointments;
}

function getVacationsForDayScheduler(
  vacations: Vacation[],
  calendar: number
): Vacation[] {
  return vacations.filter((vacation) => {
    return (
      vacation.startDate <= toLocalISOString(currentDate.value) &&
      vacation.endDate >= toLocalISOString(currentDate.value) &&
      selectedDoctors.value.includes(vacation.doctorId) &&
      vacation.doctorId === calendar
    );
  });
}

function getAllDayVacationsForCalendar(calendar: number): Vacation[] {
  return getVacationsForDayScheduler(vacationsAllDay.value, calendar);
}

function getTimedVacationsForCalendar(calendar: number): Vacation[] {
  return getVacationsForDayScheduler(vacationsTimed.value, calendar);
}

// Dacă currentDate.value este setată la 2024-02-20 01:00 în fusul orar UTC-3, atunci currentDate.value.toISOString() va returna 2024-02-19T04:00:00.000Z
function toLocalISOString(date: Date): string {
  const tzOffset = date.getTimezoneOffset() * 60000;
  const localISOTime = new Date(date.getTime() - tzOffset)
    .toISOString()
    .slice(0, -1);
  return localISOTime.split("T")[0];
}

const emit = defineEmits(["toggle-calendar"]);

function toggleCalendar() {
  emit("toggle-calendar");
}

const selectedDoctors = computed(() => {
  return props.selectedCalendars
    .filter((doctor) => doctor.checked)
    .map((doctor) => doctor.id);
});

const selectedCalendarsFiltered = computed(() => {
  return props.selectedCalendars.filter((doctor) => doctor.checked);
});

function getDoctorColor(doctorId: number): string {
  const doctor = props.selectedCalendars.find(
    (calendar) => calendar.id === doctorId
  );
  return doctor ? doctor.color : "#ffffff";
}

async function loadAppointments() {
  isLoading.value = true;
  await getAllForCalendar().then((res) => {
    const updatedAppointments = res.map(
      (appointment: DayCalendarAppointment) => {
        let [hour, minute] = appointment.time.split(":").map(Number);
        minute += appointment.finalDuration;

        while (minute >= 60) {
          hour += 1;
          minute -= 60;
        }

        appointment.endTime = `${hour.toString().padStart(2, "0")}:${minute
          .toString()
          .padStart(2, "0")}`;

        appointment.time = appointment.time.substring(
          0,
          appointment.time.lastIndexOf(":")
        );
        return appointment;
      }
    );

    appointments.value = updatedAppointments;
    isLoading.value = false;
  });
}

async function loadVacations() {
  isLoading.value = true;
  await getVacationsForCalendar().then((res) => {
    const allDay = res.filter((vacation: Vacation) => !vacation.startTime);
    const timed = res.filter((vacation: Vacation) => vacation.startTime);
    vacationsAllDay.value = allDay;
    vacationsTimed.value = timed;
    isLoading.value = false;
  });
}

onMounted(() => {
  loadAppointments();
  loadVacations();
});

function backgroundColorStyle(calendarId: number) {
  const color = getDoctorColor(calendarId) ?? "#ef4b4c";
  return `background-color: ${color}`;
}
</script>

<template>
  <div class="scheduler" :class="{ fullWidth }">
    <div class="header">
      <div class="controls">
        <button class="calendar-pick" @click="toggleCalendar">Day</button>
        <button @click="navigate(-1)" class="header-button">
          <font-awesome-icon icon="chevron-left" />
        </button>
        <button @click="navigate(1)" class="header-button">
          <font-awesome-icon icon="chevron-right" />
        </button>
      </div>

      <span class="current-date">{{ currentDay }}</span>
    </div>

    <div class="calendar-container" :key="currentDateKey">
      <table v-if="!isLoading" class="calendar">
        <thead>
          <tr class="calendar-header">
            <th class="column-header">Hour</th>
            <th
              v-for="calendar in selectedCalendarsFiltered"
              :key="calendar.id"
              class="column-header"
            >
              {{ "Dr. " + calendar.firstName + " " + calendar.lastName }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="slot in timeSlots" :key="slot" class="slots">
            <td class="slot">
              <span>
                {{ slot }}
              </span>
            </td>
            <td
              v-for="calendar in selectedCalendarsFiltered"
              :key="calendar.id"
              class="slot calendar"
            >
              <div v-if="slot === timeSlots[0]">
                <div
                  v-for="vacation in getAllDayVacationsForCalendar(calendar.id)"
                  :key="vacation.doctorId"
                  class="vacation all-day"
                >
                  <span>All Day</span>
                  <span>{{ vacation.reason }}</span>
                </div>
              </div>

              <div
                v-for="vacation in getTimedVacationsForCalendar(calendar.id)"
                :key="vacation.doctorId"
                class="vacation"
                :style="[computeVacationStyle(vacation, slot)]"
              >
                <span>{{
                  vacation.startTime.substring(
                    0,
                    vacation.startTime.lastIndexOf(":")
                  )
                }}</span>
                <span>{{ vacation.reason }}</span>
              </div>
              <div
                v-for="appointment in getAppointmentsForDayScheduler(
                  calendar.id
                )"
                :key="appointment.appointmentId"
                class="appointment"
                :style="[
                  computeAppointmentStyle(appointment, slot),
                  backgroundColorStyle(calendar.id),
                ]"
              >
                <span>{{ appointment.time }}</span>
                <span>{{ appointment.serviceName }}</span>
                <span>{{
                  `Patient: ${appointment.customerFirstName} ${appointment.customerLastName}`
                }}</span>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="loading-container" style="height: 100vh">
        <LoadingSpinner color="#30619b" />
      </div>
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

    .slot {
      &.calendar {
        width: calc((80vw - 80vw / 7));

        .appointment {
          font-size: 18px;
        }
      }
    }
  }
}

.calendar-container {
  padding: 0px 10px;
  overflow-y: scroll;
  max-height: 95vh;

  .calendar {
    border-collapse: collapse;
    padding-right: 5px;

    .calendar-header {
      color: @font-dark-gray;
      user-select: none;
    }
  }
  .slot {
    height: calc(88vh / 5);
    width: calc(65vw / 7);

    border: 1px solid @border-gray;
    border-bottom: none;
    position: relative;
    text-align: center;

    &.calendar {
      width: calc((65vw - 65vw / 7) / v-bind(calendarsNumber));
      position: relative;

      .appointment,
      .vacation {
        position: absolute;
        left: 50%;
        transform: translateX(-50%);
        width: 90%;
        overflow: hidden;
        border-radius: 5px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 1);

        display: flex;
        flex-direction: column;
        gap: 2px;
        justify-content: center;
        box-sizing: border-box;

        color: @white;
        font-size: 12px;
        font-weight: bold;
      }

      .vacation {
        background-color: #6e6e6e;

        &.all-day {
          position: absolute;
          top: 10px;
          height: 50px;
        }
      }
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
</style>
