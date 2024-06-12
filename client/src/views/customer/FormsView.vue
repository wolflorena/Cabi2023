<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import { CustomerSidebarOptions } from "@/data/types/SidebarOptions";
import { onMounted, ref } from "vue";
import TableHeader from "@/components/TableHeader.vue";
import TableRow from "@/components/TableRow.vue";
import { Form, FormWithStatus } from "@/data/types/Entities";
import { getVisibleForms } from "@/services/form_service";
import { getUserIdAndToken } from "@/services/authentication_service";
import LoadingSpinner from "@/components/LoadingSpinner.vue";

const forms = ref<FormWithStatus[]>([]);

onMounted(async () => {
  const { userId, token } = getUserIdAndToken();
  await getVisibleForms(token, userId).then((res) => {
    if (res.length > 0) {
      forms.value = res;
    }
  });
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
              :columns="[index + 1, form.title, form.formEventType, `expand`]"
              :index="index"
              highlight="odd"
            >
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
@/data/types/SidebarOptions
