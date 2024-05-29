<script setup lang="ts">
import CustomerCalendarHeader from "./CustomerCalendarHeader.vue";
</script>

<template>
  <div class="container">
    <CustomerCalendarHeader />
    <span>something</span>
  </div>
</template>

<style lang="less" scoped>
@import (reference) "@/assets/styles.less";
.container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  background-color: @green;
}
</style>
