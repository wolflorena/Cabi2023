<script setup lang="ts">
import {
  Appointment,
  AppointmentCalendar,
  Doctor,
  Vacation,
} from "@/data/types/Entities";
import CustomerCalendarHeader from "./CustomerCalendarHeader.vue";
import placeholderGif from "@/assets/doctor-gif-placeholder.gif";
import CustomerDailyCalendar from "./CustomerDailyCalendar.vue";
import CustomerWeeklyCalendar from "./CustomerWeeklyCalendar.vue";
import { watch } from "vue";

const props = defineProps<{
  selectedDoctor: Doctor | null;
  appointments: AppointmentCalendar[];
  unavailabilites: Vacation[] | null;
  calendarDate: Date;
  calendarViewType: string;
}>();

const emits = defineEmits<{
  onDateChange: [newDate: Date];
  onViewTypeChange: [newViewType: string];
  onAppointmentCreated: [];
}>();

function handleDateChange(newDate: Date) {
  emits("onDateChange", newDate);
}

function handleViewTypeChange(newViewType: string) {
  emits("onViewTypeChange", newViewType);
}

function handleCreatedAppointment() {
  emits("onAppointmentCreated");
}

watch(
  () => props.calendarViewType,
  () => {
    if (!(props.appointments.length > 0) && props.selectedDoctor !== null) {
      handleViewTypeChange("DAY");
    }
  }
);
</script>

<template>
  <div class="appointment-content-container">
    <CustomerCalendarHeader
      :calendarDate="calendarDate"
      :calendarViewType="calendarViewType"
      @on-date-changed="handleDateChange"
      @on-view-type-changed="handleViewTypeChange"
    />
    <div class="placeholder-image-container" v-if="selectedDoctor === null">
      <img
        id="placeholder-image"
        alt="placeholderGif"
        class="placeholder-image"
        :src="placeholderGif"
      />
      <span id="placeholder-text" class="placeholder-text"
        >Select a Doctor...</span
      >
    </div>
    <div v-if="appointments.length > 0 && props.calendarViewType === 'DAY'">
      <CustomerDailyCalendar
        :appointments="appointments"
        :selectedDoctor="selectedDoctor"
        :unavailabilites="unavailabilites"
        :calendarDate="calendarDate"
        @created-appointment="handleCreatedAppointment"
      />
    </div>
    <div
      v-else-if="appointments.length > 0 && props.calendarViewType === 'WEEK'"
    >
      <CustomerWeeklyCalendar
        :appointments="appointments"
        :selectedDoctor="selectedDoctor"
        :unavailabilites="unavailabilites"
        :calendarDate="calendarDate"
        @created-appointment="handleCreatedAppointment"
      />
    </div>
    <div v-else-if="selectedDoctor !== null">
      <CustomerDailyCalendar
        :appointments="appointments"
        :selectedDoctor="selectedDoctor"
        :unavailabilites="unavailabilites"
        :calendarDate="calendarDate"
        @created-appointment="handleCreatedAppointment"
      />
    </div>
  </div>
</template>

<style lang="less" scoped>
@import (reference) "@/assets/styles.less";
.appointment-content-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  background-color: @light-smoke;
  color: @white;

  .placeholder-image-container {
    z-index: 2;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
    overflow: hidden;
    .placeholder-text {
      color: @heavy-smoke;
      font-size: 4rem;
      font-weight: bold;
      font-family: cursive, Haettenschweiler, "Arial Narrow Bold", sans-serif;
      position: absolute;
      top: 5rem;
      left: 5rem;
    }
    .placeholder-image {
      width: 100%;
    }
  }
}
</style>
