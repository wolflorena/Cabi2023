<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from "vue";
import TimePicker from "./TimePicker.vue";
import { formatTime } from "@/utils/helpers";

const emit = defineEmits(["update:selectedTime", "toggleTimePicker"]);
const props = defineProps({
  label: String,
  error: Boolean,
  errorMessage: String,
});

const selectedTime = ref("");
const showTimePicker = ref(false);

function toggleTimePicker() {
  showTimePicker.value = !showTimePicker.value;
  emit("toggleTimePicker", showTimePicker.value);
}

function handleTimeSelect(time: string) {
  selectedTime.value = time;
  showTimePicker.value = false;
  emit("update:selectedTime", selectedTime.value);
  emit("toggleTimePicker", false);
}

watch(
  () => props.error,
  (newError) => {
    if (newError) {
      console.log("Error state changed:", newError);
    }
  }
);
</script>

<template>
  <div class="time-input">
    <label>{{ label }}</label>
    <div class="input-container">
      <input
        type="text"
        :value="selectedTime ? formatTime(selectedTime + ':00') : selectedTime"
        @focus="toggleTimePicker"
        placeholder="Select a time"
        readonly
        :class="{ 'error-border': props.error }"
      />

      <p v-if="props.error" class="error-message">{{ props.errorMessage }}</p>
    </div>

    <div v-if="showTimePicker" class="time-picker-container">
      <TimePicker
        @selectTime="handleTimeSelect"
        :initialHour="
          selectedTime.split(':')[0] === '' ? 'HH' : selectedTime.split(':')[0]
        "
        :initialMinute="
          selectedTime.split(':')[1] === '' ? 'MM' : selectedTime.split(':')[1]
        "
      />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.time-input {
  width: 100%;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;

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
    background-color: #f9f9f9;
    color: #333;

    &.error-border {
      border-color: @red;
    }
  }

  .time-picker-container {
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
