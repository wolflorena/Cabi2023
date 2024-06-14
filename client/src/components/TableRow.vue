<script setup lang="ts">
import DateAndTimeSpan from "@/components/DateAndTimeSpan.vue";
import { computed } from "vue";

const props = withDefaults(
  defineProps<{
    index: number;
    columns: any[];
    statusColumn?: string;
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
    <td
      v-if="statusColumn"
      :class="{
        new: statusColumn === 'NEW',
        viewed: statusColumn === 'VIEWED',
        signed: statusColumn === 'SIGNED',
      }"
    >
      {{ statusColumn }} <span v-if="statusColumn === 'NEW'">*</span>
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
    position: relative;
    &:first-child {
      border-top-left-radius: 20px;
      border-bottom-left-radius: 20px;
    }

    &.new {
      color: @new-form;
      font-weight: bolder;
    }

    &.signed {
      color: @signed-form;
      font-weight: bolder;
    }

    &.viewed {
      font-weight: bolder;
      color: @viewed-form;
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
