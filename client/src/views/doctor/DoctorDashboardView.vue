<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import DashboardPieChart from "@/components/DashboardPieChart.vue";
import { DoctorSidebarOptions } from "@/data/types/SidebarOptions";
import { AppointmentDetail, TreatmentType } from "@/data/types/Entities";
import { computed, onMounted, ref } from "vue";
import { getServicesInCurrentMonth } from "@/services/doctor_service";
import {
  getTotalAppointments,
  getUpcomingAppointments,
} from "@/services/appointments_service";
import { formatDate, formatTime } from "@/utils/helpers";

const treatments = ref<TreatmentType[]>();
const appointments = ref<AppointmentDetail[]>();
const types = ref<string[]>([
  "SCHEDULED",
  "REQUESTED",
  "COMPLETED",
  "CANCELLED",
]);
const appointmentsCount = ref<Record<string, number>>({});

onMounted(() => {
  getTreatmentTypes();
  loadUpcomingAppointments();
  loadScheduledAppointments();
});

async function getTreatmentTypes() {
  await getServicesInCurrentMonth(2).then((res) => (treatments.value = res));
}

async function loadUpcomingAppointments() {
  await getUpcomingAppointments(2)
    .then((res) => (appointments.value = res))
    .catch((e) => console.log(e));
}

async function loadScheduledAppointments() {
  for (const type of types.value) {
    const count = await getScheduledAppointmentsNumber(type);
    appointmentsCount.value[type] = count;
  }
}

async function getScheduledAppointmentsNumber(status: string): Promise<number> {
  const res = await getTotalAppointments(2, status);
  return res;
}

const formattedDate = computed(() => {
  const currentDate = new Date();
  console.log(currentDate);

  const monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];
  const month = monthNames[currentDate.getMonth()];
  const year = currentDate.getFullYear();

  return `${month} ${year}`;
});
</script>

<template>
  <div class="container">
    <Sidebar :options="DoctorSidebarOptions" />

    <div class="header">
      <span>{{ formattedDate }}</span>
    </div>

    <div class="dashboard">
      <div class="upper">
        <DashboardPieChart :treatments="treatments" />

        <div class="right-side">
          <div class="upcoming">
            <h1>Upcoming appointments for today, {{ formatDate(Date()) }}</h1>

            <div v-if="appointments?.length === 0" class="message">
              <p>No upcoming appointments for today.</p>
            </div>
            <div class="details" v-else>
              <div
                class="appointment-detail"
                v-for="appointment in appointments"
              >
                <h3>{{ formatTime(appointment.time) }}</h3>
                <h3>{{ appointment.serviceName }}</h3>
                <h3>
                  {{
                    appointment.customerFirstName +
                    " " +
                    appointment.customerLastName
                  }}
                </h3>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="down">
        <div class="cards">
          <div class="card" v-for="type in types">
            <h1>{{ type }}</h1>
            <h1>
              {{ appointmentsCount[type] }}
            </h1>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;

  .header {
    position: absolute;
    top: 0;
    right: 0;
    background-color: @gray;
    height: 7vh;
    width: 50vw;
    border-bottom-left-radius: 40px;

    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 30px;

    span {
      color: @white;
      font-size: large;
      font-size: 25px;
    }
  }
  .dashboard {
    margin: 100px 0 0 30px;
    display: flex;
    flex-direction: column;

    .upper {
      display: flex;
      flex-wrap: wrap;

      gap: 3vw;

      .right-side {
        width: 53vw;

        .upcoming {
          border: 1px solid @gray;
          border-radius: 20px;
          padding: 30px;
          height: calc(50% - 60px);

          .details {
            display: flex;
            flex-direction: column;
            gap: 10px;
            .appointment-detail {
              h3 {
                margin: 0;
                font-weight: 100;
              }
            }
          }

          .message {
            height: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
          }
        }
      }
    }

    h1 {
      text-align: center;
      color: @gray;
      user-select: none;
    }

    .down {
      height: 30vh;
      display: flex;
      justify-content: center;
      align-items: center;

      .cards {
        display: flex;
        flex-wrap: wrap;
        gap: 5vw;

        .card {
          background-color: @gray;
          border-radius: 20px;
          padding: 10px;

          h1 {
            color: @white;
          }
        }
      }
    }
  }
}
</style>
