<script setup lang="ts">
import AvatarImage from "@/components/AvatarImage.vue";
</script>

<template>
  <div class="view-profile-container">
    <div class="avatar-container">
      <AvatarImage src="../../../assets/default-avatar.png" alt="" />
    </div>
    <div class="inputs">Input1</div>
    <div class="inputs">Input2</div>
    <div class="inputs">Input3</div>
    <div class="inputs">Input4</div>
  </div>
</template>

<style lang="less" scoped>
.view-profile-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: auto;
  .avatar-container {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 25vh;
    width: 100%;
  }
}
</style>
