<script setup lang="ts">
import { Doctor } from "@/data/types/Entities";
import { ref } from "vue";

const props = defineProps<{
  doctors: Doctor[];
  isOpen: boolean;
}>();

const selectedDoctor = ref<string>("Select Doctor");

const dropdownOpen = ref<boolean>(props.isOpen);
</script>
<template>
  <div class="doctors-dropdown-container">
    <span class="selected-doctor-label"> {{ selectedDoctor }}</span>
    <div class="icon-container">
      <font-awesome-icon
        icon="circle-chevron-down"
        id="chevron-down-icon"
        class="chevron-down-icon"
      />
    </div>
  </div>
  <div class="doctors-dropdown" v-show="dropdownOpen"></div>
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
    .chevron-down-icon {
      font-size: 32px;
      color: @white;
    }
  }
}

.doctors-dropdown {
  display: flex;
  background-color: @gray;
}
</style>
