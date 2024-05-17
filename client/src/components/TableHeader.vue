<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    columns: string[];
    isMain?: boolean;
    hasEmptyRow?: boolean;
    hasActionButton?: boolean;
  }>(),
  {
    isMain: true,
    hasEmptyRow: true,
    hasActionButton: false,
  }
);

const emits = defineEmits<{
  (e: "actionTriggered"): void;
}>();

const handleClickEvent = () => {
  emits("actionTriggered");
};
</script>

<template>
  <thead>
    <tr :class="{ isMain }">
      <th>No.#</th>
      <th v-for="column in columns">{{ column }}</th>
      <th v-if="hasActionButton">
        <button @click="handleClickEvent" class="add-button">
          Add new product
        </button>
      </th>
    </tr>
  </thead>
  <tr v-if="hasEmptyRow" style="height: 20px">
    <td :colspan="columns.length + 1"></td>
  </tr>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

thead tr {
  height: 8vh;
  background-color: @light-gray;
  font-size: large;
}

.isMain {
  background-color: @gray;
  color: @white;

  th:first-child {
    border-bottom-left-radius: 40px;
  }
}

.add-button {
  background-color: transparent;
  border: 1px solid @white;
  color: @white;
  font-size: large;
  padding: 7px;
  cursor: pointer;
}
</style>
