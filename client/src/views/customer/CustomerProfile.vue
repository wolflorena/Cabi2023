<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import { CustomerSidebarOptions } from "@/data/types/SidebarOptions";

const handleContentChanged = () => {};
</script>

<template>
  <div class="container">
    <Sidebar
      :options="CustomerSidebarOptions"
      @content-changed="handleContentChanged"
    />
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;
}
</style>
@/data/types/SidebarOptions
