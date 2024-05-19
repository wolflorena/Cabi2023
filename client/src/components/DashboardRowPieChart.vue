<script setup lang="ts">
import { TreatmentType } from "@/data/types/Entities";

const props = withDefaults(
  defineProps<{
    serviceName?: string;
    color?: string;
  }>(),
  {}
);
</script>

<template>
  <div class="row">
    <div id="color" :style="{ backgroundColor: color }"></div>
    <span class="text">{{ serviceName }}</span>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.row {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 5px 0;

  #color {
    width: 20px;
    height: 20px;
  }

  .text {
    color: @gray;
    font-size: 15px;
    font-weight: 100;
  }
}
</style>
