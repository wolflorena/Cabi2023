import { ref } from "vue";
import { Appointment, UserDetails } from "@/data/types/Entities";
import { getAvatar, getById } from "@/services/customer_service";
import { getUserIdAndToken } from "@/services/authentication_service";

const userProfile = ref<UserDetails | null>(null);
const avatarImage = ref<string | null>(null);
const userHistory = ref<Appointment[] | null>(null);

async function fetchUserProfile() {
  if (!userProfile.value) {
    try {
      const { userId, token } = getUserIdAndToken();
      const resp = await getById(userId, token);
      if (resp) {
        userProfile.value = resp;
      }
    } catch (err) {
      throw new Error("Failed to load user profile: " + err);
    }
  }
}

async function retrieveUserAvatar() {
  if (!avatarImage.value) {
    const { userId, token } = getUserIdAndToken();
    const blob = await getAvatar(userId, token);

    if (blob.size > 0) {
      avatarImage.value = URL.createObjectURL(blob);
    } else {
      avatarImage.value = null;
    }
  }
}

function updateUserProfile(editedUserProfile: UserDetails) {
  userProfile.value = editedUserProfile;
}

function updateAvatarImage(newAvatarImage: string) {
  avatarImage.value = newAvatarImage;
}

export function useUserProfile() {
  return {
    userDetails: userProfile,
    avatarImage,
    fetchUserProfile,
    retrieveUserAvatar,
    updateUserProfile,
    updateAvatarImage,
  };
}
