<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import DashboardPieChart from "@/components/DashboardPieChart.vue";
import { DoctorSidebarOptions } from "@/data/types/SidebarOptions";
import { AppointmentDetail, TreatmentType } from "@/data/types/Entities";
import { computed, onMounted, ref } from "vue";
import { getServicesInCurrentMonth } from "@/services/doctor_service";
import { getUpcomingAppointments } from "@/services/appointments_service";
import { formatDate, formatTime } from "@/utils/helpers";

const treatments = ref<TreatmentType[]>();
const appointments = ref<AppointmentDetail[]>();

onMounted(() => {
  getTreatmentTypes();
  loadUpcomingAppointments();
});

async function getTreatmentTypes() {
  await getServicesInCurrentMonth(2).then((res) => (treatments.value = res));
  console.log(
    getServicesInCurrentMonth(2).then((res) => (treatments.value = res))
  );
}

async function loadUpcomingAppointments() {
  await getUpcomingAppointments(2)
    .then((res) => (appointments.value = res))
    .catch((e) => console.log(e));
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

            <div v-if="appointments?.length === 0">
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

    .upper {
      display: flex;
      gap: 3vw;
    }

    .right-side {
      width: 53vw;
    }

    .upcoming {
      border: 1px solid @gray;
      border-radius: 20px;
      padding: 30px;

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
    }

    h1 {
      text-align: center;
      color: @gray;
    }
  }
}
</style>
