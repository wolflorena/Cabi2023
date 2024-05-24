<script setup lang="ts">
import AvatarImage from "@/components/AvatarImage.vue";
import CustomButton from "@/components/CustomButton.vue";
import InfoField from "@/components/InfoField.vue";
import { UserDetails } from "@/data/types/Entities";
import { getUserIdAndToken } from "@/services/authentication_service";
import {
  editUserDetails,
  getAvatar,
  uploadAvatar,
} from "@/services/customer_service";
import { onMounted, ref } from "vue";
const props = defineProps<{
  userDetails: UserDetails;
  avatarImage?: string;
}>();

const emits = defineEmits<{
  updateUserDetails: [newUserDetails: UserDetails];
  updateAvatarImage: [newAvatarImage: string];
}>();

const editedUserDetails = ref<UserDetails>(props.userDetails);
const avatarFile = ref<File | undefined>();
const avatarPreview = ref<string | undefined>(props.avatarImage);
console.log(props.avatarImage);

function handleFileChange(event: Event) {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files[0]) {
    const file = target.files[0];
    if (file.size > 500 * 1024) {
      alert("File size exceeds 500KB");
      return;
    }
    const fileReader = new FileReader();
    fileReader.onload = () => {
      avatarPreview.value = fileReader.result as string;
    };
    fileReader.readAsDataURL(file);
    avatarFile.value = file;
  }
}

function triggerFileInput() {
  const fileInput = document.getElementById("fileInput");
  if (fileInput) {
    fileInput.click();
  }
}

async function handleSaveChanges() {
  const { userId, token } = getUserIdAndToken();
  if (avatarFile.value) {
    await uploadAvatar(userId, token, avatarFile.value);
  }
  const newUserDetails = await editUserDetails(
    userId,
    token,
    editedUserDetails.value
  );

  emits("updateUserDetails", newUserDetails);
  if (avatarPreview.value !== props.avatarImage && avatarPreview.value) {
    emits("updateAvatarImage", avatarPreview.value);
  }
}
</script>

<template>
  <div class="view-profile-container">
    <div class="avatar-settings-container">
      <div class="avatar-settings">
        <div class="top-side">
          <AvatarImage :src="avatarPreview" alt="" />
          <input
            type="file"
            id="fileInput"
            @change="handleFileChange"
            accept=".jpg,.png"
            style="display: none"
          />
          <CustomButton
            type="input"
            text="Choose a file"
            :isMain="false"
            color="#30619b"
            font-size="18"
            height="40"
            :style="{
              width: '100%',
            }"
            @click="triggerFileInput"
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
            @click="handleSaveChanges"
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
          uuid="First Name"
          label="First Name"
          v-model:inputValue="editedUserDetails.firstName"
          :isReadonly="false"
          type="text"
          variant="ACCOUNT_INFORMATION"
        />
        <InfoField
          uuid="Last Name"
          label="Last Name"
          v-model:inputValue="editedUserDetails.lastName"
          :isReadonly="false"
          type="text"
          variant="ACCOUNT_INFORMATION"
        />
        <InfoField
          uuid="Email"
          label="Email"
          v-model:input-value="editedUserDetails.email"
          :isReadonly="false"
          type="text"
          variant="ACCOUNT_INFORMATION"
        />
        <InfoField
          uuid="PhoneNumber"
          label="Phone Number"
          v-model:input-value="editedUserDetails.phoneNo"
          :isReadonly="false"
          type="phoneNo"
          variant="ACCOUNT_INFORMATION"
        />
        <InfoField
          uuid="DateOfBirth"
          label="Date of Birth"
          v-model:input-value="editedUserDetails.dateOfBirth"
          :isReadonly="false"
          type="date"
          variant="ACCOUNT_INFORMATION"
        />
        <InfoField
          uuid="Occupation"
          label="Occupation"
          v-model:input-value="editedUserDetails.occupation"
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
