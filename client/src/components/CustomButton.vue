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
    disabled?: boolean;
  }>(),
  {
    text: "",
    isMain: true,
    disabled: false,
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
      height: height ? height + 'px' : 'auto',
      width: width ? width + 'px' : 'auto',
      fontSize: fontSize ? fontSize + 'px' : 'inherit',
      opacity: disabled ? '0.6' : '1',
      cursor: disabled ? 'not-allowed' : 'pointer',
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
  border-radius: 12px;
  font-size: 15px;
  font-weight: bold;
  color: @sugar;
}
</style>
