<script setup lang="ts">
withDefaults(
  defineProps<{
    uuid: string;
    label: string;
    variant?: string;
    inputValue?: string;
    isReadonly?: boolean;
    type?: string;
  }>(),
  {
    isReadonly: true,
    type: "text",
    variant: "DEFAULT",
  }
);

const emit = defineEmits(["update:inputValue"]);

const inputChange = (event: any) => {
  emit("update:inputValue", event.target.value);
};
</script>

<template>
  <div
    class="info-field-default"
    :class="{
      account_info: variant === 'ACCOUNT_INFORMATION',
    }"
  >
    <label :for="uuid">{{ label }}</label>
    <input
      :type="type"
      :id="uuid"
      :value="inputValue"
      :readonly="isReadonly"
      @input="inputChange"
    />
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.info-field-default {
  display: flex;
  flex-direction: column;

  &.account_info {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    input {
      width: 22vw;
    }
  }
  label {
    color: @blue;
    margin-bottom: 5px;
    font-weight: bold;
    font-size: larger;
  }

  input {
    padding: 8px;
    height: 4vh;
    border: 0.5px solid @gray;
    border-radius: 4px;
    background-color: transparent;
    color: @black;
  }

  input[readonly] {
    cursor: not-allowed;
    opacity: 0.8;
  }
}
</style>
