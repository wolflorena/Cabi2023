<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import TableHeaderButton from "@/components/TableHeaderButton.vue";
import { CustomerSidebarOptions } from "@/data/types/SidebarOptions";
import { computed, ref } from "vue";
import ViewProfile from "./profile-content/ViewProfile.vue";
import EditProfile from "./profile-content/EditProfile.vue";
import SecuritySettings from "./profile-content/SecuritySettings.vue";

const profilePage = ref("viewProfile");

const isActivePage = (page: string) =>
  computed(() => profilePage.value === page);

const handleClick = (page: string) => {
  profilePage.value = page;
  console.log(isActivePage("viewProfile").value);
};
</script>

<template>
  <div class="container">
    <Sidebar :options="CustomerSidebarOptions" />
    <div class="profile-container">
      <div class="header">
        <div class="controls">
          <TableHeaderButton
            label="View Profile"
            :active="isActivePage('viewProfile').value"
            @click="handleClick('viewProfile')"
          />

          <TableHeaderButton
            label="Edit Profile"
            :active="isActivePage('editProfile').value"
            @click="handleClick('editProfile')"
          />

          <TableHeaderButton
            label="Security settings"
            :active="isActivePage('securitySettings').value"
            @click="handleClick('securitySettings')"
          />
        </div>
      </div>
      <div class="profile-content">
        <ViewProfile v-if="isActivePage('viewProfile').value" />

        <EditProfile v-if="isActivePage('editProfile').value" />

        <SecuritySettings v-if="isActivePage('securitySettings').value" />
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;
  justify-content: space-between;
  .profile-container {
    display: flex;
    flex-direction: column;
    width: 90vw;
    height: 100vh;
    .header {
      align-self: flex-end;
      background-color: @gray;
      height: 5vh;
      width: 80%;
      border-bottom-left-radius: 40px;

      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      user-select: none;

      .controls {
        width: 100%;
        display: flex;
        justify-content: space-around;
      }
    }
  }

  .profile-content {
    height: 100%;
    display: flex;
    justify-content: center;
  }
}
</style>
@/data/types/SidebarOptions
