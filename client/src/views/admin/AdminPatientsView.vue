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
import ActionTableButton from "@/components/ActionTableButton.vue";

const showDelete = ref(false);

const currentPage = ref(1);
const totalPages = ref(0);

const patients = ref<PatientAdmin[]>([]);
const patientDetails = ref<PatientAdmin>();

const router = useRouter();

async function loadPatients() {
  await getAllPageable(10, currentPage.value - 1).then((res: any) => {
    patients.value = res.pagedCustomers.content;
    totalPages.value = Math.ceil(res.total / 10);
  });
}

async function changePage(pageNumber: number) {
  currentPage.value = pageNumber;
  await getAllPageable(10, currentPage.value - 1).then((res: any) => {
    patients.value = res.pagedCustomers.content;
    totalPages.value = Math.ceil(res.total / 10);
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
        <table>
          <thead>
            <tr>
              <th>No.#</th>
              <th>Patient</th>
              <th>Email</th>
              <th>Phone Number</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tr style="height: 20px">
            <td colspan="5"></td>
          </tr>
          <tbody>
            <tr v-for="(patient, index) in patients">
              <td>{{ 10 * (currentPage - 1) + index + 1 }}</td>
              <td>
                {{ patient.firstName + " " + patient.lastName }}
              </td>
              <td>
                {{ patient.email }}
              </td>
              <td>
                {{ patient.phoneNo }}
              </td>
              <td>
                <div class="actions">
                  <router-link :to="'patients/' + patient.customerId">
                    <ActionTableButton icon-token="eye" />
                  </router-link>

                  <router-link :to="'patients/edit/' + patient.customerId">
                    <ActionTableButton icon-token="pen" />
                  </router-link>

                  <ActionTableButton
                    icon-token="trash-can"
                    @action-triggered="showDeleteModal(patient.customerId)"
                  />
                </div>
              </td>
            </tr>
          </tbody>
        </table>

        <Pagination
          :total-pages="totalPages"
          :current-page="currentPage"
          @change-page="changePage"
        />
      </div>
      <CustomModal
        :show="showDelete"
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

        thead tr {
          height: 8vh;
          background-color: @gray;
          color: @white;
          font-size: large;

          th:first-child {
            border-bottom-left-radius: 40px;
          }
        }

        tbody {
          margin-top: 20px;
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

            &:first-child {
              border-top-left-radius: 20px;
              border-bottom-left-radius: 20px;
            }
          }
          &:hover {
            background-color: @sugar;
          }
          &:nth-child(odd) {
            background-color: @light-gray;
          }
        }
      }
    }
    .delete-text {
      color: @white;
    }
  }
}
</style>
