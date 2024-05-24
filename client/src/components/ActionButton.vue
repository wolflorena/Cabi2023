<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faCirclePlus } from "@fortawesome/free-solid-svg-icons";

library.add(faCirclePlus);

const props = withDefaults(
  defineProps<{
    iconToken: string;
    buttonStyle: "full" | "transparent";
    text?: string;
  }>(),
  {
    buttonStyle: "transparent",
    text: "Add appointment",
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
  <button :class="buttonStyle" @click="handleClickEvent">
    <font-awesome-icon :icon="iconToken" id="icon" />
    <span v-if="buttonStyle === 'full'">{{ text }}</span>
  </button>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.full {
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

.transparent {
  border: none;
  background-color: transparent;
  cursor: pointer;

  #icon {
    color: @gray;
  }
}
</style>
