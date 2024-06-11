<script setup lang="ts">
import { computed, ref, watch } from "vue";

const props = withDefaults(
  defineProps<{
    uuid: string;
    label: string;
    variant?: string;
    inputValue?: string;
    isReadonly?: boolean;
    type?: string;
    fontSize?: string;
  }>(),
  {
    isReadonly: true,
    type: "text",
    variant: "DEFAULT",
  }
);

const emits = defineEmits<{
  "update:inputValue": [e: any];
  onBlur: [];
  onFocus: [];
}>();

const dateInput = ref<string | undefined>(props.inputValue);
const phoneNo = ref<string | undefined>(props.inputValue);
const wrongFormat = ref<boolean>(false);

const inputChange = (e: any) => {
  if (e.target.value == "") {
    wrongFormat.value = true;
  } else {
    wrongFormat.value = false;
    emits("update:inputValue", e.target.value);
    emits("onFocus");
  }
};

// logic to prevent user to enter wrong dates as their date of birth.
watch(dateInput, (newValue) => {
  const dateRegex = /^(19|20)\d\d-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/;

  if (newValue) {
    let formattedValue = newValue.replace(/\D/g, "");

    if (formattedValue.length > 4) {
      formattedValue = `${formattedValue.slice(0, 4)}-${formattedValue.slice(
        4
      )}`;
    }
    if (formattedValue.length > 7) {
      formattedValue = `${formattedValue.slice(0, 7)}-${formattedValue.slice(
        7
      )}`;
    }
    dateInput.value = formattedValue;
    wrongFormat.value = false;
    if (dateRegex.test(formattedValue)) {
      emits("update:inputValue", formattedValue);
    } else {
      wrongFormat.value = true;
    }
  }
});

const handleBlur = () => {
  emits("onBlur");
};
const handleFocus = () => {
  emits("onFocus");
};

//here is a check to immediate format the phoneNo info fields
watch(
  phoneNo,
  (newValue) => {
    if (newValue && props.type === "phoneNo") {
      let formattedValue = newValue.replace(/\D/g, "");

      if (formattedValue.length > 4) {
        formattedValue = `${formattedValue.slice(0, 4)}-${formattedValue.slice(
          4
        )}`;
      }
      if (formattedValue.length > 8) {
        formattedValue = `${formattedValue.slice(0, 8)}-${formattedValue.slice(
          8
        )}`;
      }
      phoneNo.value = formattedValue;
      emits("update:inputValue", formattedValue);
    }
  },
  {
    immediate: true,
  }
);
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
      v-if="type === 'date'"
      type="text"
      :id="uuid"
      :value="dateInput"
      :readonly="isReadonly"
      maxlength="10"
      :class="wrongFormat ? 'bad-format' : ''"
      @input="(e:any)=> dateInput=e.target.value"
      @focus="handleFocus"
      @blur="handleBlur"
    />
    <input
      v-else-if="type === 'phoneNo'"
      :type="type"
      :id="uuid"
      :value="phoneNo"
      :readonly="isReadonly"
      :class="wrongFormat ? 'bad-format' : ''"
      @input="(e:any) => phoneNo = e.target.value"
      maxlength="12"
      @focus="handleFocus"
      @blur="handleBlur"
    />
    <input
      v-else
      :type="type"
      :id="uuid"
      :value="inputValue"
      :readonly="isReadonly"
      :class="wrongFormat ? 'bad-format' : ''"
      :style="{fontSize: fontSize ? fontSize +'px' : '' }"
      @input="inputChange"
      @focus="handleFocus"
      @blur="handleBlur"
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
    &.bad-format {
      border-color: red;
    }
  }

  input[readonly] {
    cursor: not-allowed;
    opacity: 0.8;
  }
}
</style>
