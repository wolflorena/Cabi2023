<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import { Form } from "@/data/types/Entities";
import { AdminSidebarOptions } from "@/data/types/SidebarOptions";
import { onMounted, ref } from "vue";
import { deleteForm, getAllForms, getForm } from "@/services/form_service";
import CustomModal from "@/components/CustomModal.vue";
import LoadingSpinner from "@/components/LoadingSpinner.vue";
import ActionButton from "@/components/ActionButton.vue";
import TableHeader from "@/components/TableHeader.vue";
import TableRow from "@/components/TableRow.vue";
import Swal from "sweetalert2";

const forms = ref<Form[]>();
const showDelete = ref(false);
const formDetails = ref<Form>();
const isLoading = ref(false);

async function loadForms() {
  isLoading.value = true;
  await getAllForms().then((res) => {
    forms.value = res;
    isLoading.value = false;
  });
}

onMounted(() => {
  loadForms();
});

async function showDeleteModal(formId: number) {
  showDelete.value = true;
  await getForm(formId).then((res) => {
    formDetails.value = res;
  });
}

async function deleteFormById(formId: number | undefined) {
  isLoading.value = true;
  if (formId) {
    await deleteForm(formId)
      .then((res) => {
        if (res.ok) {
          showDelete.value = false;
          loadForms();
          isLoading.value = false;
          Swal.fire({
            titleText: "Form has been successfully deleted!",
            icon: "success",
          });
        }
      })
      .catch((error) => {
        console.log(error);
      });
  } else return;
}
</script>

<template>
  <div class="container">
    <Sidebar :options="AdminSidebarOptions" />

    <div class="forms">
      <div class="forms-container">
        <table v-if="forms && forms?.length > 0">
          <TableHeader :columns="['Title', 'Last updated', 'Actions']" />
          <tbody>
            <TableRow
              v-for="(form, index) in forms"
              :columns="[index + 1, form.title]"
              :date="form.editedDate"
              :time="form.editedTime"
              :index="index"
            >
              <div class="actions">
                <router-link :to="'forms/' + form.formId">
                  <ActionButton iconToken="eye" />
                </router-link>

                <router-link :to="'forms/edit/' + form.formId">
                  <ActionButton iconToken="pen" />
                </router-link>

                <ActionButton
                  iconToken="trash-can"
                  @action-triggered="showDeleteModal(form.formId)"
                />
              </div>
            </TableRow>
          </tbody>
        </table>

        <img
          src="../../assets/nodata.svg"
          alt=""
          v-else-if="forms && forms.length === 0 && isLoading === false"
        />

        <LoadingSpinner v-else />

        <div class="add-button-container">
          <router-link to="forms/create">
            <button id="add">
              <font-awesome-icon icon="circle-plus" id="icon" />
              Create new form
            </button>
          </router-link>
        </div>
      </div>
    </div>
    <CustomModal
      :show="showDelete"
      button1-text="Delete"
      @button2="showDelete = false"
      @button1="deleteFormById(formDetails?.formId)"
    >
      <span class="delete-text">{{
        "Are you sure you want to delete the " + formDetails?.title + " form?"
      }}</span></CustomModal
    >
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .forms {
    width: 83vw;
    .forms-container {
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

      img {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 70%;
      }
    }

    .add-button-container {
      display: flex;
      justify-content: center;
      margin-top: 100px;

      #add {
        cursor: pointer;

        background-color: transparent;
        border: none;
        color: @gray;

        font-size: 20px;
      }
    }
  }

  .delete-text {
    color: @white;
  }
}
</style>
