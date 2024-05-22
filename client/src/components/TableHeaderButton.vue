<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    label: string;
    active?: boolean;
  }>(),
  {
    active: false,
  }
);
</script>

<template>
  <button :class="{ selected: active }">{{ label }}</button>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

button {
  background-color: transparent;
  box-sizing: border-box;
  /*adding border created the flickering upwards of the text. solved it here.*/
  border-top: 5px solid transparent;
  border-left: none;
  border-right: none;
  border-bottom: 5px solid transparent;

  color: @white;
  font-size: 20px;
  font-weight: 100;
  cursor: pointer;
  height: 5vh;
  padding: 15px 40px;
  display: flex;
  align-items: center;
  &.selected {
    border-bottom-color: @red;
  }
}
</style>
