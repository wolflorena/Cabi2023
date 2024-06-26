<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    show: boolean;
    title?: string;
    button1Text?: string;
    button2Text?: string;
    oneButton?: boolean;
    variant?: string;
  }>(),
  {
    button1Text: "Save",
    button2Text: "Cancel",
    variant: "DARK",
  }
);

const emits = defineEmits(["button1", "button2"]);
</script>

<template>
  <Transition name="modal">
    <div v-if="show" class="modal-mask">
      <div
        class="modal-container"
        :class="{
          light: variant === 'LIGHT',
          dark: variant === 'DARK',
        }"
      >
        <div class="modal-header">
          <h1>{{ title }}</h1>
          <slot></slot>
        </div>
        <div class="modal-footer">
          <button class="modal-button1" @click="$emit('button1')">
            {{ button1Text }}
          </button>
          <button
            v-if="!oneButton"
            class="modal-button2"
            @click="$emit('button2')"
          >
            {{ button2Text }}
          </button>
        </div>
      </div>
    </div>
  </Transition>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.modal-mask {
  position: fixed;
  z-index: 999;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.2);
  display: flex;
  transition: opacity 0.3s ease;

  .modal-container {
    min-width: 300px;
    margin: auto;
    padding: 20px 30px;
    background-color: @gray;
    border-radius: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
    transition: all 0.3s ease;
    backdrop-filter: blur(5px);
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    position: relative;

    &.light {
      background-color: @light;

      .modal-header {
        h1 {
          color: @heavy-smoke;
          font-weight: bold;
          font-size: 22px;
        }
      }
    }
    .modal-header,
    .modal-footer {
      width: 100%;
    }

    .modal-header {
      h1 {
        color: @font-gray;
        font-weight: 300;
        padding-bottom: 25px;
        font-size: 22px;
      }
    }

    .modal-footer {
      display: flex;
      justify-content: space-between;
      padding-top: 25px;

      button {
        background-color: transparent;
        border: none;
        cursor: pointer;

        &.modal-button {
          &1 {
            display: flex;
            align-items: center;
            gap: 6px;
            padding: 6px 10px;

            font-size: 20px;
            color: @green;
            &:hover {
              background-color: @sugar;
              border-radius: 30px;
            }
          }
          &2 {
            display: flex;
            align-items: center;
            gap: 6px;
            font-size: 20px;
            padding: 6px 10px;

            color: @red;
            &:hover {
              background-color: @sugar;
              border-radius: 30px;
            }
          }
        }
      }
    }
  }
}

.modal-enter-from {
  opacity: 0;
}

.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-container,
.modal-leave-to .modal-container {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}
</style>
