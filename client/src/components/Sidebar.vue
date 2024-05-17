<script setup lang="ts">
import SidebarButton from "./SidebarButton.vue";
import type { SidebarOptionsWithRole } from "@/data/types/SidebarOptions";
import { useRouter } from "vue-router";

defineProps<{
  options: SidebarOptionsWithRole;
}>();

const router = useRouter();

const handleContentChange = (role: string, newPath: string): void => {
  if (role === "customer") {
    router.push(newPath);
  } else {
    router.push("/" + role + newPath);
  }
  console.log(router.currentRoute.value.name);
};
</script>

<template>
  <div class="sidebar">
    <div class="logo-container">
      <img src="@/assets/logo.png" alt="" />
    </div>
    <div class="selections">
      <SidebarButton
        v-for="(option, index) in options.options"
        :key="index"
        :selected="
          ('/' + String(router.currentRoute.value.name)).includes(option.path)
        "
        :text="option.text"
        :icon="option.icon"
        @selection-changed="handleContentChange(options.role, option.path)"
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
