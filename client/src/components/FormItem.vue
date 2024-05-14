<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    name?: string;
    date?: string;
  }>(),
  {}
);
</script>

<template>
  <li>
    <span>{{ name }}</span>
    <span>{{ date }}</span>
  </li>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

li {
  display: flex;
  justify-content: space-between;

  font-size: 18px;
  color: @smoke;
  font-weight: bold;
}
</style>
