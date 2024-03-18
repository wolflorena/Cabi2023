<script setup lang="ts">
import { ref } from "vue";
import SidebarButton from "./SidebarButton.vue";
import type { SidebarOption } from "@/data/types/SidebarOptions";
import { useRoute, useRouter } from "vue-router";

defineProps<{
  options: SidebarOption[];
}>();

const router = useRouter();
const emits = defineEmits<{
  (e: "contentChanged", payload: string): void;
}>();

const hideSidebar = ref(false);
const selectedContent = ref("Dashboard");

const handleContentChange = (newValue: string, newPath: string): void => {
  selectedContent.value = newValue;
  router.push(`/admin${newPath}`);
  emits("contentChanged", newValue);
};
</script>

<template>
  <button
    @click="hideSidebar = !hideSidebar"
    class="hideSidebar"
    :class="{ hided: hideSidebar }"
  >
    <font-awesome-icon id="icon" icon="bars" />
  </button>
  <div class="sidebar" :class="{ hided: hideSidebar }">
    <div class="logo-container">
      <img src="@/assets/logo.png" alt="" />
    </div>
    <div class="selections">
      <SidebarButton
        v-for="(option, index) in options"
        :key="index"
        :selected="selectedContent === option.text"
        :text="option.text"
        :icon="option.icon"
        @selection-changed="handleContentChange(option.text, option.path)"
      />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.sidebar {
  height: 100vh;
  width: 15vw;
  background-color: @blue;
  border-top-right-radius: 100px;
  transition: transform 0.3s ease;

  .logo-container {
    padding-top: 20px;
    display: flex;
    justify-content: center;

    img {
      width: 200px;
    }
  }

  .selections {
    display: flex;
    align-items: flex-end;
    flex-direction: column;
    gap: 15px;
  }

  &.hided {
    transform: translateX(-100%);
  }
}

.hideSidebar {
  cursor: pointer;
  background-color: transparent;
  border: none;
  position: absolute;
  top: 10px;
  left: 10px;

  #icon {
    color: @black;
    height: 20px;
  }
}
</style>
@/data/types/SidebarOptions@/data/types/SidebarOptions
