<script setup lang="ts"></script>

<template>
  <button class="add-button">
    <font-awesome-icon id="icon" icon="circle-plus" />
    <span>Add appointment</span>
  </button>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.add-button {
  background-color: @green;
  border: none;
  border-radius: 20px;
  width: 90%;
  height: 40px;
  cursor: pointer;
  margin-bottom: 20px;

  display: flex;
  align-items: center;
  gap: 20px;

  #icon {
    height: 25px;
    color: @gray;
  }

  span {
    font-size: 15px;
    color: @gray;
  }
}
</style>
