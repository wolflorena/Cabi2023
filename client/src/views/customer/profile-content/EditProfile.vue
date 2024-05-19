<script setup lang="ts">
import AvatarImage from "@/components/AvatarImage.vue";
import CustomButton from "@/components/CustomButton.vue";
import InfoField from "@/components/InfoField.vue";
import { UserDetails } from "@/data/types/Entities";
import { Ref, computed, ref, watch } from "vue";

//after login i should populate the userDetails with the user details information.
const userProfile: Ref<UserDetails> = ref<UserDetails>({
  firstName: "Iulian",
  secondName: "Alex",
  email: "Iulian-Alex@yahoo.com",
  phoneNo: "0720301853",
  dateOfBirth: "12.05.2001",
  occupation: "Student",
});
</script>

<template>
  <div class="view-profile-container">
    <div class="avatar-settings-container">
      <div class="avatar-settings">
        <div class="top-side">
          <AvatarImage src="../../../assets/default-avatar.png" alt="" />

          <CustomButton
            text="Choose a file"
            :isMain="false"
            color="#30619b"
            font-size="18"
            height="40"
            :style="{
              width: '100%',
            }"
          />
          <div class="text">
            <span>Acceptable formats: .jpg, .png</span>
            <span>Max file size: 500kb</span>
          </div>
        </div>
        <div class="bottom-side">
          <CustomButton
            text="Save Changes"
            :isMain="false"
            height="60"
            font-size="18"
            :style="{
              width: '100%',
            }"
          />
        </div>
      </div>
    </div>
    <div class="fields-settings-container">
      <span
        :style="{
          fontSize: '28px',
          fontWeight: 'bold',
          color: '#43506c',
          margin: '0 0 38px 8px',
          alignSelf: 'flex-start',
        }"
        >ACCOUNT INFORMATION</span
      >
      <div class="info-fields-group">
        <InfoField
          uuid="Last Name"
          label="Last Name"
          :input-value="userProfile.firstName"
          :isReadonly="false"
          type="text"
          variant="ACCOUNT_INFORMATION"
        />
        <InfoField
          uuid="Second Name"
          label="Second Name"
          :input-value="userProfile.secondName"
          :isReadonly="false"
          type="text"
          variant="ACCOUNT_INFORMATION"
        />
        <InfoField
          uuid="Email"
          label="Email"
          :input-value="userProfile.email"
          :isReadonly="false"
          type="text"
          variant="ACCOUNT_INFORMATION"
        />
        <InfoField
          uuid="PhoneNumber"
          label="Phone Number"
          :input-value="userProfile.phoneNo"
          :isReadonly="false"
          type="text"
          variant="ACCOUNT_INFORMATION"
        />
        <InfoField
          uuid="DateOfBirth"
          label="Date of Birth"
          :input-value="userProfile.dateOfBirth"
          :isReadonly="false"
          type="text"
          variant="ACCOUNT_INFORMATION"
        />
        <InfoField
          uuid="Occupation"
          label="Occupation"
          :input-value="userProfile.occupation"
          :isReadonly="false"
          type="text"
          variant="ACCOUNT_INFORMATION"
        />
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
.view-profile-container {
  display: flex;
  width: 100%;
  height: 100%;
  .avatar-settings-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    height: 100%;
    width: 20vw;
    .avatar-settings {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: space-between;
      height: 100%;
      .top-side {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-top: 70px;
        gap: 15px;

        .text {
          display: flex;
          flex-direction: column;
          font-size: small;
          font-weight: bold;
        }
      }
    }

    .bottom-side {
      width: 100%;
      margin-bottom: 70px;
    }
  }

  .fields-settings-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 140px 20px 20px 40px;

    width: 50vw;
    .info-fields-group {
      display: flex;
      flex-direction: column;
      width: 100%;
      gap: 36px;
    }
    .info-field {
      width: 2vw;
    }
  }
}
</style>
