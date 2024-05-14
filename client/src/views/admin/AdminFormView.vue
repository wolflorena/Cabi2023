<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import { AdminSidebarOptions } from "@/data/types/SidebarOptions";
import router from "@/router";
import { useRoute } from "vue-router";
import { MdEditor } from "md-editor-v3";
import "md-editor-v3/lib/style.css";
import { onMounted, ref } from "vue";
import CustomButton from "@/components/CustomButton.vue";
import ToggleButton from "@/components/ToggleButton.vue";
import { addForm, getForm } from "@/services/form_service";
import { Form } from "@/data/types/Entities";

const route = useRoute();
const title = ref("");
const description = ref("");
const visibility = ref(false);

const formId = ref(route.params.id);
const form = ref<Form>();

function goBack() {
  router.back();
}

async function createForm() {
  await addForm(title.value, description.value, visibility.value);
  alert("Form submitted successfully");
}

async function getFormDetails(formId: number) {
  await getForm(formId).then((res) => {
    form.value = res;
    title.value = res.title;
    description.value = res.description;
    visibility.value = res.visibility;
  });
}

onMounted(() => {
  if (formId) getFormDetails(+formId.value);
});

async function submitChanges() {
  if (!formId) {
    createForm();
  }
  //TODO: @wolflorena add PATCH method
}
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
      <span>{{ formId ? "Edit form" : "Create new form" }}</span>
    </div>

    <div class="details">
      <div class="form-info">
        <div class="info-field">
          <label for="title">Title</label>
          <input type="text" id="title" v-model="title" />
        </div>
      </div>

      <div class="rich-text">
        <MdEditor
          v-model="description"
          language="en-US"
          :toolbars-exclude="['github', 'save']"
        />
      </div>

      <ToggleButton
        v-model="visibility"
        text="Visibility for patients:"
        uuid="form-toggle"
      />
      <CustomButton
        text="Submit"
        :is-main="false"
        @click="submitChanges"
        id="submitButton"
        height="30"
        width="150"
        font-size="20"
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

    .form-info {
      .info-field {
        display: flex;
        flex-direction: column;

        label {
          color: @blue;
          margin-bottom: 5px;
          font-weight: bold;
          font-size: larger;
        }

        input {
          padding: 8px;
          height: 2vh;
          width: 15vw;
          border: 0.5px solid @gray;
          border-radius: 4px;
          background-color: transparent;
          color: @black;
          font-size: 18px;
        }

        input[readonly] {
          cursor: not-allowed;
          opacity: 0.8;
        }
      }
    }

    #submitButton {
      position: absolute;
      bottom: 10px;
      right: 10px;
    }
  }
}
</style>