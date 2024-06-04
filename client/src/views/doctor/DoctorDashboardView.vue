<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import DashboardPieChart from "@/components/DashboardPieChart.vue";
import { DoctorSidebarOptions } from "@/data/types/SidebarOptions";
import {
  AppointmentDetail,
  TreatmentType,
  AppointmentWeekly,
} from "@/data/types/Entities";
import { computed, onMounted, ref } from "vue";
import { getServicesInCurrentMonth } from "@/services/doctor_service";
import {
  getTotalAppointments,
  getUpcomingAppointments,
  getWeeklyAppointmentsNumber,
} from "@/services/appointments_service";
import { formatDate, formatTime } from "@/utils/helpers";
import BarChart from "@/components/BarChart.vue";
import LoadingSpinner from "@/components/LoadingSpinner.vue";

const treatments = ref<TreatmentType[]>();
const appointments = ref<AppointmentDetail[]>();
const types = ref<string[]>([
  "SCHEDULED",
  "REQUESTED",
  "COMPLETED",
  "CANCELLED",
]);
const appointmentsCount = ref<Record<string, number>>({});
const appointmentsWeekly = ref<AppointmentWeekly[]>([]);

const treatmentsAreLoading = ref(false);
const upcomingIsLoading = ref(false);
const barChartIsLoading = ref(false);
const numbersAreLoading = ref(false);

onMounted(() => {
  getTreatmentTypes();
  loadUpcomingAppointments();
  loadScheduledAppointments();
  getDataForBarChart();
});

async function getTreatmentTypes() {
  treatmentsAreLoading.value = true;
  await getServicesInCurrentMonth(26).then((res) => {
    treatments.value = res;
    treatmentsAreLoading.value = false;
  });
}

async function loadUpcomingAppointments() {
  upcomingIsLoading.value = true;
  await getUpcomingAppointments(1)
    .then((res) => {
      appointments.value = res;
      upcomingIsLoading.value = false;
    })
    .catch((e) => console.log(e));
}

async function loadScheduledAppointments() {
  numbersAreLoading.value = true;
  for (const type of types.value) {
    const count = await getScheduledAppointmentsNumber(type);
    appointmentsCount.value[type] = count;
  }
  numbersAreLoading.value = false;
}

async function getScheduledAppointmentsNumber(status: string): Promise<number> {
  const res = await getTotalAppointments(1, status);
  return res;
}

async function getDataForBarChart() {
  barChartIsLoading.value = true;
  await getWeeklyAppointmentsNumber(3).then((res) => {
    appointmentsWeekly.value = res;
    barChartIsLoading.value = false;
  });
}

const formattedDate = computed(() => {
  const currentDate = new Date();

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
        <DashboardPieChart
          v-if="!treatmentsAreLoading"
          :treatments="treatments"
        />
        <div v-else class="treatments">
          <LoadingSpinner />
        </div>
        <div class="right-side">
          <div class="upcoming" v-if="!upcomingIsLoading">
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
          <div class="upcoming" v-else style="position: relative">
            <LoadingSpinner />
          </div>
          <BarChart v-if="!barChartIsLoading" :data="appointmentsWeekly" />
          <div
            v-else
            class="barchart-container"
            style="height: calc(50% - 60px); position: relative"
          >
            <LoadingSpinner />
          </div>
        </div>
      </div>

      <div class="down">
        <div class="cards">
          <div class="card" v-for="type in types" style="position: relative">
            <h1>{{ type }}</h1>
            <h1 v-if="!numbersAreLoading">
              {{ appointmentsCount[type] }}
            </h1>
            <LoadingSpinner v-else />
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

      .treatments {
        height: 33vw;
        border: 1px solid @gray;
        border-radius: 20px;
        padding: 30px;
        width: 300px;
        position: relative;
      }

      .right-side {
        width: 53vw;
        display: flex;
        flex-direction: column;
        gap: 20px;

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
            font-size: 20px;
            color: @gray;
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
          height: 15vh;
          width: auto;

          h1 {
            color: @white;
          }
        }
      }
    }
  }
}
</style>
