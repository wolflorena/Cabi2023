<script setup lang="ts">
import { ref, computed } from "vue";

const weekdays = ["MO", "TU", "WE", "TH", "FR", "SA", "SU"];

type Day = {
  date: number;
  fullDate: string;
  currentMonth: boolean;
  isWeekend: boolean;
};

const currentDate = ref(new Date());
const emit = defineEmits(["selectDay"]);

const currentMonthYear = computed(() => {
  return currentDate.value.toLocaleString("default", {
    month: "long",
    year: "numeric",
  });
});

function navigate(monthIncrement: number): void {
  const current = currentDate.value;
  currentDate.value = new Date(
    current.getFullYear(),
    current.getMonth() + monthIncrement,
    1
  );
}

function weeksInMonth(date: Date): Day[][] {
  const month = date.getMonth();
  const year = date.getFullYear();

  const firstDayOfMonth = new Date(year, month, 0).getDay();
  const daysInPreviousMonth = new Date(year, month, 0).getDate();

  const days: Day[][] = [[]];
  let weekIndex = 0;

  // Zilele din luna anterioara
  for (let i = firstDayOfMonth; i > 0; i--) {
    const prevMonthDay = daysInPreviousMonth - i + 1;
    const fullDate = new Date(year, month - 1, prevMonthDay + 1);
    days[weekIndex].push({
      date: prevMonthDay,
      fullDate: fullDate.toISOString().split("T")[0],
      currentMonth: false,
      isWeekend: fullDate.getDay() === 6 || fullDate.getDay() === 0,
    });
  }

  // Zilele din luna curenta
  const lastDay = new Date(year, month + 1, 0).getDate();
  for (let date = 1; date <= lastDay; date++) {
    if (days[weekIndex].length === 7) {
      weekIndex++;
      days[weekIndex] = [];
    }
    const fullDate = new Date(year, month, date + 1);
    days[weekIndex].push({
      date: date,
      fullDate: fullDate.toISOString().split("T")[0],
      currentMonth: true,
      isWeekend: fullDate.getDay() === 0 || fullDate.getDay() === 1,
    });
  }

  let nextMonthDay = 1;
  // Zilele din luna viitoare
  while (days[weekIndex].length < 7) {
    const fullDate = new Date(year, month + 1, nextMonthDay + 1);

    days[weekIndex].push({
      date: nextMonthDay,
      fullDate: fullDate.toISOString().split("T")[0],
      currentMonth: false,
      isWeekend: fullDate.getDay() === 0 || fullDate.getDay() === 1,
    });
    nextMonthDay++;
  }

  return days;
}

function selectDay(day: Day) {
  emit("selectDay", day);
}
</script>

<template>
  <div class="date-picker">
    <div class="header">
      <span>{{ currentMonthYear }}</span>
      <div class="header-controls">
        <button class="header-button" @click="navigate(-1)">
          <font-awesome-icon icon="chevron-left" />
        </button>
        <button class="header-button" @click="navigate(1)">
          <font-awesome-icon icon="chevron-right" />
        </button>
      </div>
    </div>

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
            :class="{
              outer: day.currentMonth === false,
              weekend: day.isWeekend === true,
            }"
          >
            <button class="date-number" @click="selectDay(day)">
              {{ day.date }}
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.date-picker {
  width: 19vw;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    span {
      color: @font-dark-gray;
      font-weight: bolder;
    }

    .header-controls {
      button {
        background-color: transparent;
        border: none;
        color: @font-dark-gray;
      }
    }
  }

  .calendar {
    background-color: @light-gray;
    width: 100%;
    border-collapse: collapse;

    .weekdays {
      height: 40px;

      .weekday {
        color: @font-gray;
      }
    }

    .day {
      height: 40px;
      width: 40px;
      text-align: center;

      button {
        background-color: transparent;
        border: none;
        font-weight: bold;

        color: @font-darker-gray;

        cursor: pointer;
      }

      &.weekend {
        background-color: @dark-gray;

        button {
          color: @font-dark-gray;
        }
      }

      &.outer button {
        color: @font-dark-gray;
      }
    }
  }
}
</style>
