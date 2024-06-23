<script setup lang="ts">
import ActionButton from "@/components/ActionButton.vue";
import LoadingSpinner from "@/components/LoadingSpinner.vue";
import Pagination from "@/components/Pagination.vue";
import Sidebar from "@/components/Sidebar.vue";
import TableHeader from "@/components/TableHeader.vue";
import TableRow from "@/components/TableRow.vue";
import { PatientAdmin } from "@/data/types/Entities";
import { DoctorSidebarOptions } from "@/data/types/SidebarOptions";
import { getUserIdAndToken } from "@/services/authentication_service";
import { getAllPatientsForDoctor } from "@/services/customer_service";
import { onMounted, ref } from "vue";

const isLoading = ref(false);

const currentPage = ref(1);
const totalPages = ref(0);

const patients = ref<PatientAdmin[]>([]);
const loggedDoctorId = ref<number>(-1);

async function loadPatients() {
  isLoading.value = true;
  await getAllPatientsForDoctor(
    10,
    currentPage.value - 1,
    loggedDoctorId.value
  ).then((res: any) => {
    patients.value = res.pagedCustomers.content;
    totalPages.value = Math.ceil(res.total / 10);
    isLoading.value = false;
  });
}

async function changePage(pageNumber: number) {
  isLoading.value = true;
  currentPage.value = pageNumber;
  await getAllPatientsForDoctor(
    10,
    currentPage.value - 1,
    loggedDoctorId.value
  ).then((res: any) => {
    patients.value = res.pagedCustomers.content;
    totalPages.value = Math.ceil(res.total / 10);
    isLoading.value = false;
  });
}

onMounted(() => {
  const { userId, token } = getUserIdAndToken();
  loggedDoctorId.value = userId;
  loadPatients();
});
</script>

<template>
  <div class="container">
    <Sidebar :options="DoctorSidebarOptions" />

    <div class="patients">
      <div class="patients-container">
        <table v-if="patients.length > 0">
          <TableHeader
            :columns="['Patient', 'Email', 'Phone Number', 'Actions']"
          />

          <tbody>
            <TableRow
              v-for="(patient, index) in patients"
              :columns="[
                10 * (currentPage - 1) + index + 1,
                patient.firstName + ' ' + patient.lastName,
                patient.email,
                patient.phoneNo,
              ]"
              :index="index"
            >
              <div class="actions">
                <router-link :to="'patients/' + patient.customerId">
                  <ActionButton icon-token="eye" />
                </router-link>
              </div>
            </TableRow>
          </tbody>
        </table>

        <img
          src="../../assets/nodata.svg"
          alt=""
          v-else-if="patients.length === 0 && isLoading === false"
        />

        <LoadingSpinner v-else />

        <Pagination
          :total-pages="totalPages"
          :current-page="currentPage"
          @change-page="changePage"
        />
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .patients {
    width: 83vw;
    .patients-container {
      width: 100%;
      height: 100vh;
      position: relative;

      table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0;
        color: @gray;

        tbody {
          margin-top: 20px;
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
  }
}
</style>
