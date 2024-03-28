<script setup lang="ts">
import { Appointment, SelectedDoctor } from "@/data/types/Entities";
import { ref, computed, watch, onMounted } from "vue";
import { getAll } from "@/services/appointments_service";

const props = withDefaults(
  defineProps<{
    selectedCalendars: SelectedDoctor[];
    daySelected: Date;
  }>(),
  {}
);

type CalendarAppointment = Appointment & { endTime: string };

const currentDate = ref(props.daySelected);

const timeSlots: string[] = createTimeSlots(9, 18);
const calendars = ref(["Ana", "Doctor 1"]);
const calendarsNumber = computed(() => calendars.value.length);
const currentDateKey = computed(() => currentDate.value.toISOString());
const appointments = ref<CalendarAppointment[]>([]);

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

function getAppointmentStyle(appointment: CalendarAppointment) {
  const startHour = parseInt(appointment.time.split(":")[0]);
  const startMinutes = parseInt(appointment.time.split(":")[1]);
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

function computeStyle(appointment: CalendarAppointment, slot: string) {
  const startHour = parseInt(appointment.time.split(":")[0]);
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

function getAppointmentsForCalendar(calendar: number) {
  const filteredAppointments = appointments.value.filter((appointment) => {
    const condition =
      appointment.date === toLocalISOString(currentDate.value) &&
      selectedDoctors.value.includes(appointment.doctorId) &&
      appointment.doctorId === calendar;
    return condition;
  });

  return filteredAppointments;
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
  await getAll().then((res) => {
    const updatedAppointments = res.map((appointment: CalendarAppointment) => {
      let [hour, minute] = appointment.time.split(":").map(Number);
      // Adaugă durata la minute
      minute += appointment.finalDuration;

      // Verifică dacă suma minutelor depășește 59 și ajustează orele și minutele corespunzător
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
    });

    appointments.value = updatedAppointments;
  });
}

onMounted(() => {
  loadAppointments();
});
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
              {{ calendar.firstName }}
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
                v-for="appointment in getAppointmentsForCalendar(calendar.id)"
                class="appointment"
                :style="[
                  computeStyle(appointment, slot),
                  `background-color: ${getDoctorColor(calendar.id)}`,
                ]"
              >
                <span>{{ appointment.time }}</span>
                <span>{{ "service id" + appointment.serviceId }}</span>
                <span>{{ `Patient: ${appointment.customerId}` }}</span>
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
  max-height: 97vh;

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
  &&::-webkit-scrollbar {
    width: 4px;
    background-color: @light-gray;
  }

  &&::-webkit-scrollbar-thumb {
    background-color: @blue;
    border-radius: 10px;
  }
}
</style>
