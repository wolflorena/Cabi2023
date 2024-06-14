<script setup lang="ts">
import { MdEditor } from "md-editor-v3";
import "md-editor-v3/lib/style.css";
import { onMounted, ref } from "vue";

const props = defineProps<{
  description: string;
}>();
const editorRef = ref();

const emit = defineEmits(["onCheckValue"]);
const checkValue = ref<boolean>(false);

function handleCheckboxValue() {
  console.log("Checkbox value before emitting:", checkValue.value);
  emit("onCheckValue", checkValue.value);
}
onMounted(() => {
  editorRef.value?.togglePreviewOnly(true);
});
</script>

<template>
  <div>
    <MdEditor
      ref="editorRef"
      v-model="props.description"
      preview-only
      :toolbars="[]"
      :footers="[]"
      language="en-US"
    />
  </div>
</template>

<style scoped>
.md-editor-input-wrapper .md-editor-resize-operate {
  display: none !important;
}
</style>
