<script setup lang="ts">
import AvatarImage from "@/components/AvatarImage.vue";
import InfoField from "@/components/InfoField.vue";
import { UserDetails } from "@/data/types/Entities";
import { ref } from "vue";
const props = defineProps<{
  userDetails: UserDetails | null;
  avatarImage?: string;
}>();
</script>

<template>
  <div class="view-profile-container">
    <div class="avatar-container">
      <AvatarImage :src="avatarImage" alt="" />
    </div>
    <div class="fields" v-if="userDetails !== null">
      <InfoField
        uuid="FirstName"
        label="First Name"
        :inputValue="userDetails.firstName"
        :isReadonly="true"
        type="text"
      />
      <InfoField
        uuid="SecondName"
        label="Second Name"
        :inputValue="userDetails.lastName"
        :isReadonly="true"
        type="text"
      />
      <InfoField
        uuid="Email"
        label="Email"
        :inputValue="userDetails.email"
        :isReadonly="true"
        type="text"
      />
      <InfoField
        uuid="PhoneNo"
        label="Phone Number"
        :inputValue="userDetails.phoneNo"
        :isReadonly="true"
        type="text"
      />
    </div>
    <div class="bottom-fields" v-if="userDetails">
      <InfoField
        uuid="DateOfBirth"
        label="Date of Birth"
        :inputValue="userDetails.dateOfBirth"
        :isReadonly="true"
        type="text"
      />
      <InfoField
        uuid="Occupation"
        label="Occupation"
        :inputValue="userDetails.occupation"
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
