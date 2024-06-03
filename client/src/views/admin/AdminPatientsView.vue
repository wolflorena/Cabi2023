<script setup lang="ts">
import { ref, onMounted } from "vue";

import Sidebar from "@/components/Sidebar.vue";
import Pagination from "@/components/Pagination.vue";
import { AdminSidebarOptions } from "@/data/types/SidebarOptions";
import {
  editStatus,
  getAllPageable,
  getById,
} from "@/services/customer_service";
import { PatientAdmin } from "@/data/types/Entities";
import { useRouter } from "vue-router";
import CustomModal from "@/components/CustomModal.vue";
import ActionButton from "@/components/ActionButton.vue";
import TableHeader from "@/components/TableHeader.vue";
import TableRow from "@/components/TableRow.vue";
import LoadingSpinner from "@/components/LoadingSpinner.vue";

const showDelete = ref(false);
const isLoading = ref(false);

const currentPage = ref(1);
const totalPages = ref(0);

const patients = ref<PatientAdmin[]>([]);
const patientDetails = ref<PatientAdmin>();

const router = useRouter();

async function loadPatients() {
  isLoading.value = true;
  await getAllPageable(10, currentPage.value - 1).then((res: any) => {
    patients.value = res.pagedCustomers.content;
    totalPages.value = Math.ceil(res.total / 10);
    isLoading.value = false;
  });
}

async function changePage(pageNumber: number) {
  isLoading.value = true;
  currentPage.value = pageNumber;
  await getAllPageable(10, currentPage.value - 1).then((res: any) => {
    patients.value = res.pagedCustomers.content;
    totalPages.value = Math.ceil(res.total / 10);
    isLoading.value = false;
  });
}

async function showDeleteModal(patientId: number) {
  showDelete.value = true;
  await getById(patientId).then((res) => {
    patientDetails.value = res;
  });
}

async function deletePatient(patientId: number | undefined) {
  if (patientId) {
    await editStatus(patientId, "SUSPENDED").then((res) => {
      console.log("Account successfull deleted");
      showDelete.value = false;
      loadPatients();
    });
  }
}

onMounted(() => {
  loadPatients();
});
</script>

<template>
  <div class="container">
    <Sidebar :options="AdminSidebarOptions" />

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

                <router-link :to="'patients/edit/' + patient.customerId">
                  <ActionButton icon-token="pen" />
                </router-link>

                <ActionButton
                  icon-token="trash-can"
                  @action-triggered="showDeleteModal(patient.customerId)"
                />
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
      <CustomModal
        :show="showDelete"
        button1-text="Delete"
        @button2="showDelete = false"
        @button1="deletePatient(patientDetails?.customerId)"
      >
        <span class="delete-text">{{
          "Are you sure you want to delete the account for " +
          patientDetails?.firstName +
          " " +
          patientDetails?.lastName +
          "?"
        }}</span></CustomModal
      >
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

    .delete-text {
      color: @white;
    }
  }
}
</style>
