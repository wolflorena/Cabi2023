<script setup lang="ts">
import { ref, defineProps, defineEmits, computed } from "vue";

const props = withDefaults(
  defineProps<{
    doctors: string[];
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
    doctor.toUpperCase().includes(searchQuery.value.toUpperCase())
  );
});

function updateSearchQuery(event: Event) {
  const target = event.target as HTMLInputElement;
  searchQuery.value = target.value;
}

function selectDoctor(doctorName: string) {
  emit("select", doctorName);
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
        :key="doctor"
        @click="selectDoctor(doctor)"
        class="element"
      >
        {{ doctor }}
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
      height: 3vh;
      border: none;
      background-color: @light-gray;
      box-sizing: border-box;

      padding: 3px;

      &:focus {
        border-color: transparent;
        outline: none;
      }
    }
  }

  .dropdown {
    background-color: @light-gray;
    width: 19vw;
    position: absolute;
    top: 100%;
    max-height: 100px;
    overflow: scroll;
    scrollbar-color: @gray;

    display: flex;
    flex-direction: column;
    gap: 5px;

    .element {
      border-bottom: 0.1px solid @gray;
      cursor: pointer;

      &:hover {
        background-color: @red;
      }
    }
  }
}
</style>
