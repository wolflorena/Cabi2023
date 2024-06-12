<script setup lang="ts">
import SidebarButton from "./SidebarButton.vue";
import type { SidebarOptionsWithRole } from "@/data/types/SidebarOptions";
import { useRouter } from "vue-router";
import CustomButton from "./CustomButton.vue";
import { useUserProfile } from "@/store/useUserProfile";

defineProps<{
  options: SidebarOptionsWithRole;
}>();

const router = useRouter();
const { userDetails, avatarImage } = useUserProfile();

const handleContentChange = (role: string, newPath: string): void => {
  if (role === "customer") {
    router.push(newPath);
  } else {
    router.push("/" + role + newPath);
  }
};

function handleLogOut() {
  localStorage.removeItem("jwtToken");
  userDetails.value = null;
  avatarImage.value = null;
  router.push("/login");
}
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
        :selected="String(router.currentRoute.value.path).includes(option.path)"
        :text="option.text"
        :icon="option.icon"
        @selection-changed="handleContentChange(options.role, option.path)"
      />
      <CustomButton
        uuid="logout-button"
        class="logout-button"
        text="Log out"
        :isMain="false"
        height="60"
        width="160"
        @click="handleLogOut"
      />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.sidebar {
  height: 100vh;
  min-width: 15vw;
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
    .logout-button {
      align-self: center;

      position: absolute;
      bottom: 45px;
    }
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
