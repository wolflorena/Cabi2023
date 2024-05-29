<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import TableHeaderButton from "@/components/TableHeaderButton.vue";
import { CustomerSidebarOptions } from "@/data/types/SidebarOptions";
import { computed, onMounted, ref } from "vue";
import ViewProfile from "./profile-content/ViewProfile.vue";
import EditProfile from "./profile-content/EditProfile.vue";
import SecuritySettings from "./profile-content/SecuritySettings.vue";
import { UserDetails } from "@/data/types/Entities";
import { getAvatar, getById } from "@/services/customer_service";
import { getUserIdAndToken } from "@/services/authentication_service";
import Swal from "sweetalert2";

const profilePage = ref("viewProfile");

const isActivePage = (page: string) =>
  computed(() => profilePage.value === page);

const handleClick = (page: string) => {
  profilePage.value = page;
};

const userDetails = ref<UserDetails>({
  firstName: "string",
  lastName: "string",
  email: "string",
  phoneNo: "string",
  dateOfBirth: "string",
  occupation: "string",
});
const avatarImage = ref<string>();
const isLoading = ref<boolean>(false);

async function loadUserDetails() {
  try {
    const { userId, token } = getUserIdAndToken();
    isLoading.value = true;
    await getById(userId, token).then((resp) => {
      if (resp) {
        userDetails.value = resp;
        isLoading.value = false;
        Swal.close();
      }
    });
  } catch (err) {
    throw new Error("Failed to load user profile: " + err);
  }
}

function handleUserDetailsUpdate(editedUserDetails: UserDetails) {
  userDetails.value = editedUserDetails;
}

async function retrieveUserAvatar() {
  const { userId, token } = getUserIdAndToken();
  const blob = await getAvatar(userId, token);

  if (blob.size > 0) {
    avatarImage.value = URL.createObjectURL(blob);
  } else {
    avatarImage.value = undefined;
  }
}

function handleAvatarImageUpdated(newAvatarImage: string) {
  avatarImage.value = newAvatarImage;
}
onMounted(() => {
  Swal.fire({
    titleText: "Loading...",
    allowOutsideClick() {
      return false;
    },
    showConfirmButton: false,
    position: "center",
    padding: "60px 100px",
  });
  loadUserDetails();
  retrieveUserAvatar();
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
      <div class="profile-content">
        <ViewProfile
          v-if="isActivePage('viewProfile').value"
          :userDetails="userDetails"
          :avatarImage="avatarImage"
        />

        <EditProfile
          v-if="isActivePage('editProfile').value"
          :userDetails="userDetails"
          :avatarImage="avatarImage"
          @updateUserDetails="handleUserDetailsUpdate"
          @update-avatar-image="handleAvatarImageUpdated"
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
