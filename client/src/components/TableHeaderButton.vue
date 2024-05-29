<script setup lang="ts">
import { computed } from "vue";

const props = withDefaults(
  defineProps<{
    label: string;
    active?: boolean;
    variant?: string;
  }>(),
  {
    variant: "DEFAULT",
    active: false,
  }
);

const variantClass = computed(() => ({
  "default-button": props.variant === "DEFAULT",
  "customer-calendar-button": props.variant === "CUSTOMER-CALENDAR",
  selected: props.active,
}));
</script>

<template>
  <button :class="variantClass">{{ label }}</button>
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

  font-size: 20px;
  font-weight: 500;
  cursor: pointer;
  height: 100%;
  padding: 15px 40px;
  display: flex;
  align-items: center;
  &.selected {
    border-bottom-color: @red;
  }
}
.default-button {
  color: @white;
}

.customer-calendar-button {
  color: @smoke;
}
</style>
