<script setup lang="ts">
import CustomButton from "@/components/CustomButton.vue";
import InfoField from "@/components/InfoField.vue";
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
        inputValue="Password"
        :isReadonly="false"
        type="text"
        variant="ACCOUNT_INFORMATION"
      />
      <InfoField
        uuid="newPassword"
        label="New Password"
        inputValue="pass"
        :isReadonly="false"
        type="text"
        variant="ACCOUNT_INFORMATION"
      />
      <InfoField
        uuid="confirmPassword"
        label="Confirm Password"
        inputValue="passsrd"
        :isReadonly="false"
        type="text"
        variant="ACCOUNT_INFORMATION"
      />
      <CustomButton
        :isMain="false"
        text="Save Changes"
        width="240"
        height="60"
        fontSize="18"
        :style="{
          alignSelf: 'flex-end',
        }"
      />
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
        />
        <CustomButton
          :isMain="true"
          text="Delete Account"
          width="240"
          height="60"
          fontSize="18"
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
