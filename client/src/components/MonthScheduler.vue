<script setup lang="ts">
import { ref, computed } from "vue";

type Day = {
  date: number;
  fullDate: string;
};

type Appointment = {
  id: string;
  time: string;
  description: string;
};

const currentDate = ref(new Date());
const appointments: any = ref([]);

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
    const fullDate = new Date(year, month, date);
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
                  v-for="appointment in appointments[day.fullDate]"
                  :key="appointment.id"
                  class="appointment"
                >
                  {{ appointment.time }} - {{ appointment.description }}
                </div>
                <div class="appointment">
                  <div id="circle"></div>
                  <span id="hour">9:00pm</span>
                  <span id="details">Appointment</span>
                </div>
                <div class="appointment">
                  <div id="circle"></div>
                  <span id="hour">9:00pm</span>
                  <span id="details">Appointment</span>
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
          }
        }
      }
    }
  }
}
</style>
