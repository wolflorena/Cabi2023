<script setup lang="ts">
import { ref, onMounted, watch } from "vue";
import Sidebar from "@/components/Sidebar.vue";
import TableHeaderButton from "@/components/TableHeaderButton.vue";
import TableHeader from "@/components/TableHeader.vue";
import CustomCheckbox from "@/components/CustomCheckbox.vue";
import Pagination from "@/components/Pagination.vue";
import CustomModal from "@/components/CustomModal.vue";
import AddAppointmentModal from "@/components/AddAppointmentModal.vue";
import DateAndTimeSpan from "@/components/DateAndTimeSpan.vue";
import { AdminSidebarOptions } from "@/data/types/SidebarOptions";
import TableRow from "@/components/TableRow.vue";

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
  updateStatus,
  createAppointment,
} from "@/services/appointments_service";
import { formatTime, formatDate } from "@/utils/helpers";
import ActionButton from "@/components/ActionButton.vue";

const showModal = ref(false);
const showInfo = ref(false);
const showDelete = ref(false);
const appointmentDetails = ref<AppointmentDetail>();

const doctors = ref<SelectedDoctor[]>([]);
const doctorIds = ref<number[]>([]);
const appointments = ref<AppointmentAdmin[]>([]);

const appointmentStatus = ref("SCHEDULED");

const currentPage = ref(1);
const totalPages = ref(0);

async function loadDoctors() {
  await getAllDoctors().then((res: any) => {
    doctors.value = res.map((doctor: any) => ({ ...doctor, checked: true }));
  });
}

async function loadAppointments() {
  computeSelectedDoctorIds();
  await getAllPageable(
    10,
    currentPage.value - 1,
    doctorIds.value,
    appointmentStatus.value
  ).then((res: any) => {
    appointments.value = res.pagedAppointments.content;
    totalPages.value = Math.ceil(res.total / 10);
  });
}

async function changePage(pageNumber: number) {
  currentPage.value = pageNumber;
  await getAllPageable(
    10,
    currentPage.value - 1,
    doctorIds.value,
    appointmentStatus.value
  ).then((res: any) => {
    appointments.value = res.pagedAppointments.content;
    totalPages.value = Math.ceil(res.total / 10);
  });
}

function computeSelectedDoctorIds() {
  doctorIds.value = doctors.value
    .filter((doctor) => doctor.checked === true)
    .map((doctor) => doctor.id);
  console.log(doctorIds.value);
}

onMounted(() => {
  loadDoctors();
  loadAppointments();
});

watch(
  [doctors, appointmentStatus],
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

async function updateAppointmentStatus(appointmentId: number, status: string) {
  if (appointmentId) {
    await updateStatus(appointmentId, status).then((res) => {
      if (res) {
        loadAppointments();
      }
    });
  } else return;
}

function prepareAddAppointmentModal() {
  showModal.value = true;
}

function closeModal() {
  showModal.value = false;
}

async function addAppointment(
  date: string,
  hour: string,
  doctor: number,
  service: number,
  patient: number
) {
  if (date && hour && doctor && service && patient) {
    await createAppointment(date, hour, doctor, service, patient).then((res) =>
      console.log(res)
    );
    showModal.value = false;
  }
}
</script>

<template>
  <div class="container">
    <Sidebar :options="AdminSidebarOptions" />
    <div class="settings">
      <ActionButton
        button-style="full"
        icon-token="circle-plus"
        @action-triggered="prepareAddAppointmentModal"
      />
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
          <TableHeaderButton
            label="Upcoming"
            :active="appointmentStatus === 'SCHEDULED'"
            @click="appointmentStatus = 'SCHEDULED'"
          />
          <TableHeaderButton
            label="Awaiting confirmation"
            :active="appointmentStatus === 'REQUESTED'"
            @click="appointmentStatus = 'REQUESTED'"
          />
          <TableHeaderButton
            label="History"
            :active="appointmentStatus === 'COMPLETED'"
            @click="appointmentStatus = 'COMPLETED'"
          />
        </div>
      </div>
      <div
        class="appointments-container"
        :class="{ empty: appointments.length === 0 }"
      >
        <table v-if="appointments.length > 0">
          <TableHeader
            :columns="
              appointmentStatus == 'COMPLETED'
                ? ['Patient', 'Date', 'Doctor', 'Status']
                : ['Patient', 'Date', 'Doctor', 'Actions']
            "
            :is-main="false"
            :has-empty-row="false"
          />
          <tbody>
            <TableRow
              v-for="(appointment, index) in appointments"
              :columns="[
                10 * (currentPage - 1) + index + 1,
                appointment.customerFirstName +
                  ' ' +
                  appointment.customerLastName,
                'Dr. ' +
                  appointment.doctorFirstName +
                  ' ' +
                  appointment.doctorLastName,
              ]"
              :date="appointment.date"
              :time="appointment.time"
              :index="index"
              highlight="odd"
            >
              <div class="actions" v-if="appointmentStatus === 'SCHEDULED'">
                <ActionButton
                  iconToken="eye"
                  @action-triggered="showInfoModal(appointment.appointmentId)"
                />

                <ActionButton iconToken="pen" />

                <ActionButton
                  iconToken="trash-can"
                  @action-triggered="showDeleteModal(appointment.appointmentId)"
                />
              </div>
              <div class="actions" v-if="appointmentStatus === 'REQUESTED'">
                <ActionButton
                  iconToken="check"
                  @action-triggered="
                    updateAppointmentStatus(
                      appointment.appointmentId,
                      'SCHEDULED'
                    )
                  "
                />

                <ActionButton iconToken="pen" />

                <ActionButton
                  iconToken="xmark"
                  @action-triggered="
                    updateAppointmentStatus(
                      appointment.appointmentId,
                      'CANCELLED'
                    )
                  "
                />
              </div>
              <span
                class="status"
                v-if="appointmentStatus === 'COMPLETED'"
                :class="{ canceled: appointment.status === 'CANCELLED' }"
              >
                {{ appointment.status }}
              </span>
            </TableRow>
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

  <AddAppointmentModal
    :visible="showModal"
    @add-appointment="addAppointment"
    @close="closeModal"
  />
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
      max-height: 30vh;
      overflow-y: scroll;

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

        .status {
          background-color: @green;
          padding: 5px;

          &.canceled {
            background-color: @red;
          }
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
