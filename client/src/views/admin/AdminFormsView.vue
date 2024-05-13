<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import { Form } from "@/data/types/Entities";
import { AdminSidebarOptions } from "@/data/types/SidebarOptions";
import { onMounted, ref } from "vue";
import { formatTime, formatDateForTable } from "@/utils/helpers";
import { getAllForms } from "@/services/form_service";

const forms = ref<Form[]>();

async function loadForms() {
  await getAllForms().then((res) => (forms.value = res));
}

onMounted(() => {
  loadForms();
});
</script>

<template>
  <div class="container">
    <Sidebar :options="AdminSidebarOptions" />

    <div class="forms">
      <div class="forms-container">
        <table>
          <thead>
            <tr>
              <th>No.#</th>
              <th>Title</th>
              <th>Last updated</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tr style="height: 20px">
            <td colspan="5"></td>
          </tr>
          <tbody>
            <tr v-for="(form, index) in forms">
              <td>{{ index + 1 }}</td>
              <td>
                {{ form.title }}
              </td>
              <td>
                <div class="date">
                  <span id="time">{{ formatTime(form.editedTime) }} </span>
                  <span id="date">
                    {{ formatDateForTable(form.editedDate) }}
                  </span>
                </div>
              </td>
              <td>
                <div class="actions">
                  <button>
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

            .actions {
              button {
                border: none;
                background-color: transparent;
                cursor: pointer;
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
}
</style>
