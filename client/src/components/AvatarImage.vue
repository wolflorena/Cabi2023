<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    src?: string;
    alt?: string;
  }>(),
  {
    src: "",
  }
);
</script>
<template>
  <div class="avatar">
    <img :src="src" :alt="alt" />
  </div>
</template>

<style scoped lang="less">
.avatar {
  width: 180px;
  height: 180px;
  border-radius: 50%;
  margin-bottom: 15px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid #ccc;
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}
</style>
