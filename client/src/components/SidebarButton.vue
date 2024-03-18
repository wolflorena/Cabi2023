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

const emits = defineEmits<{
  (e: "selectionChanged", payload: string): void;
}>();

const handleOptionClicked = () => {
  emits("selectionChanged", props.text);
};
</script>

<template>
  <button :class="{ selected }" @click="handleOptionClicked">
    <font-awesome-icon id="icon" :icon="icon" />
    <span>
      {{ text }}
    </span>
  </button>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.selected {
  background-color: @gray;
  border-top-left-radius: 20px;
  border-bottom-left-radius: 20px;

  #icon {
    background-color: @red;
    padding: 8px;
    border-radius: 15px;
    border-bottom-right-radius: 5px;
  }
}

button {
  cursor: pointer;
  width: 14vw;
  height: 6vh;
  color: @sugar;
  font-size: 17px;
  background-color: transparent;
  border: none;

  padding-left: 5vw;
  display: flex;
  justify-content: flex-start;
  align-items: center;

  position: relative;

  #icon {
    font-size: 25px;
    position: absolute;
    left: 5px;
    padding: 8px;
  }
}
</style>
