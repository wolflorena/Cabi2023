<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import TableHeaderButton from "@/components/TableHeaderButton.vue";
import { CustomerSidebarOptions } from "@/data/types/SidebarOptions";
import { computed, onMounted, ref } from "vue";
import ViewProfile from "./profile-content/ViewProfile.vue";
import EditProfile from "./profile-content/EditProfile.vue";
import SecuritySettings from "./profile-content/SecuritySettings.vue";
import { jwtDecode } from "jwt-decode";
import { LoadingState, UserDetails, jwtPayload } from "@/data/types/Entities";
import { getById } from "@/services/customer_service";
import { getUserIdAndToken } from "@/services/authentication_service";

const profilePage = ref("viewProfile");

const isActivePage = (page: string) =>
  computed(() => profilePage.value === page);

const handleClick = (page: string) => {
  profilePage.value = page;
  console.log(isActivePage("viewProfile").value);
};

const userDetails = ref<UserDetails>({
  firstName: "string",
  lastName: "string",
  email: "string",
  phoneNo: "string",
  dateOfBirth: "string",
  occupation: "string",
});
const isLoading = ref<boolean>(false);

async function loadUserDetails() {
  try {
    const { userId, token } = getUserIdAndToken();
    isLoading.value = true;
    await getById(userId, token).then((resp) => {
      if (resp) {
        userDetails.value = resp;
        isLoading.value = false;
        console.log(userDetails.value);
      }
    });
  } catch (err) {
    throw new Error("Failed to load user profile: " + err);
  }
}

function handleUserDetailsUpdate(editedUserDetails: UserDetails) {
  userDetails.value = editedUserDetails;
}

onMounted(() => {
  loadUserDetails();
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
      <div v-if="isLoading">LOADING....</div>
      <div v-else class="profile-content">
        <ViewProfile
          v-if="isActivePage('viewProfile').value"
          :userDetails="userDetails"
        />

        <EditProfile
          v-if="isActivePage('editProfile').value"
          :userDetails="userDetails"
          @updateUserDetails="handleUserDetailsUpdate"
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
