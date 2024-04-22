<script setup lang="ts">
import { ref } from "vue";
const props = withDefaults(
  defineProps<{
    label: string;
    active: boolean;
  }>(),
  {}
);
</script>

<template>
  <button :class="{ selected: active }">{{ label }}</button>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

button {
  background-color: transparent;
  border: none;
  color: @white;
  font-size: 20px;
  font-weight: 100;
  cursor: pointer;
  height: 5vh;
  padding: 20px;
  &.selected {
    border-bottom: 5px solid @red;
  }
}
</style>
