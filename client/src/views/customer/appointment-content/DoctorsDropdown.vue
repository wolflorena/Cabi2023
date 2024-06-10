<script setup lang="ts">
import { Doctor } from "@/data/types/Entities";
import { computed, ref } from "vue";

const props = defineProps<{
  doctors: Doctor[] | null;
  isOpen: boolean;
  selectedDoctor: Doctor | null;
}>();

const emits = defineEmits<{
  onDoctorSelected: [doctor: Doctor];
}>();

const displayedDoctor = computed(() => {
  if (props.selectedDoctor) {
    return (
      "Dr. " +
      props.selectedDoctor.firstName +
      " " +
      props.selectedDoctor.lastName
    );
  } else {
    return "Select a doctor";
  }
});
function handleDoctorSelected(event: Event, clickedDoctor: Doctor) {
  event.stopPropagation();
  emits("onDoctorSelected", clickedDoctor);
}
</script>
<template>
  <div class="doctors-dropdown-container">
    <span class="selected-doctor-label"> {{ displayedDoctor }}</span>
    <div class="icon-container">
      <font-awesome-icon
        icon="circle-chevron-down"
        id="dropdown-icon"
        :class="{
          'dropdown-icon': props.isOpen === false,
          'dropdown-icon-up': props.isOpen === true,
        }"
      />
    </div>
    <transition name="dropdown">
      <div class="doctors-dropdown" v-if="isOpen && doctors">
        <div
          v-for="doctor in doctors"
          class="doctor-selection"
          @click="handleDoctorSelected($event, doctor)"
        >
          <div class="doctor-label">
            Dr. {{ doctor.firstName }} {{ doctor.lastName }}
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<style lang="less" scoped>
@import (reference) "@/assets/styles.less";
.doctors-dropdown-container {
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: space-between;

  .selected-doctor-label {
    margin: 0px 35px;
    color: @white;
    font-size: 22px;
  }
  .icon-container {
    display: flex;
    margin-right: 12vw;

    .dropdown-icon {
      font-size: 32px;
      color: @white;
      transition: transform 0.3s ease;
    }

    .dropdown-icon-up {
      font-size: 32px;
      color: @white;
      transition: transform 0.3s ease;
      transform: rotate(180deg);
    }
  }
  .doctors-dropdown {
    display: flex;
    flex-direction: column;
    background-color: @gray;
    position: absolute;
    top: 100%;
    width: 100%;
    border-bottom-left-radius: 40px;
    overflow: hidden;

    max-height: 200px;
    overflow-y: auto;

    transition: max-height 0.3s ease, opacity 0.3s ease;
    opacity: 1;
    z-index: 1000;
    .doctor-selection {
      transition: background-color 0.3s ease;
      .doctor-label {
        color: @white;
        font-size: 24px;
      }
      padding: 20px 30px;

      &:hover {
        background-color: @blue;
      }
    }

    &::-webkit-scrollbar {
      width: 15px;
      height: 15px;
    }

    &::-webkit-scrollbar-thumb {
      background: linear-gradient(to bottom right, #4d7fff 0%, #1a56ff 100%);
      border-radius: 5px;
    }

    &::-webkit-scrollbar-track:horizontal {
      background-color: @blue;
      box-shadow: inset 0 0 2px 2px gainsboro;
    }
  }
  .dropdown-enter-active,
  .dropdown-leave-active {
    transition: opacity 0.3s ease, transform 0.3s ease;
  }

  .dropdown-enter-from,
  .dropdown-leave-to {
    opacity: 0;
    transform: translateY(-30px);
  }
}
</style>
