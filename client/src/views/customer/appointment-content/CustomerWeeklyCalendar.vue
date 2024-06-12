<script setup lang="ts">
import AddAppointmentModal from "@/components/AddAppointmentModal.vue";
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
  differenceInDays,
  endOfWeek,
  format,
  formatISO,
  getDay,
  isWeekend,
  isWithinInterval,
  parseISO,
  startOfWeek,
} from "date-fns";
import Swal from "sweetalert2";
import { computed, onMounted, ref, watch } from "vue";

const props = defineProps<{
  appointments: AppointmentCalendar[];
  selectedDoctor: Doctor | null;
  unavailabilites: Vacation[] | null;
  calendarDate: Date;
}>();

const emits = defineEmits<{
  createdAppointment: [];
}>();

const timeSlots: string[] = createTimeSlots(9, 18, 10);
const { userId } = getUserIdAndToken();
const showAppointmentModal = ref<boolean>(false);
const { userDetails, fetchUserProfile } = useUserProfile();
const start = ref(startOfWeek(props.calendarDate, { weekStartsOn: 1 })); //weekStartsOn: 1 - 1 inseamna Luni, 0 Duminica
const end = ref(addDays(start.value, 4)); // end e setat ca fiind start Date(LUNI) + 4 rezultand end(VINERI)
const appointmentsForTheWeek = ref<AppointmentCalendar[]>([]);

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
  return `${format(start.value, "d")} - ${format(
    addDays(start.value, 4),
    "dd MMMM yyyy"
  )}`;
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
  let appointmentDay = new Date(appointment.date).getDay();

  return {
    gridRowStart: start,
    gridRowEnd: start + duration,
    gridColumn: `${appointmentDay * 2 + 1} / span 1`,
  };
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
  Swal.fire({
    icon: "info",
    title: "Appointing :)",
    showCancelButton: false,
  });
  const resp = await createAppointment(date, hour, doctorId, serviceId, userId);
  if (resp.ok) {
  }
  emits("createdAppointment");
}
function closeAppointmentModal() {
  showAppointmentModal.value = false;
}

interface unavailableForDays {
  wholeDay: boolean;
  fromPosition: number;
  toPosition: number;
  columnPosition: number;
}

const unavailabilityForDays = computed((): unavailableForDays[] => {
  if (props.unavailabilites) {
    let unavailabilitesArray = props.unavailabilites.filter((unav) => {
      let unavailabilyDate = new Date(unav.startDate);
      return isWithinInterval(unavailabilyDate, {
        start: start.value,
        end: addDays(end.value, 1),
      });
    });
    return unavailabilitesArray.map((unav) => {
      if (unav.startTime === null) {
        let colPos = getDay(new Date(unav.startDate)) * 2 + 1;

        return {
          wholeDay: true,
          fromPosition: 1,
          toPosition: 55,
          columnPosition: colPos,
        };
      } else {
        let [startHour, startMinute] = unav.startTime.split(":").map(Number);
        let [endHour, endMinute] = unav.endTime.split(":").map(Number);
        let fromPos = (startHour - 9) * 6 + 1 + Math.floor(startMinute / 10);
        let toPos = (endHour - 9) * 6 + 1 + Math.ceil(endMinute / 10);
        let colPos = getDay(new Date(unav.startDate)) * 2 + 1;
        return {
          wholeDay: false,
          fromPosition: fromPos,
          toPosition: toPos,
          columnPosition: colPos,
        };
      }
    });
  }

  return [];
});

// start si end sunt actualizate atunci cand calendarul se schimba.
watch(
  () => props.calendarDate,
  (newDate) => {
    start.value = startOfWeek(newDate, { weekStartsOn: 1 });
    end.value = addDays(start.value, 4);

    appointmentsForTheWeek.value = props.appointments.filter((app) => {
      const appointmentDate = parseISO(app.date);
      return appointmentDate >= start.value && appointmentDate <= end.value;
    });
    console.log(appointmentsForTheWeek.value);
  }
);

watch(
  () => props.appointments,
  (newAppointments) => {
    appointmentsForTheWeek.value = newAppointments.filter((app) => {
      const appointmentDate = parseISO(app.date);
      return appointmentDate >= start.value && appointmentDate <= end.value;
    });
  },
  { immediate: true }
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
        <div
          class="weekday-header"
          v-for="day in 5"
          :key="day"
          :style="{ gridColumn: `${1 + 2 * day}/ span 1` }"
        >
          {{ format(addDays(start, day - 1), "dd") }},
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
            <span> service name: <br />{{ appointment.serviceName }} </span>
          </div>
          <div v-else>BUSY</div>
        </div>
        <div
          v-for="(unavailability, index) in unavailabilityForDays"
          :key="index"
          class="appointment-block"
          :style="{
            gridRowStart: `${unavailability.fromPosition}`,
            gridRowEnd: `${unavailability.toPosition}`,
            gridColumn: `${unavailability.columnPosition}/ span 1`,
          }"
        >
          <span v-if="unavailability.wholeDay">Unavailable TODAY</span>
          <span v-else>UNAVAILABLE</span>
        </div>
      </div>
    </div>
  </div>
  <CustomerAddsAppointment
    variant="WEEK"
    :visible="showAppointmentModal"
    :selectedDoctor="selectedDoctor"
    :selectedDate="calendarDate"
    :userDetails="userDetails"
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
        color: @white;
      }
    }
  }

  .calendar-table-container {
    margin: 10px 80px 20px 40px;
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
