<script setup lang="ts">
import { Doctor } from "@/data/types/Entities";
import { ref, defineProps, defineEmits, computed } from "vue";

const props = withDefaults(
  defineProps<{
    doctors: Doctor[];
    label: string;
    placeholder: string;
  }>(),
  {}
);

const emit = defineEmits(["select", "toggleDropdown"]);

const searchQuery = ref("");
const displayDropdown = ref(false);

const filteredDoctors = computed(() => {
  return props.doctors.filter((doctor) =>
    doctor.firstName.toUpperCase().includes(searchQuery.value.toUpperCase())
  );
});

function updateSearchQuery(event: Event) {
  const target = event.target as HTMLInputElement;
  searchQuery.value = target.value;
}

function selectDoctor(doctor: Doctor) {
  emit("select", doctor);
  displayDropdown.value = false;
}

function toggleDropdown() {
  displayDropdown.value = !displayDropdown.value;
}
</script>

<template>
  <div class="add-calendar">
    <label
      >{{ label }}
      <input
        type="text"
        :placeholder="placeholder"
        @click="toggleDropdown"
        @keyup="updateSearchQuery"
    /></label>
    <div v-if="displayDropdown" class="dropdown">
      <div
        v-for="doctor in filteredDoctors"
        :key="doctor.id"
        @click="selectDoctor(doctor)"
        class="element"
      >
        {{ "Dr. " + doctor.firstName + " " + doctor.lastName }}
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";
.add-calendar {
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
