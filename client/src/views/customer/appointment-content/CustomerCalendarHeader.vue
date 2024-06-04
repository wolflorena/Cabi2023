<script setup lang="ts">
import TableHeaderButton from "@/components/TableHeaderButton.vue";
import VueDatePicker from "@vuepic/vue-datepicker";
import { computed, ref, watch } from "vue";

const props = defineProps<{
  calendarDate: Date;
  calendarViewType: string;
}>();
const emits = defineEmits<{
  onDateChanged: [newDate: Date];
  onViewTypeChanged: [newViewType: string];
}>();

const calendarBind = ref<Date>(props.calendarDate);

const viewTypeCalendarActive = (selectedType: string) =>
  computed(() => {
    return props.calendarViewType === selectedType;
  });

function handleClickOnCalendarButton(value: string) {
  emits("onViewTypeChanged", value);
}

function handlePrevDate() {
  const prevDate = new Date(calendarBind.value);

  if (props.calendarViewType === "DAY") {
    prevDate.setDate(calendarBind.value.getDate() - 1);
  } else if (props.calendarViewType === "WEEK") {
    prevDate.setDate(calendarBind.value.getDate() - 7);
  } else if (props.calendarViewType === "MONTH") {
    const currentMonth = calendarBind.value.getMonth();

    prevDate.setMonth(currentMonth - 1); // current month - 1 = 3 | prevDate 31.04 => 01.05
    const prevMonth = prevDate.getMonth(); // 4

    if (prevMonth !== (currentMonth - 1 + 12) % 12) {
      prevDate.setDate(0);
    }
  }

  calendarBind.value = prevDate;
}

function handleNextDate() {
  const nextDate = new Date(calendarBind.value);

  if (props.calendarViewType === "DAY") {
    nextDate.setDate(calendarBind.value.getDate() + 1);
  } else if (props.calendarViewType === "WEEK") {
    nextDate.setDate(calendarBind.value.getDate() + 7);
  } else if (props.calendarViewType === "MONTH") {
    const currentMonth = calendarBind.value.getMonth();
    nextDate.setMonth(currentMonth + 1);
    const nextMonth = nextDate.getMonth();

    if (nextMonth !== (currentMonth + 1) % 12) {
      nextDate.setDate(0);
    }
  }

  calendarBind.value = nextDate;
}

const formatDay = (calendarDate: Date) => {
  const day = props.calendarDate.getDate();
  const month = props.calendarDate.getMonth() + 1;
  const year = props.calendarDate.getFullYear();

  return `${day}/${month}/${year}`;
};

const formatMonth = (calendarDate: Date) => {
  const month = props.calendarDate.getMonth() + 1;
  const year = props.calendarDate.getFullYear();

  return `${month}/${year}`;
};

const formatToUse = computed(() => {
  if (props.calendarViewType === "DAY") {
    return formatDay;
  } else if (props.calendarViewType === "WEEK") {
    return formatDay;
  } else {
    return formatMonth;
  }
});

watch(calendarBind, () => {
  emits("onDateChanged", calendarBind.value);
});
</script>

<template>
  <div class="appointment-calendar-header-container">
    <div class="buttons-container">
      <TableHeaderButton
        label="MONTH"
        variant="CUSTOMER_CALENDAR"
        :active="viewTypeCalendarActive('MONTH').value"
        @click="handleClickOnCalendarButton('MONTH')"
      />
      <TableHeaderButton
        label="WEEK"
        variant="CUSTOMER_CALENDAR"
        :active="viewTypeCalendarActive('WEEK').value"
        @click="handleClickOnCalendarButton('WEEK')"
      />
      <TableHeaderButton
        label="DAY"
        variant="CUSTOMER_CALENDAR"
        :active="viewTypeCalendarActive('DAY').value"
        @click="handleClickOnCalendarButton('DAY')"
      />
    </div>
    <div class="date-picker-container">
      <font-awesome-icon
        icon="caret-left"
        id="left-arrow-icon"
        class="arrow-icon"
        @click="handlePrevDate"
      />

      <VueDatePicker
        class="date-picker"
        v-model="calendarBind"
        :format="formatToUse"
        :auto-apply="true"
        :enable-time-picker="false"
        :clearable="false"
      />
      <font-awesome-icon
        icon="caret-right"
        id="right-arrow-icon"
        class="arrow-icon"
        @click="handleNextDate"
      />
    </div>
  </div>
</template>

<style lang="less" scoped>
@import (reference) "@/assets/styles.less";
.appointment-calendar-header-container {
  display: flex;
  background-color: @white;
  height: 70px;
  width: 100%;

  .buttons-container {
    display: flex;
    gap: 100px;
  }

  .date-picker-container {
    display: flex;
    width: 100%;
    align-items: center;
    justify-content: center;

    .date-picker {
      width: 280px;
      --dp-text-color: @smoke;
      --dp-hover-icon-color: @smoke;
      --dp-border-color: @smoke;
      --dp-icon-color: @smoke;
    }

    .arrow-icon {
      font-size: 32px;
      padding: 0px 8px;
      cursor: pointer;
      color: @smoke;
    }
  }
}
</style>
