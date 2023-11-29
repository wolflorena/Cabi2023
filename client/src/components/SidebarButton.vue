<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    text: string;
    selected: boolean;
    icon?: string;
  }>(),
  {
    text: "",
    selected: false,
  }
);
</script>

<template>
  <button :class="{ selected }">
    <font-awesome-icon id="icon" :icon="icon" />
    {{ text }}
  </button>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.selected {
  background-color: @gray;

  #icon {
    background-color: @red;
    padding: 8px;
    border-radius: 15px;
    border-bottom-right-radius: 5px;
  }
}

button {
  cursor: pointer;
  width: 15vw;
  height: 6vh;
  color: @sugar;
  font-size: 17px;
  background-color: transparent;
  border: none;

  display: flex;
  justify-content: center;
  align-items: center;

  position: relative;

  #icon {
    font-size: 30px;
    position: absolute;
    left: 5px;
    padding: 8px;
  }
}
</style>
