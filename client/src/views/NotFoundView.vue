<script setup lang="ts"></script>

<template>
  <div class="container">
    <img src="../assets/404Error.svg" alt="" />
    <h1>The page was not found</h1>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  height: 100vh;
  width: 100vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  h1 {
    color: @gray;
  }
}
</style>
