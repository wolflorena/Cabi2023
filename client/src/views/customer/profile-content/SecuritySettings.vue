<script setup lang="ts">
import CustomButton from "@/components/CustomButton.vue";
import InfoField from "@/components/InfoField.vue";
import { ChangePasswordBody } from "@/data/types/Entities";
import { getUserIdAndToken } from "@/services/authentication_service";
import {
  changePassword,
  deactivateAccount,
  deleteAccount,
} from "@/services/customer_service";
import Swal from "sweetalert2";
import { computed, ref, watch } from "vue";
import { useRouter } from "vue-router";

const currentPassword = ref<string>("");
const newPassword = ref<string>("");
const confirmPassword = ref<string>("");

const infoMessage = ref<string>("");
const isTyping = ref<boolean>(true);
const isSuccessful = ref<boolean>(false);

const router = useRouter();
const infoMessageVisible = computed(() => {
  if (isTyping.value) {
    return false;
  } else {
    return true;
  }
});
const passwordsMatch = computed(() => {
  return newPassword.value === confirmPassword.value;
});

async function handleDeactivateAccount() {
  Swal.fire({
    titleText: "You really want to deactivate your account",
    icon: "question",
    confirmButtonText: "Yes, I do.",
  }).then(async (result) => {
    if (result.isConfirmed) {
      try {
        const { userId, token } = getUserIdAndToken();
        const result = await deactivateAccount(userId, token);
      } catch (error) {
        console.error("Error deactivating account", error);
      }
      router.push("/login");
    }
  });
}

async function handleDeleteAccount() {
  Swal.fire({
    titleText: "Do you really want to delete your account",
    icon: "question",
    confirmButtonText: "Yes, I do",
    cancelButtonText: "Nooo!",
    showCancelButton: true,
  }).then(async (result) => {
    if (result.isConfirmed) {
      try {
        const { userId, token } = getUserIdAndToken();
        const result = await deleteAccount(userId, token);
      } catch (error) {
        console.error("Error deleting account", error);
      }
      router.push("/login");
    }
  });
}

async function handlePasswordChange() {
  isTyping.value = false;

  if (!currentPassword.value || !newPassword.value || !confirmPassword.value) {
    infoMessage.value = "All fields are required.";
    return;
  }

  if (!passwordsMatch.value) {
    infoMessage.value = "New password do not match.";
    return;
  }

  const changePasswordBody: ChangePasswordBody = {
    currentPassword: currentPassword.value,
    newPassword: newPassword.value,
  };
  try {
    const { userId, token } = getUserIdAndToken();
    const result = await changePassword(userId, token, changePasswordBody);
    infoMessage.value = "Password changed successfully.";
    Swal.fire({
      titleText: "Password changed successfully",
      icon: "success",
    });
    isTyping.value = false;
    isSuccessful.value = true;
    currentPassword.value = "";
    newPassword.value = "";
    confirmPassword.value = "";
  } catch (error: any) {
    infoMessage.value =
      "Current password is incorrect or failed to change password(CheckComplexity).";
    console.error("Error changing password:", error.message);
  }
}

function handleFocus() {
  isTyping.value = true;
  isSuccessful.value = false;
  infoMessage.value = "";
}

function handleBlur() {
  isTyping.value = false;
}

const infoMessageClass = computed(() => {
  if (isSuccessful.value) {
    return "success";
  } else {
    return "error";
  }
});
</script>
<template>
  <div class="security-settings-container">
    <span
      :style="{
        fontSize: '28px',
        fontWeight: 'bold',
        color: '#43506c',
        margin: '0 0 38px 0px',
        alignSelf: 'flex-start',
      }"
      >CHANGE PASSWORD</span
    >
    <div class="info-fields-group">
      <InfoField
        uuid="currentPassword"
        label="Current Password"
        v-model:inputValue="currentPassword"
        :isReadonly="false"
        type="password"
        variant="ACCOUNT_INFORMATION"
        @onFocus="handleFocus"
        @onBlur="handleBlur"
      />
      <InfoField
        uuid="newPassword"
        label="New Password"
        v-model:inputValue="newPassword"
        :isReadonly="false"
        type="password"
        variant="ACCOUNT_INFORMATION"
        @onFocus="handleFocus"
        @onBlur="handleBlur"
      />
      <InfoField
        uuid="confirmPassword"
        label="Confirm Password"
        v-model:inputValue="confirmPassword"
        :isReadonly="false"
        type="password"
        variant="ACCOUNT_INFORMATION"
        @onFocus="handleFocus"
        @onBlur="handleBlur"
      />
      <div class="info">
        <span
          class="info-message"
          :class="infoMessageClass"
          v-if="infoMessageVisible && infoMessage"
        >
          <font-awesome-icon
            icon="circle-info"
            id="info-icon"
            class="info-icon"
          />
          <span> {{ infoMessage }}</span>
        </span>
        <span v-else> </span>
        <CustomButton
          :isMain="false"
          text="Save Changes"
          width="240"
          height="60"
          fontSize="18"
          :style="{
            alignSelf: 'flex-end',
          }"
          @click="handlePasswordChange"
        />
      </div>
    </div>
    <div class="bottom-actions">
      <span
        :style="{
          fontSize: '28px',
          fontWeight: 'bold',
          color: '#43506c',
          margin: '0 0 38px 0px',
          alignSelf: 'flex-start',
        }"
        >ACCOUNT ACTIONS</span
      >
      <div class="bottom-buttons">
        <CustomButton
          :isMain="false"
          text="Deactivate Account"
          width="240"
          height="60"
          fontSize="18"
          @click="handleDeactivateAccount"
        />
        <CustomButton
          :isMain="true"
          text="Delete Account"
          width="240"
          height="60"
          fontSize="18"
          @click="handleDeleteAccount"
        />
      </div>
    </div>
  </div>
</template>

<style lang="less">
.security-settings-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  margin: 86px 12px 0px 10vw;
  .info {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .info-message {
      display: flex;
      align-items: center;
      &.error {
        color: red;
        font-weight: bold;
        .info-icon {
          color: red;
          margin: 0px 15px;
        }
      }
      &.success {
        color: green;
        font-weight: bold;
        padding-left: 5px;
        .info-icon {
          color: green;
          margin: 0px 10px;
        }
      }
    }
  }

  .info-fields-group {
    display: flex;
    flex-direction: column;
    gap: 36px;
    width: 60vw;
  }

  .bottom-actions {
    width: 60vw;
    height: 100%;
    display: flex;
    flex-direction: column;
    margin-top: 140px;

    .bottom-buttons {
      display: flex;
      justify-content: space-between;
    }
  }
}
</style>
