<script setup lang="ts">
import TableHeaderButton from "@/components/TableHeaderButton.vue";
import { Ref, computed, ref } from "vue";

const activeCalendar = ref<string>("MONTH");
const activeCalendarType = (selectedType: string) =>
  computed(() => {
    return activeCalendar.value === selectedType;
  });

function handleClickOnCalendarButton(value: string) {
  activeCalendar.value = value;
}
</script>

<template>
  <div class="container">
    <div class="buttons-container">
      <TableHeaderButton
        label="MONTH"
        variant="CUSTOMER_CALENDAR"
        :active="activeCalendarType('MONTH').value"
        @click="handleClickOnCalendarButton('MONTH')"
      />
      <TableHeaderButton
        label="WEEK"
        variant="CUSTOMER_CALENDAR"
        :active="activeCalendarType('WEEK').value"
        @click="handleClickOnCalendarButton('WEEK')"
      />
      <TableHeaderButton
        label="DAY"
        variant="CUSTOMER_CALENDAR"
        :active="activeCalendarType('DAY').value"
        @click="handleClickOnCalendarButton('DAY')"
      />
    </div>
  </div>
</template>

<style lang="less" scoped>
@import (reference) "@/assets/styles.less";
.container {
  display: flex;
  box-sizing: border-box;
  gap: 100px;
  height: 70px;
  max-height: 70px;
  width: 100%;
  background-color: @white;

  .buttons-container {
    display: flex;
    background-color: @white;
    gap: 100px;
    height: 100%;
    width: 100%;
  }
}
</style>
