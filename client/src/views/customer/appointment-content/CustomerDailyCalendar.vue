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
import { useLoadAppointments } from "@/store/useLoadAppointments";
import { useUserProfile } from "@/store/useUserProfile";
import { format, formatISO } from "date-fns";
import { computed, getCurrentInstance, onMounted, ref, watch } from "vue";

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

function calculateEndTime(
  startHour: number,
  startMinute: number,
  durationMinutes: number
): { startTime: Time; endTime: Time } {
  const startTime: Time = { hour: startHour, minute: startMinute };
  const totalMinutes = startMinute + durationMinutes;
  const endHour = startHour + Math.floor(totalMinutes / 60);
  const endMinute = totalMinutes % 60;
  const endTime: Time = { hour: endHour, minute: endMinute };

  return { startTime, endTime };
}

function slotHasAppointment(slot: string): boolean {
  const [hour, minutes] = slot.split(":").map(Number);
  const slotTime = new Date();
  slotTime.setHours(hour, minutes, 0, 0);

  return appointmentsForTheDay.value.some((app) => {
    const [startHour, startMinutes] = app.time.split(":").map(Number);
    const { startTime, endTime } = calculateEndTime(
      startHour,
      startMinutes,
      app.finalDuration
    );

    const appointmentStartTime = new Date();
    appointmentStartTime.setHours(startTime.hour, startTime.minute, 0, 0);

    const appointmentEndTime = new Date();
    appointmentEndTime.setHours(endTime.hour, endTime.minute, 0, 0);

    return slotTime >= appointmentStartTime && slotTime < appointmentEndTime;
  });
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
          <div class="time-slot-block" v-if="!slotHasAppointment(slot)"></div>
        </div>
        <div
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
