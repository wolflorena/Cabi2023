<script setup lang="ts">
import { ref, onMounted, watch } from "vue";
import Sidebar from "@/components/Sidebar.vue";
import TableHeaderButton from "@/components/TableHeaderButton.vue";
import AddButton from "@/components/AddButton.vue";
import CustomCheckbox from "@/components/CustomCheckbox.vue";
import Pagination from "@/components/Pagination.vue";
import CustomModal from "@/components/CustomModal.vue";
import { AdminSidebarOptions } from "@/data/types/SidebarOptions";

import {
  AppointmentDetail,
  AppointmentAdmin,
  type SelectedDoctor,
} from "@/data/types/Entities";
import { getAllDoctors } from "@/services/doctor_service";
import {
  getById,
  getAllPageable,
  deleteAppointment,
} from "@/services/appointments_service";

const showModal = ref(false);
const showInfo = ref(false);
const showDelete = ref(false);
const appointmentDetails = ref<AppointmentDetail>();

const doctors = ref<SelectedDoctor[]>([]);
const appointments = ref<AppointmentAdmin[]>([]);

const showUpcoming = ref(true);
const showAwaiting = ref(false);
const showHistory = ref(false);

const currentPage = ref(1);
const totalPages = ref(0);

async function loadDoctors() {
  await getAllDoctors().then((res: any) => {
    doctors.value = res.map((doctor: any) => ({ ...doctor, checked: true }));
  });
}

async function loadAppointments() {
  await getAllPageable(10, currentPage.value - 1).then((res: any) => {
    // Clear existing appointments to avoid duplication or irrelevant data
    appointments.value = [];
    // Filter and add appointments based on the checked status of doctors
    res.pagedAppointments.content.forEach((element: any) => {
      const doctor = doctors.value.find(
        (d) => d.id === element.doctorId && d.checked
      );
      if (doctor) {
        appointments.value.push(element);
      }
    });
    totalPages.value = Math.ceil(res.total / 10);
  });
}

async function changePage(pageNumber: number) {
  currentPage.value = pageNumber;
  await getAllPageable(10, currentPage.value - 1).then((res: any) => {
    appointments.value = [];

    res.pagedAppointments.content.forEach((element: any) => {
      const doctor = doctors.value.find(
        (d) => d.id === element.doctorId && d.checked
      );
      if (doctor) {
        appointments.value.push(element);
      }
    });
    totalPages.value = Math.ceil(res.total / 10);
  });
}

onMounted(() => {
  loadDoctors();
  loadAppointments();
});

watch(
  doctors,
  () => {
    currentPage.value = 1;
    loadAppointments();
  },
  { deep: true }
);

async function showInfoModal(appointmentId: number) {
  showInfo.value = true;
  await getById(appointmentId).then((res) => {
    appointmentDetails.value = res;
  });
}

async function showDeleteModal(appointmentId: number) {
  showDelete.value = true;
  await getById(appointmentId).then((res) => {
    appointmentDetails.value = res;
  });
}

async function deleteAppointmentById(appointmentId: number | undefined) {
  if (appointmentId) {
    await deleteAppointment(appointmentId)
      .then((res) => {
        if (res.ok) {
          console.log("Appointment successfull deleted!");
          showDelete.value = false;
          loadAppointments();
        }
      })
      .catch((error) => {
        console.log(error);
      });
  } else return;
}

function formatDateForTable(dateStr: string): string {
  const monthsOfYear = [
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sept",
    "Oct",
    "Nov",
    "Dec",
  ];
  if (dateStr) {
    const date = new Date(dateStr);
    const dayOfMonth = date.getDate();
    const year = date.getFullYear();
    const month = monthsOfYear[date.getMonth()];

    return `${dayOfMonth} ${month} ${year}`;
  }
  return "";
}

