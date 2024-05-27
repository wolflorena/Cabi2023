<script setup lang="ts">
const props = defineProps({
  message: String,
});
</script>

<template>
  <span>{{ message }}</span>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

span {
  color: @red;
}
</style>
