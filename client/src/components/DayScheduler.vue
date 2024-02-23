<script setup lang="ts">
import { ref, computed, watch } from "vue";

const props = withDefaults(
  defineProps<{
    selectedCalendars: any[];
    daySelected: Date;
  }>(),
  {}
);

const currentDate = ref(props.daySelected);

const timeSlots: string[] = createTimeSlots(9, 17);
const calendars = ref(["Ana", "Doctor 1"]);
const calendarsNumber = computed(() => calendars.value.length);
const currentDateKey = computed(() => currentDate.value.toISOString());

type Appointment = {
  startTime: string;
  endTime: string;
  description: string;
  patient: string;
  doctor: string;
  day: string; //format: "YYYY-DD-MM"
};

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

const appointments = ref([
  {
    id: "1",
    startTime: "09:00AM",
    endTime: "10:00",
    description: "Example Appointment 2",
    patient: "gigel",
    doctor: "Ana",
    day: "2024-02-19",
  },
  {
    id: "2",
    startTime: "10:20",
    endTime: "11:00",
    description: "Example Appointment 1",
    patient: "gigel",
    doctor: "Doctor 1",
    day: "2024-03-18",
  },
  {
    id: "2",
    startTime: "13:30",
    endTime: "14:00",
    description: "Example Appointment 2",
    patient: "gigel",
    doctor: "Doctor 1",
    day: "2024-02-20",
  },
]);

function getAppointmentStyle(appointment: Appointment) {
  const startHour = parseInt(appointment.startTime.split(":")[0]);
  const startMinutes = parseInt(appointment.startTime.split(":")[1]);
  const endHour = parseInt(appointment.endTime.split(":")[0]);
  const endMinutes = parseInt(appointment.endTime.split(":")[1]);

  const startPosition = (startHour - 9) * 60 + startMinutes;

  const endPosition = (endHour - 9) * 60 + endMinutes;

  const duration = endPosition - startPosition;
  return {
    top: `${((startPosition - 60 * (startHour - 9)) * 100) / 60}%`,
    height: `${(duration / 60) * (88 / 5)}vh`,
  };
}

function computeStyle(appointment: Appointment, slot: string) {
  const startHour = parseInt(appointment.startTime.split(":")[0]);
  const slotStartHour = parseInt(slot.split(":")[0]);

  if (startHour !== slotStartHour) {
    return { display: "none" };
  }
  return getAppointmentStyle(appointment);
}

function navigate(dayIncrement: number): void {
  const current = currentDate.value;
  currentDate.value = new Date(
    current.getFullYear(),
    current.getMonth(),
    current.getDate() + dayIncrement,
    1
  );
}

const getAppointmentsForCalendar = computed(() => {
  return (calendar: string) => {
    return appointments.value.filter((appointment: Appointment) => {
      return (
        selectedDoctorsNames.value.includes(appointment.doctor) &&
        appointment.day === toLocalISOString(currentDate.value) &&
        appointment.doctor === calendar
      );
    });
  };
});

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

const selectedDoctorsNames = computed(() => {
  return props.selectedCalendars
    .filter((calendar) => calendar.checked)
    .map((calendar) => calendar.name);
});

const selectedCalendarsFiltered = computed(() => {
  return props.selectedCalendars.filter((calendar) => calendar.checked);
});

function getDoctorColor(doctorName: string): string {
  const doctor = props.selectedCalendars.find(
    (calendar) => calendar.name === doctorName
  );
  return doctor ? doctor.color : "#ffffff";
}
</script>

<template>
  <div class="scheduler">
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
      <table class="calendar">
        <thead>
          <tr class="calendar-header">
            <th class="column-header">Hour</th>
            <th
              v-for="calendar in selectedCalendarsFiltered"
              class="column-header"
            >
              {{ calendar.name }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="slot in timeSlots" class="slots">
            <td class="slot">
              <span>
                {{ slot }}
              </span>
            </td>
            <td
              v-for="calendar in selectedCalendarsFiltered"
              class="slot calendar"
            >
              <div
                v-for="appointment in getAppointmentsForCalendar(calendar.name)"
                class="appointment"
                :style="[
                  computeStyle(appointment, slot),
                  `background-color: ${getDoctorColor(calendar.name)}`,
                ]"
              >
                <span>{{ appointment.startTime }}</span>
                <span>{{ appointment.description }}</span>
                <span>{{ `Patient: ${appointment.patient}` }}</span>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
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
}

.calendar-container {
  padding: 0px 10px;
  overflow: scroll;
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

      .appointment {
        position: absolute;
        left: 50%;
        transform: translateX(-50%);
        width: 90%;
        overflow: hidden;

        display: flex;
        flex-direction: column;
        gap: 2px;
        justify-content: center;
        box-sizing: border-box;

        color: @white;
        font-size: 12px;
        font-weight: bold;
      }
    }
  }
}
</style>
