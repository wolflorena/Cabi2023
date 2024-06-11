<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import { CustomerSidebarOptions } from "@/data/types/SidebarOptions";
import { computed, onMounted, ref } from "vue";

import TableHeader from "@/components/TableHeader.vue";
import TableRow from "@/components/TableRow.vue";
import { AppointmentCalendar } from "@/data/types/Entities";
import { useUserAppointments } from "@/store/useUserAppointments";

const { userHistoryAppointments, fetchUserHistoryAppointments } =
  useUserAppointments();

const currentPage = ref<number>(1);
const pageNumber = ref<number>(1);
onMounted(async () => {
  await fetchUserHistoryAppointments(pageNumber.value);
});
</script>

<template>
  <div class="container">
    <Sidebar :options="CustomerSidebarOptions" />
    <div class="history-container">
      <div class="history-content">
        <table v-if="userHistoryAppointments.length > 0">
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
                appointment.doctorId,
              ]"
              :date="appointment.date"
              :time="appointment.time"
              :index="index"
              highlight="odd"
            >
              <span
                class="status"
                :class="{ canceled: appointment.status === 'CANCELLED' }"
              >
                {{ appointment.status }}
              </span>
            </TableRow>
          </tbody>
        </table>
      </div>
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
    height: 100vh;
  }

  .history-content {
    height: 100%;
    display: flex;
    justify-content: center;
    width: 100%;
    height: 100vh;
    position: relative;

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
