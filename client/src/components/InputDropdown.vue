<script setup lang="ts">
import { ref, defineProps, defineEmits } from "vue";
import CustomCheckbox from "./CustomCheckbox.vue";

const props = withDefaults(
  defineProps<{
    array: string[];
    label: string;
    placeholder: string;
    multiple?: boolean;
  }>(),
  {
    multiple: false,
  }
);

const emit = defineEmits(["select", "toggleDropdown"]);

const displayDropdown = ref(false);
const selectedValues = ref<string[]>([]);

function select(element: string) {
  if (props.multiple) {
    if (selectedValues.value.includes(element)) {
      selectedValues.value = selectedValues.value.filter((e) => e !== element);
    } else {
      selectedValues.value.push(element);
    }
  } else {
    selectedValues.value = [element];
    displayDropdown.value = false;
  }
  emit("select", selectedValues.value);
}

function toggleDropdown() {
  displayDropdown.value = !displayDropdown.value;
}

function isSelected(element: string) {
  return selectedValues.value.includes(element);
}
</script>

<template>
  <div class="add">
    <label>
      {{ label }}
      <input type="text" :placeholder="placeholder" @click="toggleDropdown" readonly/>
    </label>
    <div v-if="displayDropdown" class="dropdown">
      <div
        v-for="element in array"
        :key="`uuid-${element}`"
        @click="select(element)"
        class="element"
      >
        <CustomCheckbox v-if="multiple" :text="element" :uuid="`${element}-uuid`" :modelValue="isSelected(element)" @update:modelValue="select(element)" />
        <span v-else>{{ element }}</span>
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";
.add {
  position: relative;
  label {
    color: @font-dark-gray;
    font-weight: bolder;
    display: flex;
    flex-direction: column;
    gap: 5px;
    width: 19vw;

    input {
      height: 5vh;
      border: none;
      background-color: @light-gray;
      box-sizing: border-box;
      padding: 10px;

      &:focus {
        border-color: transparent;
        outline: none;
      }
    }
  }

  .dropdown {
    background-color: @light-gray;
    width: 19vw;
    max-height: 100px;
    overflow: scroll;
    z-index: 100;
    position: absolute;
    border-radius: 6px;
    margin-top: 5px;
    max-height: 180px;
    -webkit-transition: 0.4s linear 0.2s;
    -o-transition: 0.4s linear 0.2s;
    transition: 0.4s linear 0.2s;

    .element {
      padding: 8px 5px;
      font-weight: 500;
      font-size: 14px;
      -webkit-transition: 0.3s ease-in-out;
      -o-transition: 0.3s ease-in-out;
      transition: 0.3s ease-in-out;

      &:hover {
        color: @blue;
      }
    }

    &&::-webkit-scrollbar {
      width: 4px;
      background-color: red;
    }

    &&::-webkit-scrollbar-thumb {
      background-color: green;
      border-radius: 10px;
    }
  }
}
</style>
