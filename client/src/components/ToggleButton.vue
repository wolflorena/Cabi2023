<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    uuid: string;
    text?: string;
    modelValue: boolean;
  }>(),
  {
    modelValue: false,
    text: "",
  }
);

const emit = defineEmits(["update:modelValue"]);

function onCheckedChange(event: Event) {
  const target = event.target as HTMLInputElement;
  emit("update:modelValue", target.checked);
}
</script>

<template>
  <div class="custom-checkbox">
    <span>{{ text }}</span>
    <div class="toggle-wrapper">
      <input
        type="checkbox"
        id="toggle-input"
        @change="onCheckedChange"
        :checked="modelValue"
      />
      <label for="toggle-input" class="toggle-switch"><span></span></label>
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

// .checkbox {
//   display: flex;
//   gap: 15px;
//   color: @gray;
//   align-items: center;
// }
// .checkbox-wrapper-3 input[type="checkbox"] {
//   visibility: hidden;
//   display: none;
// }

// .checkbox-wrapper-3 .toggle {
//   position: relative;
//   display: block;
//   width: 40px;
//   height: 20px;
//   cursor: pointer;
//   -webkit-tap-highlight-color: transparent;
//   transform: translate3d(0, 0, 0);
// }
// .checkbox-wrapper-3 .toggle:before {
//   content: "";
//   position: relative;
//   top: 3px;
//   left: 3px;
//   width: 34px;
//   height: 14px;
//   display: block;
//   background: #9a9999;
//   border-radius: 8px;
//   transition: background 0.2s ease;
// }
// .checkbox-wrapper-3 .toggle span {
//   position: absolute;
//   top: 0;
//   left: 0;
//   width: 20px;
//   height: 20px;
//   display: block;
//   background: white;
//   border-radius: 10px;
//   box-shadow: 0 3px 8px rgba(154, 153, 153, 1);
//   transition: all 0.2s ease;
// }
// .checkbox-wrapper-3 .toggle span:before {
//   content: "";
//   position: absolute;
//   display: block;
//   margin: -18px;
//   width: 56px;
//   height: 56px;
//   background: rgba(79, 46, 220, 0.5);
//   border-radius: 50%;
//   transform: scale(0);
//   opacity: 1;
//   pointer-events: none;
// }

// .checkbox-wrapper-3 #cbx-3:checked + .toggle:before {
//   background: #947ada;
// }
// .checkbox-wrapper-3 #cbx-3:checked + .toggle span {
//   background: #4f2edc;
//   transform: translateX(20px);
//   transition: all 0.2s cubic-bezier(0.8, 0.4, 0.3, 1.25), background 0.15s ease;
//   box-shadow: 0 3px 8px rgba(79, 46, 220, 0.2);
// }
// .checkbox-wrapper-3 #cbx-3:checked + .toggle span:before {
//   transform: scale(1);
//   opacity: 0;
//   transition: all 0.4s ease;
// }

.custom-checkbox {
  display: flex;
  gap: 15px;
  color: @gray;
  align-items: center;

  .toggle-wrapper {
    input[type="checkbox"] {
      visibility: hidden;
      display: none;
    }

    .toggle-switch {
      position: relative;
      display: block;
      width: 40px;
      height: 20px;
      cursor: pointer;
      -webkit-tap-highlight-color: transparent;
      transform: translate3d(0, 0, 0);

      &:before {
        content: "";
        position: relative;
        top: 3px;
        left: 3px;
        width: 34px;
        height: 14px;
        display: block;
        background: #9a9999;
        border-radius: 8px;
        transition: background 0.2s ease;
      }

      span {
        position: absolute;
        top: 0;
        left: 0;
        width: 20px;
        height: 20px;
        display: block;
        background: white;
        border-radius: 10px;
        box-shadow: 0 3px 8px rgba(154, 153, 153, 1);
        transition: all 0.2s ease;

        &:before {
          content: "";
          position: absolute;
          display: block;
          margin: -18px;
          width: 56px;
          height: 56px;
          background: rgba(48, 97, 155, 0.5);
          border-radius: 50%;
          transform: scale(0);
          opacity: 1;
          pointer-events: none;
        }
      }
    }

    #toggle-input:checked + .toggle-switch {
      &:before {
        background: #4d7db3;
      }

      span {
        background: @blue;
        transform: translateX(20px);
        transition: all 0.2s cubic-bezier(0.8, 0.4, 0.3, 1.25),
          background 0.15s ease;
        box-shadow: 0 3px 8px rgba(79, 46, 220, 0.2);

        &:before {
          transform: scale(1);
          opacity: 0;
          transition: all 0.4s ease;
        }
      }
    }
  }
}
</style>
