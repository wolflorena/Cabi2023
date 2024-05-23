<script setup lang="ts">
import { ref } from "vue";
import CustomInput from "./CustomInput.vue";

const props = withDefaults(
  defineProps<{
    labelText: string;
    value: string;
  }>(),
  {
    labelText: "",
  }
);

const emits = defineEmits(["password-changed"]);

const showPassword = ref(false);

function sendPassword(text: string) {
  emits("password-changed", text);
}

function toggleShowPassword(event: Event) {
  event.preventDefault();
  showPassword.value = !showPassword.value;
}
</script>

<template>
  <span class="password-container">
    <CustomInput
      :type="showPassword ? 'text' : 'password'"
      :label-text="labelText"
      :model-value="value"
      icon-token="key"
      id="password-input"
      @update:model-value="sendPassword"
    />
    <button @click="toggleShowPassword">
      <font-awesome-icon v-if="!showPassword" icon="eye" id="icon" />
      <font-awesome-icon v-else icon="eye-slash" id="icon" />
    </button>
  </span>
</template>

<style scoped lang="less">
span {
  position: relative;
  button {
    border: none;
    position: absolute;
    right: 0;
    top: 50%;
    transform: translateY(-50%);
    z-index: 9999;
  }
}
</style>
