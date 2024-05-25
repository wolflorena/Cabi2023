<script setup lang="ts">
import { ref, watch, defineEmits, defineProps } from "vue";

const props = defineProps({
  initialHour: String,
  initialMinute: String,
});

const hours = Array.from({ length: 9 }, (_, i) =>
  String(i + 9).padStart(2, "0")
);

const minutes = ["00", "30"];

const selectedHour = ref(props.initialHour || "HH");
const selectedMinute = ref(props.initialMinute || "MM");

const emit = defineEmits(["selectTime"]);

function handleTimeSelect() {
  if (selectedHour.value !== "HH" && selectedMinute.value !== "MM") {
    emit("selectTime", `${selectedHour.value}:${selectedMinute.value}`);
  }
}

function updateHour(event: any) {
  selectedHour.value = event.target.value;
  handleTimeSelect();
}

function updateMinute(event: any) {
  selectedMinute.value = event.target.value;
  handleTimeSelect();
}

watch([selectedHour, selectedMinute], handleTimeSelect);
</script>

<template>
  <div class="time-picker">
    <div class="time-select">
      <select v-model="selectedHour" @change="updateHour">
        <option disabled value="HH">Hour</option>
        <option v-for="hour in hours" :key="hour" :value="hour">
          {{ hour }}
        </option>
      </select>
      <span>:</span>
      <select v-model="selectedMinute" @change="updateMinute">
        <option disabled value="MM">Minute</option>
        <option v-for="minute in minutes" :key="minute" :value="minute">
          {{ minute }}
        </option>
      </select>
    </div>
  </div>
</template>

<style scoped>
.time-picker {
  display: flex;
  justify-content: center;
  align-items: center;
  width: calc(19vw - 20px);
  padding: 10px;
  background-color: #f9f9f9;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.time-select {
  display: flex;
  align-items: center;
  gap: 5px;

  select {
    padding: 5px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 5px;
    background-color: white;

    width: 150px;
  }
}
</style>
