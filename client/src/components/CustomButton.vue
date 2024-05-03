<script setup lang="ts">
import { StyleValue } from "vue";

const props = withDefaults(
  defineProps<{
    text?: string;
    isMain?: boolean;
    height?: StyleValue;
    width?: StyleValue;
    fontSize?: StyleValue;
    color?: string;
  }>(),
  {
    text: "",
    isMain: true,
  }
);

const emits = defineEmits<{
  (e: "actionTriggered"): void;
}>();

const handleClickEvent = () => {
  emits("actionTriggered");
};
</script>

<template>
  <button
    :style="{
      backgroundColor: isMain ? '#ef4b4c' : color ? color : '#43506c',
      height: height ? height + 'px' : '',
      width: width ? width + 'px' : '',
      fontSize: fontSize ? fontSize + 'px' : '',
    }"
    :class="isMain"
    @click="handleClickEvent"
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
