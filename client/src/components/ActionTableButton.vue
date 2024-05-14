<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    iconToken: string;
  }>(),
  {}
);

const emits = defineEmits<{
  (e: "actionTriggered"): void;
}>();

const handleClickEvent = () => {
  emits("actionTriggered");
};
</script>

<template>
  <button @click="handleClickEvent">
    <font-awesome-icon :icon="iconToken" id="icon" />
  </button>
</template>

<style scoped lang="less">
button {
  border: none;
  background-color: transparent;
  cursor: pointer;
}
</style>
