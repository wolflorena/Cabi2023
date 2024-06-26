<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import { CustomerSidebarOptions } from "@/data/types/SidebarOptions";
import { onMounted, ref } from "vue";
import TableHeader from "@/components/TableHeader.vue";
import TableRow from "@/components/TableRow.vue";
import { FormWithStatus } from "@/data/types/Entities";
import {
  getVisibleForms,
  setSignedForm,
  setViewedForm,
} from "@/services/form_service";
import { getUserIdAndToken } from "@/services/authentication_service";
import LoadingSpinner from "@/components/LoadingSpinner.vue";
import FormDescription from "./form-content/FormDescription.vue";
import Swal from "sweetalert2";
import { mountComponent } from "./form-content/MountComponent";
import { useUserProfile } from "@/store/useUserProfile";

const { forms, getVisibleFormsStore } = useUserProfile();

async function handleOpenForm(form: FormWithStatus) {
  const formDescriptionElement = mountComponent(FormDescription, {
    description: form.description,
  });

  try {
    const { userId, token } = getUserIdAndToken();

    if (form.formEventType === "NEW") {
      await setViewedForm(form.formId, userId).then((res) => {
        form.formEventType = "VIEWED";
      });
    }

    Swal.fire({
      title: form.title,
      html: formDescriptionElement,
      input: "checkbox",
      inputPlaceholder: "I agree",
      confirmButtonText: "Confirm",
      showCancelButton: true,
      inputValidator: (result) => {
        return result ? null : "You need to agree to proceed!";
      },
      preConfirm: async () => {
        try {
          await setSignedForm(form.formId, userId);
          form.formEventType = "SIGNED";
        } catch (error) {
          console.error("Error signing form", error);
        }
      },
    });
  } catch (error: any) {
    console.error("Error setting form as viewed", error.message);
  }
}

onMounted(async () => {
  console.log("forms view");

  await getVisibleFormsStore();
});
</script>

<template>
  <div class="container">
    <Sidebar :options="CustomerSidebarOptions" />
    <div class="forms-container">
      <div class="forms-content">
        <table v-if="forms.length > 0">
          <TableHeader
            :columns="['Title', 'Status', 'Expand']"
            :has-empty-row="false"
          />
          <tbody>
            <TableRow
              v-for="(form, index) in forms"
              :columns="[index + 1, form.title]"
              :status-column="form.formEventType"
              :index="index"
              highlight="odd"
              :style="{ cursor: 'pointer' }"
              @click="handleOpenForm(form)"
            >
              <div :style="{ fontSize: '32px' }">
                <font-awesome-icon id="icon" icon="caret-down" />
              </div>
            </TableRow>
          </tbody>
        </table>
        <div v-else>
          <table>
            <TableHeader
              :columns="['Service', 'Title', 'Status', 'Expand']"
              :has-empty-row="false"
            />
            <tbody>
              <div class="no-form-container">
                <LoadingSpinner />
              </div>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;
  justify-content: space-between;
  .forms-container {
    display: flex;
    flex-direction: column;
    width: 90vw;
    height: 100%;
  }

  .forms-content {
    display: flex;
    justify-content: center;
    margin-left: 35px;
    width: 100%;
    height: 100%;
    position: relative;

    .no-form-container {
      width: 75vw;
      position: relative;
      height: 100vh;
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
@/data/types/SidebarOptions./form-content/FormDescription.vue
