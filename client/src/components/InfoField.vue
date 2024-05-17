<script setup lang="ts">
withDefaults(
  defineProps<{
    uuid: string;
    label: string;
    inputValue?: string;
    isReadonly?: boolean;
    type?: string;
  }>(),
  {
    isReadonly: true,
    type: "text",
  }
);
</script>

<template>
  <div class="info-field">
    <label :for="uuid">{{ label }}</label>
    <input :type="type" :id="uuid" :value="inputValue" :readonly="isReadonly" />
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

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
    height: 4vh;
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
</style>
