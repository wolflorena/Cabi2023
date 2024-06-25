<script setup lang="ts">
import { ref } from "vue";
import CustomCheckbox from "./CustomCheckbox.vue";
import DateInput from "./DateInput.vue";
import TimeInput from "./TimeInput.vue";
import CustomButton from "./CustomButton.vue";
import { createUnavailability } from "@/services/doctor_unavailability_service";
import ErrorMessage from "./ErrorMessage.vue";
import { faPeopleArrows } from "@fortawesome/free-solid-svg-icons";
import Swal from "sweetalert2";

const multipleDays = ref<boolean>(false);
const allDay = ref<boolean>(false);
const selectedStartDate = ref("");
const selectedEndDate = ref("");
const selectedStartTime = ref("");
const selectedEndTime = ref("");
const reason = ref("");
const endDateError = ref<boolean>(false);
const endDateErrorMessage = ref<string>("");
const endTimeError = ref<boolean>(false);
const endTimeErrorMessage = ref<string>("");
const errorMessage = ref<string>("");

const props = withDefaults(
  defineProps<{
    doctorId: number;
  }>(),
  {}
);

function handleStartDateUpdate(date: string) {
  selectedStartDate.value = date;
  endDateError.value = false;
}

function handleEndDateUpdate(date: string) {
  if (new Date(date) <= new Date(selectedStartDate.value)) {
    endDateError.value = true;
    endDateErrorMessage.value = "End date must be after start date.";
  } else {
    selectedEndDate.value = date;
    endDateError.value = false;
    endDateErrorMessage.value = "";
  }
}

function handleSelectedDate(date: string) {
  selectedStartDate.value = date;
  selectedEndDate.value = date;
}

function handleMultipleDaysUpdate() {
  multipleDays.value = !multipleDays.value;
  selectedEndDate.value = "";
  selectedStartDate.value = "";
}

function handleStartTimeUpdate(time: string) {
  selectedStartTime.value = time;
}

function handleEndTimeUpdate(time: string) {
  const startTime = new Date(`1970-01-01T${selectedStartTime.value}:00`);
  const endTime = new Date(`1970-01-01T${time}:00`);

  if (endTime <= startTime) {
    endTimeError.value = true;
    endTimeErrorMessage.value = "End time must be after start time.";
  } else {
    selectedEndTime.value = time;
    endTimeError.value = false;
    endTimeErrorMessage.value = "";
  }
}

async function addVacation() {
  if (reason.value) {
    if (multipleDays.value) {
      if (selectedStartDate.value && selectedEndDate.value) {
        errorMessage.value = "";

        await createUnavailability(
          selectedStartDate.value,
          selectedEndDate.value,
          props.doctorId,
          reason.value
        )
          .then((res) => {
            if (res) {
              Swal.fire({
                title: "Unavailability successfully created.",
                icon: "success",
                confirmButtonText: "OK",
              });
              multipleDays.value = false;
              selectedStartDate.value = "";
              selectedEndDate.value = "";
            }
          })
          .catch((err) => (errorMessage.value = err.message));
      } else {
        errorMessage.value =
          "Start date and end date must be provided for multiple days vacation.";
      }
    } else {
      if (allDay.value) {
        errorMessage.value = "";

        await createUnavailability(
          selectedStartDate.value,
          selectedStartDate.value,
          props.doctorId,
          reason.value
        )
          .then((res) => {
            if (res) {
              Swal.fire({
                title: "Unavailability successfully created.",
                icon: "success",
                confirmButtonText: "OK",
              });
              allDay.value = false;
              selectedStartDate.value = "";
              selectedEndDate.value = "";
              reason.value = "";
            }
          })
          .catch((err) => (errorMessage.value = err.message));
      } else {
        if (selectedStartTime.value && selectedEndTime.value) {
          errorMessage.value = "";

          await createUnavailability(
            selectedStartDate.value,
            selectedStartDate.value,
            props.doctorId,
            reason.value,
            selectedStartTime.value + ":00",
            selectedEndTime.value + ":00"
          )
            .then((res) => {
              if (res) {
                Swal.fire({
                  title: "Unavailability successfully created.",
                  icon: "success",
                  confirmButtonText: "OK",
                });
                multipleDays.value = false;
                allDay.value = false;
                selectedStartDate.value = "";
                selectedEndDate.value = "";
                selectedEndTime.value = "";
                selectedStartTime.value = "";
                reason.value = "";
              }
            })
            .catch((err) => (errorMessage.value = err.message));
        } else {
          errorMessage.value =
            "Start time and end time must be provided for partial day vacation.";
        }
      }
    }
  } else {
    errorMessage.value = "All field are requiered";
  }
}
</script>

<template>
  <div class="container">
    <CustomCheckbox
      uuid="Day-uuid"
      text="Multiple days"
      fontSize="20"
      color="#ef4b4c"
      font-color="#e9e9e8"
      :modelValue="multipleDays"
      @update:modelValue="handleMultipleDaysUpdate"
    />
    <div class="dates" v-if="multipleDays">
      <DateInput
        label="Start Date"
        @update:selectedDate="handleStartDateUpdate"
      />

      <DateInput
        label="End Date"
        :error="endDateError"
        :errorMessage="endDateErrorMessage"
        @update:selectedDate="handleEndDateUpdate"
      />
    </div>
    <div class="singleDate" v-else>
      <DateInput label="Date" @update:selectedDate="handleSelectedDate" />
      <CustomCheckbox
        uuid="Time-uuid"
        text="All day"
        fontSize="20"
        font-color="#e9e9e8"
        color="#ef4b4c"
        :modelValue="allDay"
        @update:modelValue="allDay = !allDay"
      />
      <div class="time" v-if="!allDay">
        <TimeInput
          label="Start Time"
          @update:selectedTime="handleStartTimeUpdate"
        />

        <TimeInput
          label="End Time"
          :error="endTimeError"
          :errorMessage="endTimeErrorMessage"
          @update:selectedTime="handleEndTimeUpdate"
        />
      </div>
    </div>
    <div class="reason">
      <label>Reason</label>
      <input type="text" v-model="reason" placeholder="Write your reason" />
    </div>
    <div class="button">
      <CustomButton
        text="Save"
        width="250"
        height="30"
        fontSize="20"
        :disabled="endDateError || endTimeError"
        @action-triggered="addVacation"
      />
      <ErrorMessage v-if="errorMessage != ''" :message="errorMessage" />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  width: 65vw;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 10vw;
  gap: 10vh;

  .dates,
  .singleDate {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10vh;

    .time {
      width: 100%;
      display: flex;
      flex-direction: column;
      gap: 10vh;
    }
  }

  .reason {
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
    }
  }

  .button {
    position: absolute;
    bottom: 40px;

    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 10px;
  }
}
</style>
