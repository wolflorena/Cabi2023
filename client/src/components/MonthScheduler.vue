<script setup lang="ts">
import { ref, computed } from "vue";

const props = withDefaults(
  defineProps<{
    selectedCalendars: any[];
  }>(),
  {}
);

type Day = {
  date: number;
  fullDate: string;
};

type Appointment = {
  details: string;
  hour: string;
  day: string;
  doctor: string;
  id?: string;
};

const currentDate = ref(new Date());
const appointments: any = ref([
  {
    details: "Appointment",
    hour: "09:00AM",
    day: "2024-02-20",
    doctor: "Doctor 1",
  },
  {
    details: "Appointment",
    hour: "08:00AM",
    day: "2024-02-20",
    doctor: "Doctor 2",
  },
  {
    details: "Appointment",
    hour: "11:00AM",
    day: "2024-02-22",
    doctor: "Doctor 3",
  },
]);

const weekdays = ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"];

const currentMonthYear = computed(() => {
  return currentDate.value.toLocaleString("default", {
    month: "long",
    year: "numeric",
  });
});

function weeksInMonth(date: Date): Day[][] {
  const month = date.getMonth();
  const year = date.getFullYear();

  const firstDayOfMonth = new Date(year, month, 0).getDay();

  const days: Day[][] = [[]];
  let weekIndex = 0;

  for (let i = 0; i < firstDayOfMonth; i++) {
    days[weekIndex].push({ date: 0, fullDate: "" });
  }

  const lastDay = new Date(year, month + 1, 0).getDate();
  for (let date = 1; date <= lastDay; date++) {
    const fullDate = new Date(year, month, date + 1);

    const dayObj: Day = {
      date,
      fullDate: fullDate.toISOString().split("T")[0],
    };

    if (days[weekIndex].length === 7) {
      weekIndex++;
      days[weekIndex] = [];
    }

    days[weekIndex].push(dayObj);
  }

  while (days[weekIndex] && days[weekIndex].length < 7) {
    days[weekIndex].push({ date: 0, fullDate: "" });
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

const emit = defineEmits(["toggle-calendar"]);

function toggleCalendar() {
  emit("toggle-calendar");
}

const selectedAppointments = computed(() => {
  const selectedDoctorsNames = props.selectedCalendars
    .filter((doctor) => doctor.checked)
    .map((doctor) => doctor.name);

  return appointments.value
    .filter((appointment: Appointment) =>
      selectedDoctorsNames.includes(appointment.doctor)
    )
    .sort((a: Appointment, b: Appointment) => {
      const timeA = convertTime12to24(a.hour);
      const timeB = convertTime12to24(b.hour);

      return timeA - timeB;
    });
});

function getDoctorColor(doctorName: string): string {
  const doctor = props.selectedCalendars.find(
    (calendar) => calendar.name === doctorName
  );
  return doctor ? doctor.color : "#ffffff";
}

function convertTime12to24(time: string): number {
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
</script>

<template>
  <div class="scheduler">
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
      <table class="calendar">
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
                <span class="date-number" v-show="day.date !== 0">
                  {{ day.date }}
                </span>
              </div>

              <div class="appointments">
                <div
                  v-for="appointment in selectedAppointments.filter((appointment:Appointment) => appointment.day === day.fullDate)"
                  :key="appointment.id"
                  class="appointment"
                >
                  <div
                    id="circle"
                    :style="{
                      backgroundColor: getDoctorColor(appointment.doctor),
                    }"
                  ></div>
                  <span id="hour">{{ appointment.hour }}</span>
                  <span id="details">{{ appointment.details }}</span>
                </div>
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
            background-color: red;
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
        }
      }
    }
  }
}
</style>
