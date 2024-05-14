<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import { AdminSidebarOptions } from "@/data/types/SidebarOptions";
import router from "@/router";
import { useRoute } from "vue-router";
import { onMounted, ref } from "vue";
import { getFormEvents } from "@/services/form_service";
import { FormEvent } from "@/data/types/Entities";
import FormDetails from "@/components/FormDetails.vue";

const route = useRoute();
const signedForms = ref<FormEvent[]>();
const viewedForms = ref<FormEvent[]>();
const totalViewed = ref(0);
const totalSigned = ref(0);

const formId = ref(route.params.id);

function goBack() {
  router.back();
}

async function getFormDetails(formId: number) {
  await getFormEvents(formId).then((res) => {
    signedForms.value = res.signed;
    viewedForms.value = res.viewed;
    totalSigned.value = res.signed.length;
    totalViewed.value = res.viewed.length;
  });
}

onMounted(() => {
  getFormDetails(+formId.value);
});
</script>

<template>
  <div class="container">
    <Sidebar :options="AdminSidebarOptions" />

    <div class="header">
      <span>
        <button @click="goBack">
          <font-awesome-icon icon="chevron-left" id="icon" />
        </button>

        Table view</span
      >
      <span>Form Statistics</span>
    </div>

    <div class="details">
      <FormDetails
        label="Seen by: "
        :total="totalViewed"
        :patients-list="viewedForms"
      />
      <FormDetails
        label="Signed by: "
        :total="totalSigned"
        :patients-list="signedForms"
      />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;
  position: relative;
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

  .details {
    width: 80vw;
    height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    gap: 5vh;
    margin-left: 50px;
  }
}
</style>
