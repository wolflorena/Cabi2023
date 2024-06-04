<script setup lang="ts">
import AddAppointmentModal from "@/components/AddAppointmentModal.vue";
import {
  Appointment,
  AppointmentCalendar,
  Doctor,
} from "@/data/types/Entities";
import { getUserIdAndToken } from "@/services/authentication_service";
import { useUserProfile } from "@/store/useUserProfile";
import {
  addDays,
  endOfWeek,
  format,
  formatISO,
  parseISO,
  startOfWeek,
} from "date-fns";
import { computed, onMounted, ref } from "vue";

const props = defineProps<{
  appointments: Appointment[];
  selectedDoctor: Doctor | null;
  calendarDate: Date;
}>();

const timeSlots: string[] = createTimeSlots(9, 18, 10);
const { userId } = getUserIdAndToken();

const showAppointmentModal = ref<boolean>(false);
const { userDetails, fetchUserProfile } = useUserProfile();
const start = startOfWeek(props.calendarDate, { weekStartsOn: 1 }); //weekStartsOn: 1 - 1 inseamna Luni, 0 Duminica
const end = endOfWeek(props.calendarDate, { weekStartsOn: 1 });

const appointmentsForTheWeek = computed(() => {
  return props.appointments.filter((app) => {
    const appointmentDate = parseISO(app.date);
    return appointmentDate >= start && appointmentDate <= end;
  });
});

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
  return `${format(start, "d")} - ${format(end, "dd MMMM yyyy")}`;
});

function getAppointmentPosition(appointment: Appointment, startHour: number) {
  const [hour, minutes] = appointment.time.split(":").map(Number);
  const adjustedHour = hour - startHour;
  const start = adjustedHour * 6 + Math.floor(minutes / 10);
  const duration = Math.ceil(appointment.finalDuration / 10);

  return { start: start + 1, duration: duration };
}

function getAppointmentStyle(appointment: Appointment) {
  const { start, duration } = getAppointmentPosition(appointment, 9);
  let appointmentDay = new Date(appointment.date).getDay();

  console.log("app " + appointment.date + " " + appointmentDay);

  return {
    gridRowStart: start,
    gridRowEnd: start + duration,
    gridColumn: `${appointmentDay * 2 + 1} / span 1`,
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

  return appointmentsForTheWeek.value.some((app) => {
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
function handleAddAppointment() {
  console.log("add appointment");
}
function closeAppointmentModal() {
  showAppointmentModal.value = false;
}

onMounted(async () => {
  if (userDetails.value === null) {
    await fetchUserProfile();
  }
});
</script>
<template>
  <div class="customer-daily-calendar">
    <div class="calendar-header">
      <div class="header">
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
      <div class="weekdays">
        <div class="time-slot-block"></div>
        <div class="time-slot-block"></div>
        <div class="weekday-header" v-for="day in 5" :key="day">
          {{ format(addDays(start, day - 1), "EEEE") }}
        </div>
      </div>
    </div>
    <div class="calendar-table-container">
      <div class="calendar-grid">
        <div class="time-slot" v-for="(slot, index) in timeSlots" :key="index">
          <div class="hour-label" v-if="index % 6 === 0">
            <span> {{ slot.split(":")[0] }}:00 </span>
          </div>
          <div class="median-slot"></div>
          <div class="time-slot-block" v-if="!slotHasAppointment(slot)"></div>
          <div
            class="between-weekdays"
            :style="{ gridColumn: '4/ span 1 ' }"
          ></div>
          <div
            class="between-weekdays"
            :style="{ gridColumn: '6/ span 1 ' }"
          ></div>
          <div
            class="between-weekdays"
            :style="{ gridColumn: '8/ span 1 ' }"
          ></div>
          <div
            class="between-weekdays"
            :style="{ gridColumn: '10/ span 1 ' }"
          ></div>
        </div>
        <div
          v-for="appointment in appointmentsForTheWeek"
          :key="appointment.appointmentId"
          class="appointment-block"
          :style="getAppointmentStyle(appointment)"
        >
          <div v-if="appointment.customerId === userId">
            {{ userDetails?.firstName }} {{ userDetails?.lastName }} start:{{
              appointment.date
            }}
          </div>
          <div v-else>BUSY</div>
        </div>
      </div>
    </div>
  </div>
  <AddAppointmentModal
    v-if="showAppointmentModal"
    :visible="showAppointmentModal"
    :doctor="selectedDoctor"
    :date="calendarDate"
    :patient="userDetails"
    variant="CUSTOMER_APPOINTMENT"
    @add-appointment="handleAddAppointment"
    @close="closeAppointmentModal"
  />
</template>

<style lang="less" scoped>
@import (reference) "@/assets/styles.less";

.customer-daily-calendar {
  display: flex;
  flex-direction: column;

  .calendar-header {
    display: flex;
    flex-direction: column;
    color: @white;
    padding: 10px 0px 10px 40px;
    box-shadow: @smoke 0px 6px 12px;
    z-index: 10;

    .header {
      display: flex;
      justify-content: space-between;

      .displayed-date {
        display: flex;
        font-size: 22px;
        padding: 10px 0px 0px 40px;
      }

      .circle-plus-icon {
        border: 2px solid @white;
        font-size: 26px;
        border-radius: 100%;
        margin: 10px 60px 0px 0px;
        padding: 5px;
        width: 20px;
        height: 20px;
      }
    }

    .weekdays {
      display: grid;
      grid-template-rows: 40px;
      grid-template-columns:
        60px 10px
        calc(85vw / 6)
        5px
        calc(85vw / 6)
        5px
        calc(85vw / 6)
        5px
        calc(85vw / 6)
        5px
        calc(85vw / 6)
        5px;
      .weekday-header {
        display: flex;
        align-items: center;
        justify-content: center;
        grid-column: span 2;
        font-weight: bold;
        color: @dark-gray;
      }
    }
  }

  .calendar-table-container {
    margin: 0px 80px 20px 40px;
    background-color: @light-smoke;
    overflow-y: scroll;
    max-height: 72vh;

    .calendar-grid {
      display: grid;
      grid-template-columns:
        60px 10px
        calc(85vw / 6)
        5px
        calc(85vw / 6)
        5px
        calc(85vw / 6)
        5px
        calc(85vw / 6)
        5px
        calc(85vw / 6)
        5px;
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

          height: 100%;
          width: 100%;
          background-color: transparent;

          &:hover {
            display: block;
            background-color: gray;
          }
        }

        .between-weekdays {
          background-color: @sugar;
          border: 1px solid @sugar;
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
