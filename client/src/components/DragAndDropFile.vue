<script setup lang="ts">
import { ref, onMounted, watch } from "vue";
import FileCard from "@/components/FileCard.vue";
import { PdfDocument } from "@/data/types/Entities";
import Swal from "sweetalert2";
import { getFilesByPatient, uploadFile } from "@/services/pdf_document_service";
import CustomButton from "./CustomButton.vue";
import LoadingSpinner from "./LoadingSpinner.vue";

const props = withDefaults(
  defineProps<{
    patientId: number;
  }>(),
  {}
);

const fileInput = ref<HTMLInputElement | null>(null);
const files = ref<PdfDocument[]>([]);
const isLoading = ref<boolean>(false);

async function fetchFiles() {
  isLoading.value = true;
  await getFilesByPatient(props.patientId).then((res) => {
    files.value = res;
    isLoading.value = false;
  });
}

onMounted(fetchFiles);
watch(() => props.patientId, fetchFiles);

const dragOver = (event: DragEvent) => {
  const target = event.target as HTMLElement;
  target.classList.add("drag-over");
};

const dragLeave = (event: DragEvent) => {
  const target = event.target as HTMLElement;
  target.classList.remove("drag-over");
};

const handleDrop = async (event: DragEvent) => {
  const target = event.target as HTMLElement;
  target.classList.remove("drag-over");
  const droppedFiles = event.dataTransfer?.files;
  if (droppedFiles && droppedFiles.length) {
    await upload(droppedFiles[0]);
    fetchFiles();
  }
};

const handleFileSelect = async (event: Event) => {
  const target = event.target as HTMLInputElement;
  const selectedFiles = target.files;
  if (selectedFiles && selectedFiles.length) {
    await upload(selectedFiles[0]);
    fetchFiles();
  }
};

const triggerFileInput = () => {
  fileInput.value?.click();
};

async function upload(file: File) {
  try {
    const response = await uploadFile(props.patientId, file);
    if (response && response.ok) {
      Swal.fire({
        titleText: "File successfully uploaded.",
        icon: "success",
      });
    } else {
      Swal.fire({
        titleText: "Failed to upload file.",
        icon: "error",
      });
    }
  } catch (error: any) {
    Swal.fire({
      titleText: error.message,
      icon: "error",
    });
  }
}
</script>

<template>
  <div class="container">
    <div
      class="drag-drop-area"
      @dragover.prevent
      @dragenter.prevent
      @drop.prevent="handleDrop"
      @dragover="dragOver"
      @dragleave="dragLeave"
    >
      <p>Drag & Drop files here</p>
    </div>
    <input
      type="file"
      @change="handleFileSelect"
      ref="fileInput"
      style="display: none"
    />

    <CustomButton text="Attach File" @actionTriggered="triggerFileInput" />

    <div class="list-container" style="position: relative; height: 25vh">
      <div v-if="!isLoading" class="file-list">
        <FileCard
          v-for="file in files"
          :key="file.fileId"
          :fileName="file.fileName"
          :fileId="file.fileId"
        />
      </div>
      <LoadingSpinner v-else />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  .drag-drop-area {
    width: 25vw;
    height: 25vh;
    border: 2px dashed @gray;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 10px;
  }

  .drag-over {
    border-color: @green;
  }

  button {
    display: inline-block;
    padding: 10px 20px;
    cursor: pointer;
  }

  .file-list {
    width: 25vw;
    margin-top: 20px;
    height: 40vh;
    overflow-y: auto;

    &::-webkit-scrollbar {
      width: 5px;
    }

    &::-webkit-scrollbar-thumb {
      background: #424d65;
      border-radius: 5px;
    }

    &::-webkit-scrollbar-track {
      background-color: transparent;
    }

    scrollbar-width: thin;
    scrollbar-color: #424d65 transparent;
  }
}
</style>
