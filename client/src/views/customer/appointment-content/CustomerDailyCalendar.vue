<script setup lang="ts">
import CustomerAddsAppointment from "@/components/CustomerAddsAppointment.vue";
import {
  Appointment,
  AppointmentCalendar,
  Doctor,
  Vacation,
} from "@/data/types/Entities";
import { createAppointment } from "@/services/appointments_service";
import { getUserIdAndToken } from "@/services/authentication_service";
import { useUserProfile } from "@/store/useUserProfile";
import {
  addDays,
  format,
  formatISO,
  isWeekend,
  isWithinInterval,
} from "date-fns";
import Swal from "sweetalert2";
import { Ref, computed, getCurrentInstance, onMounted, ref, watch } from "vue";

const props = defineProps<{
  appointments: AppointmentCalendar[];
  unavailabilites: Vacation[] | null;
  selectedDoctor: Doctor | null;
  calendarDate: Date;
}>();

const emits = defineEmits<{
  createdAppointment: [];
}>();

const timeSlots: string[] = createTimeSlots(9, 18, 10);
const { userId } = getUserIdAndToken();
const showAppointmentModal = ref<boolean>(false);
const { userDetails, fetchUserProfile } = useUserProfile();

const appointmentsForTheDay = ref<AppointmentCalendar[]>([]);
const date = ref<Date>(props.calendarDate);

const unavailableToday = computed(() => {
  if (props.unavailabilites) {
    return props.unavailabilites.some((unavailability) => {
      if (unavailability.startTime === null) {
        let startOfTheVacation = new Date(
          addDays(unavailability.startDate, -1)
        );
        let endOfTheVacation = new Date(unavailability.endDate);
        if (
          isWithinInterval(date.value, {
            start: startOfTheVacation,
            end: endOfTheVacation,
          })
        )
          return true;
      }
    });
  }
});

interface unavailableBetweenHours {
  exists: boolean;
  fromPosition: number;
  toPosition: number;
}
const unavailableHoursWithinDay: Ref<unavailableBetweenHours> = computed(
  (): unavailableBetweenHours => {
    if (props.unavailabilites) {
      let unavailabilityArray = props.unavailabilites.filter((unav) => {
        return (
          unav.startDate === formatISO(date.value, { representation: "date" })
        );
      });

      // add a filter method for current day.
      if (unavailabilityArray.length > 0) {
        let unavailabity = unavailabilityArray[0];
        let [startHour, startMinute] = unavailabity.startTime
          .split(":")
          .map(Number);
        let [endHour, endMinute] = unavailabity.endTime.split(":").map(Number);
        let fromPos = (startHour - 9) * 6 + 1 + Math.floor(startMinute / 10);
        let toPos = (endHour - 9) * 6 + 1 + Math.ceil(endMinute / 10);
        return { exists: true, fromPosition: fromPos, toPosition: toPos };
      }
    }
    return { exists: false, fromPosition: 0, toPosition: 0 }; // 0 is a default to not show anything but it won't because of the exists flag.
  }
);
function createTimeSlots(
  startHour: number,
  endHour: number,
  interval: number
): string[] {
  const slots: string[] = [];
  for (let hour = startHour; hour < endHour; hour++) {
    for (let minutes = 0; minutes < 60; minutes += interval) {
      slots.push(`${hour}:${minutes.toString().padStart(2, "0")}`);
    }
  }

  return slots;
}

const calendarHeaderDate = computed(() => {
  return format(props.calendarDate, "EEEE, MMMM d yyyy");
});

function getAppointmentPosition(
  appointment: AppointmentCalendar,
  startHour: number
) {
  const [hour, minutes] = appointment.time.split(":").map(Number);
  const adjustedHour = hour - startHour;
  const start = adjustedHour * 6 + Math.floor(minutes / 10);
  const duration = Math.ceil(appointment.finalDuration / 10);

  return { start: start + 1, duration: duration };
}

function getAppointmentStyle(appointment: AppointmentCalendar) {
  if (appointment.status === "CANCELLED") {
    return { display: "none" };
  }
  if (appointment.status === "REQUESTED" && appointment.customerId !== userId) {
    return { display: "none" };
  }
  const { start, duration } = getAppointmentPosition(appointment, 9);
  return {
    gridRowStart: start,
    gridRowEnd: start + duration,
  };
}

interface Time {
  hour: number;
  minute: number;
}

function handleShowAppointment() {
  showAppointmentModal.value = true;
}
async function handleAddAppointment(
  date: string,
  hour: string,
  doctorId: number,
  serviceId: number
) {
  showAppointmentModal.value = false;
  if (isWeekend(new Date(date))) {
    Swal.fire({
      position: "top-end",
      icon: "error",
      title: "Can not create appointments on weekend.",
      showConfirmButton: false,
      timer: 2500,
    });
    return;
  }
  const resp = await createAppointment(date, hour, doctorId, serviceId, userId);
  emits("createdAppointment");
}
function closeAppointmentModal() {
  showAppointmentModal.value = false;
}

watch(
  () => props.calendarDate,
  (newDate) => {
    date.value = newDate;
    appointmentsForTheDay.value = props.appointments.filter(
      (app) => app.date === formatISO(date.value).split("T")[0]
    );
  }
);

