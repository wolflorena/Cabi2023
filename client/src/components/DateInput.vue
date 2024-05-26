<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from "vue";
import DatePicker from "./DatePicker.vue";
import { formatDate } from "@/utils/helpers";

const props = defineProps<{
  label: string;
  error?: boolean;
  errorMessage?: string;
}>();

const selectedDate = ref("");
const showDatePicker = ref(false);

const emit = defineEmits(["update:selectedDate", "toggleDatePicker"]);

function toggleDatePicker() {
  showDatePicker.value = !showDatePicker.value;
  emit("toggleDatePicker", showDatePicker.value);
}

function handleDateSelect(day: any) {
  selectedDate.value = day.fullDate;
  showDatePicker.value = false;
  emit("update:selectedDate", selectedDate.value);
  emit("toggleDatePicker", false);
}
</script>

<template>
  <div class="date-input">
    <label>{{ label }}</label>
    <div class="input-container">
      <input
        type="text"
        :value="formatDate(selectedDate)"
        @focus="toggleDatePicker"
        placeholder="Select a date"
        readonly
        :class="{ 'error-border': props.error }"
      />
      <p v-if="props.error" class="error-message">{{ props.errorMessage }}</p>
    </div>
    <div v-if="showDatePicker" class="date-picker-container">
      <DatePicker @selectDay="handleDateSelect" />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.date-input {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;

  label {
    display: block;
    font-weight: bold;
    color: @sugar;
    font-size: 25px;
  }

  input {
    width: calc(19vw - 20px);
    height: 40px;
    padding: 0 10px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 5px;
    background-color: @sugar;
    color: #333;

    &.error-border {
      border-color: @red;
    }
  }

  .date-picker-container {
    position: absolute;
    top: 45px;
    right: 0;
    z-index: 1000;
    background-color: white;
    border: 1px solid #ccc;
    border-radius: 5px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }

  .error-message {
    color: @red;
    font-size: 12px;
    margin-top: 5px;
  }
}
</style>
