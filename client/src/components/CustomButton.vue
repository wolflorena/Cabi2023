<script setup lang="ts">
import { StyleValue } from "vue";

const props = withDefaults(
  defineProps<{
    text?: string;
    isMain?: boolean;
    height?: StyleValue;
    width?: StyleValue;
  }>(),
  {
    text: "",
    isMain: true,
  }
);
</script>

<template>
  <button
    :style="{
      backgroundColor: isMain ? '#ef4b4c' : '#43506c',
      height: height ? height + 'px' : '',
      width: width ? width + 'px' : '',
    }"
    :class="isMain"
  >
    {{ text }}
  </button>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

button {
  cursor: pointer;
  border: none;
  border-radius: 15px;
  font-size: 15px;
  color: @sugar;
}
</style>