watch(
  () => props.appointments,
  (newAppointments) => {
    appointmentsForTheDay.value = newAppointments.filter(
      (app) => app.date === formatISO(date.value).split("T")[0]
    );
  },
  {
    immediate: true,
  }
);

onMounted(async () => {
  if (userDetails.value === null) {
    await fetchUserProfile();
  }
});
</script>
<template>
  <div class="customer-daily-calendar">
    <div class="calendar-header">
      <div class="displayed-date">
        {{ calendarHeaderDate }}
      </div>
      <font-awesome-icon
        icon="plus"
        class="circle-plus-icon"
        id="circle-plus-icon"
        @click="handleShowAppointment"
      />
    </div>
    <div class="calendar-table-container">
      <div class="calendar-grid">
        <div class="time-slot" v-for="(slot, index) in timeSlots" :key="index">
          <div class="hour-label" v-if="index % 6 === 0">
            <span> {{ slot.split(":")[0] }}:00 </span>
          </div>
          <div class="median-slot"></div>
        </div>
        <div
          v-if="appointmentsForTheDay.length > 0"
          v-for="appointment in appointmentsForTheDay"
          :key="appointment.appointmentId"
          class="appointment-block"
          :class="{ 'user-appointment': appointment.customerId === userId }"
          :style="getAppointmentStyle(appointment)"
        >
          <div
            class="appointment-text"
            v-if="appointment.customerId === userId"
          >
            <span :style="{ fontSize: '18px' }">
              {{ userDetails?.firstName }} {{ userDetails?.lastName }}
            </span>
            <span> Service: <br />{{ appointment.serviceName }} </span>
          </div>
          <div v-else>BUSY</div>
        </div>
        <div
          v-else-if="unavailableToday"
          class="appointment-block"
          :style="{ gridRowStart: 1, gridRowEnd: 55 }"
        >
          UNAVAILABLE TODAY
        </div>
        <div
          v-else-if="unavailableHoursWithinDay.exists"
          class="appointment-block"
          :style="{
            gridRowStart: unavailableHoursWithinDay.fromPosition,
            gridRowEnd: unavailableHoursWithinDay.toPosition,
          }"
        >
          UNAVAILABLE
        </div>
      </div>
    </div>
  </div>
  <CustomerAddsAppointment
    :visible="showAppointmentModal"
    :selectedDoctor="selectedDoctor"
    :selectedDate="calendarDate"
    :userDetails="userDetails"
    variant="DAY"
    @close="closeAppointmentModal"
    @addAppointment="handleAddAppointment"
    :disabled="unavailableToday"
  />
</template>

<style lang="less" scoped>
@import (reference) "@/assets/styles.less";

.customer-daily-calendar {
  display: flex;
  flex-direction: column;

  .calendar-header {
    display: flex;
    justify-content: space-between;
    color: @white;
    box-shadow: @smoke 0px 6px 12px;
    z-index: 10;
    padding: 10px 0px 30px 80px;

    .displayed-date {
      display: flex;
      font-size: 22px;
      padding: 10px 0px 0px 40px;
    }

    .circle-plus-icon {
      border: 2px solid @white;
      font-size: 26px;
      border-radius: 100%;
      text-align: center;
      margin: 10px 60px 0px 0px;
      padding: 5px;
      width: 20px;
      height: 20px;
    }
  }

  .calendar-table-container {
    margin: 0px 100px 20px 40px;
    background-color: @light-smoke;
    margin: 10px;
    overflow-y: scroll;
    max-height: 72vh;

    .calendar-grid {
      display: grid;
      grid-template-columns: 60px 10px auto;
      grid-template-rows: repeat(54, 30px);
      gap: 1px;

      .time-slot {
        display: contents;
        .hour-label {
          display: flex;
          align-items: center;
          justify-content: space-between;
          grid-column: 1 / span 1;
          font-weight: bold;
          background-color: transparent;
        }

        .median-slot {
          grid-column: 2 / span 1;
          background-color: @dark-gray;
        }

        .time-slot-block {
          grid-column: 3 / span 1;
          background-color: transparent;
        }
      }

      .appointment-block {
        background-color: @smoke;
        color: @white;
        display: flex;
        align-items: center;
        justify-content: center;
        border: 1px solid @dark-gray;
        box-shadow: @smoke 0px 8px 14px;
        text-align: center;
        margin: 2px 10px;
        border-radius: 10px;
        grid-column: 3 / span 1;
        &.user-appointment {
          background-color: @green;
          color: @smoke;
          font-weight: bolder;
          font-size: 14px;
        }

        .appointment-text {
          display: flex;
          flex-direction: column;
          gap: 15px;
        }
      }
    }

    &::-webkit-scrollbar {
      width: 15px;
      height: 15px;
    }

    &::-webkit-scrollbar-thumb {
      background: #424d65;
      border-radius: 5px;
    }

    &::-webkit-scrollbar-track:horizontal {
      background-color: #424d65;
      box-shadow: inset 0 0 2px 2px gainsboro;
    }
  }
}
</style>
