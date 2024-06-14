<script setup lang="ts">
import ActionButton from "./ActionButton.vue";

const props = withDefaults(
  defineProps<{
    fileId: number;
    fileName: string;
  }>(),
  {
    fileName: "",
  }
);

function downloadFile() {
  window.open(
    `http://localhost:9090/api/documents/download/${props.fileId}`,
    "_blank"
  );
}

function viewFile() {
  window.open(
    `http://localhost:9090/api/documents/view/${props.fileId}`,
    "_blank"
  );
}
</script>

<template>
  <div class="file-card">
    <div class="title">
      <div class="file-icon">
        <font-awesome-icon icon="file-pdf" id="primary" class="icon" />
      </div>
      <div class="file-details">
        <h3>{{ fileName }}</h3>
      </div>
    </div>

    <div class="buttons">
      <ActionButton icon-token="download" @action-triggered="downloadFile" />
      <ActionButton icon-token="eye" @action-triggered="viewFile" />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.file-card {
  display: flex;
  align-items: center;
  justify-content: space-between;

  background-color: @light-gray;
  border-radius: 15px;
  padding: 10px;
  margin-bottom: 10px;

  .title {
    display: flex;
    align-items: center;

    .file-icon {
      font-size: 24px;
      margin-right: 10px;
    }

    .file-details {
      h3 {
        font-size: 15px;
        font-weight: 500;
      }
    }
  }

  .buttons {
    display: flex;
    align-items: center;
  }

  &:hover {
    background-color: @dark-gray;
  }
}
</style>
