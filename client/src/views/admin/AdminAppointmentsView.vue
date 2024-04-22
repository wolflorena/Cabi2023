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
  Appointment,
  AppointmentAdmin,
  type SelectedDoctor,
} from "@/data/types/Entities";
import { getAllDoctors } from "@/services/doctor_service";
import { getById, getAllPageable } from "@/services/appointments_service";

const showModal = ref(false);
const showInfo = ref(true);
const appointmentDetails = ref<Appointment>();

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
                  <span> {{ appointment.date }} </span>
                  <span>{{ appointment.time }} </span>
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
                  <button>
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
        title="appointment"
        @button2="showInfo = false"
        class="appointment-info"
      >
        <div class="detail">
          <span>Patient</span>
          <span>{{ appointmentDetails?.customerId }}</span>
        </div>
        <div class="detail">
          <span>Doctor</span>
          <span>{{ appointmentDetails?.doctorId }}</span>
        </div>
        <div class="detail">
          <span>Date</span>
          <span>{{ appointmentDetails?.date }}</span>
        </div>
        <div class="detail">
          <span>Hour</span>
          <span>{{ appointmentDetails?.time }}</span>
        </div>
        <div class="detail">
          <span>Type of treatment</span>
          <span>{{ appointmentDetails?.serviceId }}</span>
        </div>
      </CustomModal>
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
        }

        tbody tr {
          height: 8vh;
          td {
            text-align: center;
            .date {
              display: flex;
              flex-direction: column;
            }

            .actions {
              button {
                border: none;
                background-color: transparent;
                cursor: pointer;
              }
            }
          }
        }
        tr:nth-child(even) {
          background-color: @light-gray;
        }

        tr:hover {
          background-color: @sugar;
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
        // flex-direction: column;
        justify-content: space-between;
      }
    }
  }
}
</style>
