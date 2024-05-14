<script setup lang="ts">
import { formatTime, formatDateForTable } from "@/utils/helpers";

const props = withDefaults(
  defineProps<{
    time: string;
    date: string;
  }>(),
  {}
);
</script>

<template>
  <div class="date">
    <span id="time">{{ formatTime(time) }} </span>
    <span id="date">
      {{ formatDateForTable(date) }}
    </span>
  </div>
</template>

<style scoped lang="less">
.date {
  display: flex;
  flex-direction: column;

  #time {
    font-size: 20px;
    font-weight: 500;
  }

  #date {
    font-size: 12px;
  }
}
</style>
