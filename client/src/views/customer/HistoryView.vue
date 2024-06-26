<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import { CustomerSidebarOptions } from "@/data/types/SidebarOptions";
import { computed, onMounted, ref, watch } from "vue";

import TableHeader from "@/components/TableHeader.vue";
import TableRow from "@/components/TableRow.vue";
import {
  AppointmentCalendar,
  HistoryAppointmentCalendar,
} from "@/data/types/Entities";
import { useUserAppointments } from "@/store/useUserAppointments";
import Pagination from "@/components/Pagination.vue";
import LoadingSpinner from "@/components/LoadingSpinner.vue";
import Swal from "sweetalert2";
import { updateStatus } from "@/services/appointments_service";
import { SwalLoading } from "@/utils/helpers";
import { useLoadAppointments } from "@/store/useLoadAppointments";

const {
  userHistoryAppointments,
  totalPages,
  isLoading,
  changePage,
  fetchUserHistoryAppointments,
} = useUserAppointments();

const { fetchAppointments } = useLoadAppointments();

const currentPage = ref<number>(1);
const pageNumber = ref<number>(1);
const noAppointments = ref<boolean>(false);

function handleChangePage(pageNumber: number) {
  currentPage.value = pageNumber;
  changePage(pageNumber);
}

function doctorFullName(firstName: string, lastName: string): string {
  return "Dr. " + firstName + " " + lastName;
}

function handleClickOnAppointment(appointment: HistoryAppointmentCalendar) {
  if (
    appointment.status === "SCHEDULED" ||
    appointment.status === "REQUESTED"
  ) {
    Swal.fire({
      html: `<span style="color: #84ce95">${
        appointment.serviceName
      }</span> on <span style="color: #84ce95">${
        appointment.date
      }</span>, at <span style="color: #84ce95">${
        appointment.time
      }</span></br> with <span style="color: #84ce95">${doctorFullName(
        appointment.doctorFirstName,
        appointment.doctorLastName
      )}</span>`,
      title: "Do you want to cancel your appointment?",
      icon: "question",
      showCancelButton: true,
    }).then(async (res) => {
      if (res.isConfirmed) {
        SwalLoading.fire();
        await updateStatus(appointment.appointmentId, "CANCELLED");
        await fetchUserHistoryAppointments(pageNumber.value, true);
        await fetchAppointments(true);
        handleChangePage(1);
        SwalLoading.close();
      }
    });
  } else {
    Swal.fire({
      html: `<span style="color: #84ce95">${
        appointment.serviceName
      }</span> on <span style="color: #84ce95">${
        appointment.date
      }</span>, at <span style="color: #84ce95">${
        appointment.time
      }</span></br> with <span style="color: #84ce95">${doctorFullName(
        appointment.doctorFirstName,
        appointment.doctorLastName
      )}</span>`,
      titleText: "Your appointment",
      icon: "info",
    });
  }
}

onMounted(async () => {
  await fetchUserHistoryAppointments(pageNumber.value, true);
  if (userHistoryAppointments.value.length < 1) {
    noAppointments.value = true;
  }
});
</script>

<template>
  <div class="container">
    <Sidebar :options="CustomerSidebarOptions" />
    <div class="history-container">
      <div class="history-content">
        <table v-if="!noAppointments && !isLoading">
          <TableHeader
            :columns="['Service', 'Doctor', 'Date', 'Status']"
            :has-empty-row="false"
          />
          <tbody>
            <TableRow
              v-for="(appointment, index) in userHistoryAppointments"
              :columns="[
                10 * (currentPage - 1) + index + 1,
                appointment.serviceName,
                doctorFullName(
                  appointment.doctorFirstName,
                  appointment.doctorLastName
                ),
              ]"
              :date="appointment.date"
              :time="appointment.time"
              :index="index"
              @click="handleClickOnAppointment(appointment)"
              highlight="odd"
            >
              <span
                class="status"
                :class="{
                  canceled: appointment.status === 'CANCELLED',
                  scheduled: appointment.status === 'SCHEDULED',
                  requested: appointment.status === 'REQUESTED',
                  completed: appointment.status === 'COMPLETED',
                }"
              >
                {{ appointment.status }}
              </span>
            </TableRow>
          </tbody>
        </table>
        <LoadingSpinner class="loading-spinner" v-else-if="!noAppointments" />
        <div v-else class="no-app-yet">No Appointments Yet</div>
      </div>
      <Pagination
        :total-pages="totalPages"
        :current-page="currentPage"
        @change-page="handleChangePage"
      />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;
  justify-content: space-between;
  .history-container {
    display: flex;
    flex-direction: column;
    width: 90vw;
    height: 100%;
  }

  .history-content {
    display: flex;
    justify-content: center;
    margin-left: 35px;
    width: 100%;
    height: 90vh;
    position: relative;

    .status {
      color: white;
      padding: 10px 10px;
      border-radius: 15px;
      &.canceled {
        background-color: @canceled-appointment;
      }
      &.scheduled {
        background-color: @scheduled-appointment;
      }
      &.requested {
        background-color: @requested-appointment;
      }
      &.completed {
        background-color: @completed-appointment;
      }
    }

    .no-app-yet {
      display: flex;
      align-items: center;

      font-size: 46px;
      font-family: Impact, Haettenschweiler, "Arial Narrow Bold", sans-serif;
    }

    table {
      width: 100%;
      border-collapse: separate;
      border-spacing: 0;
      color: @gray;
      position: relative;

      tbody {
        margin-top: 20px;
      }
    }
  }
}
</style>
@/data/types/SidebarOptions