//the string format of the date from the server is: YYYY-MM-DD
function formatDate(dateStr: string | undefined): string {
  const daysOfWeek = [
    "Sunday",
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
  ];
  const monthsOfYear = [
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
  if (dateStr) {
    const date = new Date(dateStr);
    const dayOfWeek = daysOfWeek[date.getDay()];
    const dayOfMonth = date.getDate();
    const month = monthsOfYear[date.getMonth()];

    const dayWithSuffix = getDayWithSuffix(dayOfMonth);

    return `${dayOfWeek}, ${dayWithSuffix} of ${month}`;
  }
  return "";
}

function getDayWithSuffix(day: number): string {
  if (day % 10 === 1 && day !== 11) {
    return `${day}st`;
  } else if (day % 10 === 2 && day !== 12) {
    return `${day}nd`;
  } else if (day % 10 === 3 && day !== 13) {
    return `${day}rd`;
  } else {
    return `${day}th`;
  }
}

function formatTime(timeStr: string | undefined): string {
  if (timeStr) {
    const timeParts = timeStr.split(":");
    const date = new Date();
    date.setHours(
      parseInt(timeParts[0]),
      parseInt(timeParts[1]),
      parseInt(timeParts[2])
    );

    let hours = date.getHours();
    const minutes = date.getMinutes();

    const amPm = hours >= 12 ? "PM" : "AM";

    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'

    const formattedMinutes = minutes < 10 ? `0${minutes}` : minutes;
    return `${hours}:${formattedMinutes} ${amPm}`;
  }
  return "";
}
</script>

<template>
  <div class="container">
    <Sidebar :options="AdminSidebarOptions" />
    <div class="settings">
      <AddButton @click="showModal = true" />

      <div class="doctors">
        <div class="title">
          <h4>Select doctors</h4>
        </div>
        <div v-for="doctor in doctors" :key="doctor.id">
          <CustomCheckbox
            v-model="doctor.checked"
            :text="`Dr. ${doctor.firstName} ${doctor.lastName}`"
            :uuid="`checkbox-doctor-${doctor.id}`"
          />
        </div>
      </div>
    </div>
    <div class="appointments">
      <div class="header">
        <div class="controls">
          <TableHeaderButton label="Upcoming" :active="showUpcoming" />
          <TableHeaderButton
            label="Awaiting confirmation"
            :active="showAwaiting"
          />
          <TableHeaderButton label="History" :active="showHistory" />
        </div>
      </div>
      <div
        class="appointments-container"
        :class="{ empty: appointments.length === 0 }"
      >
        <table v-if="appointments.length > 0">
          <thead>
            <tr>
              <th>No.#</th>
              <th>Patient</th>
              <th>Date</th>
              <th>Doctor</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(appointment, index) in appointments">
              <td>{{ 10 * (currentPage - 1) + index + 1 }}</td>
              <td>
                {{
                  appointment.customerFirstName +
                  " " +
                  appointment.customerLastName
                }}
              </td>
              <td>
                <div class="date">
                  <span id="time">{{ formatTime(appointment.time) }} </span>
                  <span id="date">
                    {{ formatDateForTable(appointment.date) }}
                  </span>
                </div>
              </td>
              <td>
                {{
                  "Dr. " +
                  appointment.doctorFirstName +
                  " " +
                  appointment.doctorLastName
                }}
              </td>
              <td>
                <div class="actions">
                  <button @click="showInfoModal(appointment.appointmentId)">
                    <font-awesome-icon icon="eye" id="icon" />
                  </button>
                  <button>
                    <font-awesome-icon icon="pen" id="icon" />
                  </button>
                  <button @click="showDeleteModal(appointment.appointmentId)">
                    <font-awesome-icon icon="trash-can" id="icon" />
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        <img src="../../assets/nodata.svg" alt="" v-else />

        <Pagination
          :total-pages="totalPages"
          :current-page="currentPage"
          @change-page="changePage"
        />
      </div>
      <CustomModal
        :show="showInfo"
        title="Appointment"
        @button2="showInfo = false"
        class="appointment-info"
      >
        <div class="detail">
          <span>Patient</span>
          <span>{{
            appointmentDetails?.customerFirstName +
            " " +
            appointmentDetails?.customerLastName
          }}</span>
        </div>
        <div class="detail">
          <span>Doctor</span>
          <span>{{
            "Dr. " +
            appointmentDetails?.doctorFirstName +
            " " +
            appointmentDetails?.doctorLastName
          }}</span>
        </div>
        <div class="detail">
          <span>Date</span>
          <span>{{ formatDate(appointmentDetails?.date) }}</span>
        </div>
        <div class="detail">
          <span>Hour</span>
          <span>{{ formatTime(appointmentDetails?.time) }}</span>
        </div>
        <div class="detail">
          <span>Type of treatment</span>
          <span>{{ appointmentDetails?.serviceName }}</span>
        </div>
      </CustomModal>
      <CustomModal
        :show="showDelete"
        @button2="showDelete = false"
        @button1="deleteAppointmentById(appointmentDetails?.appointmentId)"
      >
        <span class="delete-text">{{
          "Are you sure you want to delete the appointment for " +
          appointmentDetails?.customerFirstName +
          " " +
          appointmentDetails?.customerLastName +
          "?"
        }}</span></CustomModal
      >
    </div>
  </div>

  <CustomModal
    :show="showModal"
    @button2="showModal = false"
    title="New appointment2"
  >
  </CustomModal>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";
.container {
  display: flex;
  align-items: center;

  .settings {
    height: 100vh;
    width: 20vw;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 20px;

    .doctors {
      width: 90%;

      .title {
        color: @gray;
        margin-left: 6px;
      }
    }
  }

  .appointments {
    width: 65vw;
    position: absolute;
    top: 0;
    right: 0;
    .header {
      background-color: @gray;
      height: 5vh;
      width: 100%;
      border-bottom-left-radius: 40px;

      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      user-select: none;

      .controls {
        width: 100%;
        display: flex;
        justify-content: space-around;
      }
    }

    .appointments-container {
      width: 100%;
      height: 95vh;
      position: relative;
      &.empty {
        background-color: @light-gray;
        border-top-left-radius: 40px;
      }

      table {
        width: 100%;
        border-collapse: collapse;
        color: @gray;

        thead tr {
          height: 8vh;
          background-color: @light-gray;
          font-size: large;
        }

        tbody tr {
          height: 8vh;
          font-size: 20px;
          td {
            text-align: center;
            .date {
              display: flex;
              flex-direction: column;

              #time {
                font-size: 20px;
                font-weight: 500;
              }

              #date {
                font-size: 12px;
              }
            }

            .actions {
              button {
                border: none;
                background-color: transparent;
                cursor: pointer;
              }
            }
          }
          &:hover {
            background-color: @sugar;
          }
        }
        tr:nth-child(even) {
          background-color: @light-gray;
        }
      }

      img {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 70%;
      }
    }

    .appointment-info {
      display: flex;
      color: @white;
      font-size: 15px;
      .detail {
        padding: 10px;
        display: flex;
        justify-content: space-between;
      }
    }

    .delete-text {
      color: @white;
    }
  }
}
</style>