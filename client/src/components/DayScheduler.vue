<script setup lang="ts">
import { ref, computed } from "vue";

const currentDate = ref(new Date());
const timeSlots: string[] = createTimeSlots(9, 17);
const calendars = ref(["Calendar 2", "Dr. Ciora"]);
const calendarsNumber = computed(() => calendars.value.length);

type Appointment = {
  startTime: string;
  endTime: string;
  description: string;
  patient: string;
  doctor: string;
  day: string;
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

function navigate(dayIncrement: number): void {
  const current = currentDate.value;
  currentDate.value = new Date(
    current.getFullYear(),
    current.getMonth(),
    current.getDate() + dayIncrement,
    1
  );
}

const appointments = ref([
  {
    id: "1",
    startTime: "09:10",
    endTime: "10:00",
    description: "Example Appointment 1",
    patient: "gigel",
    doctor: "Dr. Ciora",
    day: "16",
  },
  {
    id: "2",
    startTime: "10:40",
    endTime: "11:00",
    description: "Example Appointment 1",
    patient: "gigel",
    doctor: "Dr. Ciora",
    day: "17",
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
  console.log((startPosition / 60) * 100);
  return {
    top: `${(startPosition / 60) * 100}%`,
    height: `${(duration * (88 / 5)) / 60}vh`,
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

function getAppointments(calendar: String, day: String): Appointment[] {
  let appointmentsForCalendar: Appointment[] = [];
  appointments.value.forEach((appointment) => {
    if (appointment.doctor === calendar && appointment.day === day) {
      appointmentsForCalendar.push(appointment);
    }
  });
  return appointmentsForCalendar;
}

const emit = defineEmits(["toggle-calendar"]);

function toggleCalendar() {
  emit("toggle-calendar");
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

    <div class="calendar-container">
      <table class="calendar">
        <thead>
          <tr class="calendar-header">
            <th class="column-header">Hour</th>
            <th v-for="calendar in calendars" class="column-header">
              {{ calendar }}
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
            <td v-for="calendar in calendars" class="slot calendar">
              <div
                v-for="appointment in getAppointments(
                  calendar,
                  currentDate.getDate().toString()
                )"
                class="appointment"
                :style="computeStyle(appointment, slot)"
              >
                {{ appointment.description }}
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
        background-color: @light-gray;
        overflow: hidden;

        box-sizing: border-box;
      }
    }
  }
}
</style>
