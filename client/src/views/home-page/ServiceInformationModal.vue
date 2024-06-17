<script setup lang="ts">
import { Service } from "@/data/types/Entities";

const props = defineProps<{
  service: Service;
}>();
</script>

<template>
  <div class="service-info-container">
    <div class="info-row">
      <span class="label">Name:</span>
      <span class="value">{{ service.name }}</span>
    </div>
    <div class="info-row">
      <span class="label">Duration:</span>
      <span class="value">{{ service.duration }} minutes</span>
    </div>
    <div class="info-row">
      <span class="label">Get an appointment</span>
      <span class="value">for only {{ service.price }} RON</span>
    </div>
  </div>
</template>

<style lang="less" scoped>
@import (reference) "@/assets/styles.less";

.service-info-container {
  background-color: @heavy-smoke;
  padding: 20px;
  border-radius: 10px;
  color: @white;
  font-size: 22px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);

  .info-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid @light-gray;

    &:last-child {
      border-bottom: none;
    }

    .label {
      flex: 1;
      text-align: right;
      margin-right: 10px;
      font-weight: bold;
    }

    .value {
      flex: 1;
      text-align: left;
      margin-left: 10px;
    }
  }
}
</style>
