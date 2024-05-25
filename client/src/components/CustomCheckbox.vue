<script setup lang="ts">
import { StyleValue } from "vue";

const props = withDefaults(
  defineProps<{
    uuid: string;
    text?: string;
    color?: string;
    modelValue: boolean;
    fontSize?: StyleValue;
    fontColor?: string;
  }>(),
  {
    modelValue: false,
    text: "",
    color: "#43506c",
  }
);

const emit = defineEmits(["update:modelValue"]);

function onCheckedChange(event: Event) {
  const target = event.target as HTMLInputElement;
  emit("update:modelValue", target.checked);
}
</script>

<template>
  <input
    class="inp-cbx"
    type="checkbox"
    :checked="modelValue"
    @change="onCheckedChange"
    :id="uuid"
  />
  <label class="cbx" :for="uuid" :key="uuid"
    ><span>
      <svg width="12px" height="10px">
        <use xlink:href="#check-4"></use></svg></span
    ><span
      :style="{ fontSize: fontSize ? fontSize + 'px' : '', color: fontColor ?? '' }"
      >{{ text }}</span
    ></label
  >
  <svg class="inline-svg">
    <symbol id="check-4" viewbox="0 0 12 10">
      <polyline points="1.5 6 4.5 9 10.5 1"></polyline>
    </symbol>
  </svg>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.cbx {
  -webkit-user-select: none;
  user-select: none;
  cursor: pointer;
  padding: 6px 8px;
  border-radius: 6px;
  overflow: hidden;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;

  &:not(:last-child) {
    margin-right: 6px;
  }
  &:hover {
    background: rgba(0, 119, 255, 0.06);

    span:first-child {
      border-color: v-bind(color);
    }
  }
  span {
    float: left;
    vertical-align: middle;
    transform: translate3d(0, 0, 0);

    &:first-child {
      position: relative;
      width: 18px;
      height: 18px;
      border-radius: 4px;
      transform: scale(1);
      border: 1.5px solid @font-dark-gray;
      transition: all 0.2s ease;
      box-shadow: 0 1px 1px rgba(0, 16, 75, 0.05);

      svg {
        position: absolute;
        top: 3px;
        left: 2px;
        fill: none;
        stroke: #fff;
        stroke-width: 2;
        stroke-linecap: round;
        stroke-linejoin: round;
        stroke-dasharray: 16px;
        stroke-dashoffset: 16px;
        transition: all 0.3s ease;
        transition-delay: 0.1s;
        transform: translate3d(0, 0, 0);
      }
    }

    &:last-child {
      padding-left: 8px;
      line-height: 18px;
    }
  }
}

.inp-cbx {
  position: absolute;
  visibility: hidden;

  &:checked + .cbx span:first-child {
    background: v-bind(color);
    border-color: v-bind(color);
    animation: wave-4 0.4s ease;

    svg {
      stroke-dashoffset: 0;
    }
  }
}
.inline-svg {
  position: absolute;
  width: 0;
  height: 0;
  pointer-events: none;
  user-select: none;
}

@-moz-keyframes wave-4 {
  50% {
    transform: scale(0.9);
  }
}
@-webkit-keyframes wave-4 {
  50% {
    transform: scale(0.9);
  }
}
@-o-keyframes wave-4 {
  50% {
    transform: scale(0.9);
  }
}
@keyframes wave-4 {
  50% {
    transform: scale(0.9);
  }
}
</style>
