<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import TableHeaderButton from "@/components/TableHeaderButton.vue";
import { CustomerSidebarOptions } from "@/data/types/SidebarOptions";
import { ref } from "vue";
import ViewProfile from "./profile-content/ViewProfile.vue";
import AvatarImage from "@/components/AvatarImage.vue";

const profilePage = ref("viewProfile");
</script>

<template>
  <div class="container">
    <Sidebar :options="CustomerSidebarOptions" />
    <div class="profile-container">
      <div class="header">
        <div class="controls">
          <TableHeaderButton
            label="View Profile"
            :active="profilePage === 'viewProfile'"
            @click="profilePage = 'viewProfile'"
          />

          <TableHeaderButton
            label="Edit Profile"
            :active="profilePage === 'editProfile'"
            @click="profilePage = 'editProfile'"
          />

          <TableHeaderButton
            label="Security settings"
            :active="profilePage === 'securitySettings'"
            @click="profilePage = 'securitySettings'"
          />
        </div>
      </div>
      <div class="profile-content">
        <div v-if="profilePage === 'viewProfile'">
          <ViewProfile />
        </div>

        <div v-if="profilePage === 'editProfile'">
          {{ profilePage }}
        </div>

        <div v-if="profilePage === 'securitySettings'">
          {{ profilePage }}
        </div>
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
    width: 80vw;
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
}
</style>
@/data/types/SidebarOptions
