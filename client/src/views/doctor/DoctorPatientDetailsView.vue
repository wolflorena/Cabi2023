<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import DragAndDropFile from "@/components/DragAndDropFile.vue";
import { DoctorSidebarOptions } from "@/data/types/SidebarOptions";
import { useRoute } from "vue-router";
import { ref } from "vue";
import router from "@/router";

const route = useRoute();
const patientId = ref(route.params.id);

function goBack() {
  router.back();
}
</script>

<template>
  <div class="container">
    <Sidebar :options="DoctorSidebarOptions" />

    <div class="header">
      <span>
        <button @click="goBack">
          <font-awesome-icon icon="chevron-left" id="icon" />
        </button>

        Patients View</span
      >
      <span>View Patient History</span>
    </div>

    <div class="drag-and-drop-container">
      <DragAndDropFile :patientId="+patientId" />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  height: 100vh;
  display: flex;

  .header {
    position: absolute;
    top: 0;
    right: 0;
    background-color: @gray;
    height: 7vh;
    width: 50vw;
    border-bottom-left-radius: 40px;

    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 30px;

    span {
      color: @white;
      font-size: large;

      button {
        background-color: transparent;
        border: none;
        color: @white;
      }
    }
  }

  .drag-and-drop-container {
    width: 85vw;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
</style>
