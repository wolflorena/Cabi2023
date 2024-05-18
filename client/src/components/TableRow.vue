<script setup lang="ts">
import DateAndTimeSpan from "@/components/DateAndTimeSpan.vue";
import { computed } from "vue";

const props = withDefaults(
  defineProps<{
    index: number;
    columns: any[];
    date?: string;
    time?: string;
    highlight?: string; // 'even' or 'odd'
    hasEmptyColumn?: boolean;
  }>(),
  {
    highlight: "even",
    hasEmptyColumn: false,
  }
);

const isHighlighted = (index: number) => {
  return (
    (props.highlight === "even" && index % 2 === 0) ||
    (props.highlight === "odd" && index % 2 !== 0)
  );
};

const rowClass = computed(() => {
  return isHighlighted(props.index) ? "highlight" : "";
});
</script>

<template>
  <tr :class="rowClass">
    <td v-for="column in columns">
      {{ column }}
    </td>
    <td v-if="date && time">
      <DateAndTimeSpan :date="date" :time="time" />
    </td>
    <td>
      <slot></slot>
    </td>
    <td class="empty-column" v-if="hasEmptyColumn"></td>
  </tr>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

tr {
  height: 8vh;
  font-size: 20px;
  td {
    text-align: center;
    &:first-child {
      border-top-left-radius: 20px;
      border-bottom-left-radius: 20px;
    }
  }
  &:hover {
    background-color: @sugar;
  }
}

.highlight {
  background-color: @light-gray;
}
</style>
