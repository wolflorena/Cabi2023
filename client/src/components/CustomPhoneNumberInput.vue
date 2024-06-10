<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    labelText: string;
    type: string;
    iconToken?: string;
    modelValue?: string;
  }>(),
  {
    labelText: "",
    type: "text",
  }
);

const emit = defineEmits(["update:modelValue"]);

const inputChange = (event: any) => {
  emit("update:modelValue", event.target.value);
};
</script>

<template>
  <div class="input-container">
    <div class="input-box">
      <font-awesome-icon :icon="iconToken" id="icon" />
      <input
        maxlength="12"
        :type="type"
        :key="type"
        required
        class="input-input"
        placeholder=""
        :value="modelValue"
        @input="inputChange"
      />
      <label for="" class="input-label">{{ labelText }}</label>
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.input-container {
  display: grid;
  row-gap: 1.75rem;

  .input-box {
    display: grid;
    grid-template-columns: max-content 1fr;
    align-items: center;
    column-gap: 0.75rem;
    position: relative;
    border-bottom: 2px solid @gray;

    .input-input {
      border: none;
      outline: none;
      width: 100%;
      font-size: 1rem;
      padding-block: 0.75rem;
      background: none;
      color: @gray;
    }

    .input-label {
      position: absolute;
      padding-left: 30px;
      left: 0;
      top: 13px;
      font-weight: 500;
      transition: top 0.3s, font-size 0.3s;
    }

    .input-input:focus + .input-label,
    .input-input:not(:placeholder-shown) + .input-label {
      top: -12px;
      font-size: 0.95rem;
    }
  }
}
</style>
