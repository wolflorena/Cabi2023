<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import TableHeaderButton from "@/components/TableHeaderButton.vue";
import { CustomerSidebarOptions } from "@/data/types/SidebarOptions";
import { computed, onMounted, ref } from "vue";
import ViewProfile from "./profile-content/ViewProfile.vue";
import EditProfile from "./profile-content/EditProfile.vue";
import SecuritySettings from "./profile-content/SecuritySettings.vue";
import { useUserProfile } from "@/store/useUserProfile";
import { SwalLoading } from "@/utils/helpers";

const profilePage = ref<string>("viewProfile");

const isActivePage = (page: string) =>
  computed(() => profilePage.value === page);

const handleClick = (page: string) => {
  profilePage.value = page;
};

const {
  userDetails,
  avatarImage,
  fetchUserProfile,
  retrieveUserAvatar,
  updateUserProfile,
  updateAvatarImage,
} = useUserProfile();

onMounted(async () => {
  console.log("JWT TOKEN IS" + localStorage.getItem("jwtToken"));

  if (userDetails.value === null || avatarImage.value === null) {
    SwalLoading.fire();
    await fetchUserProfile();
    await retrieveUserAvatar();
    SwalLoading.close();
  }
});
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
            variant="DEFAULT"
          />

          <TableHeaderButton
            label="Edit Profile"
            :active="isActivePage('editProfile').value"
            @click="handleClick('editProfile')"
            variant="DEFAULT"
          />

          <TableHeaderButton
            label="Security settings"
            :active="isActivePage('securitySettings').value"
            @click="handleClick('securitySettings')"
            variant="DEFAULT"
          />
        </div>
      </div>
      <div class="profile-content" v-if="userDetails && avatarImage">
        <ViewProfile
          v-if="isActivePage('viewProfile').value"
          :userDetails="userDetails"
          :avatarImage="avatarImage"
        />

        <EditProfile
          v-if="isActivePage('editProfile').value"
          :userDetails="userDetails"
          :avatarImage="avatarImage"
          @updateUserDetails="updateUserProfile"
          @update-avatar-image="updateAvatarImage"
        />

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
      height: 70px;
      width: 80%;
      border-bottom-left-radius: 40px;

      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      user-select: none;

      .controls {
        width: 100%;
        height: 100%;
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
