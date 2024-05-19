<script setup lang="ts">
import AvatarImage from "@/components/AvatarImage.vue";
import InfoField from "@/components/InfoField.vue";
</script>

<template>
  <div class="view-profile-container">
    <div class="avatar-container">
      <AvatarImage src="../../../assets/default-avatar.png" alt="" />
    </div>
    <div class="fields">
      <InfoField
        uuid="FirstName"
        label="First Name"
        inputValue="ValueOfLoggedInUserFirstName"
        :isReadonly="true"
        type="text"
      />
      <InfoField
        uuid="Last Name"
        label="Last Name"
        inputValue="ValueOfLoggedInUserLastName"
        :isReadonly="true"
        type="text"
      />
      <InfoField
        uuid="Email"
        label="Email"
        inputValue="ValueOfLoggedInUserEmail"
        :isReadonly="true"
        type="text"
      />
      <InfoField
        uuid="PhoneNo"
        label="Phone Number"
        inputValue="ValueOfLoggedInUserphonenumber"
        :isReadonly="true"
        type="text"
      />
    </div>
    <div class="bottom-fields">
      <InfoField
        uuid="DateOfBirth"
        label="Date of Birth"
        inputValue="ValueOfLoggedInUserBirthName"
        :isReadonly="true"
        type="text"
      />
      <InfoField
        uuid="Occupation"
        label="Occupation"
        inputValue="ValueOfLoggedInUserOccupation"
        :isReadonly="true"
        type="text"
      />
    </div>
  </div>
</template>

<style lang="less" scoped>
.view-profile-container {
  display: flex;
  flex-direction: column;
  width: 70vw;
  height: auto;
  .avatar-container {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 25vh;
    width: 100%;
  }

  .fields {
    display: grid;
    grid-template-columns: auto auto;
    gap: 40px;
    margin: 20px;
  }
  .bottom-fields {
    display: grid;
    grid-template-columns: auto;
    gap: 40px;
    margin: 20px;
  }
}
</style>
