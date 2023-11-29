<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    labelText: string;
    type: string;
    pattern?: string;
  }>(),
  {
    labelText: "",
    type: "text",
  }
);
</script>

<template>
  <label>
    {{ labelText }}
    <input :type="type" />
  </label>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

label {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  gap: 5px;

  color: @black;
  font-size: 15px;
  font-weight: 100;
  width: 30%;
  min-width: 400px;

  input {
    width: 200px;
    height: 20px;
    border-radius: 15px;
    padding: 5px;
    border: 1px solid rgba(0, 0, 0, 0.4);
    background-color: transparent;
  }
}
</style>
