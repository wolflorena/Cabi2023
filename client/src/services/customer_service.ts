import { ChangePasswordBody, UserDetails } from "@/data/types/Entities";
import { format, parseISO } from "date-fns";

const API_URL = "http://localhost:9090/api/customers";

async function getAllPatients() {
  const response = await fetch(`${API_URL}/getAll`);

  const json = await response.json();
  return json;
}

async function getAllPageable(pageSize: number, pageNumber: number) {
  const response = await fetch(
    `${API_URL}/allPage?pageSize=${pageSize}&pageNumber=${pageNumber}`
  );

  const json = await response.json();
  return json;
}

async function getAllPatientsForDoctor(
  pageSize: number,
  pageNumber: number,
  doctorId: number
) {
  const response = await fetch(
    `${API_URL}/allForDoctor?pageSize=${pageSize}&pageNumber=${pageNumber}&doctorId=${doctorId}`
  );

  const json = await response.json();
  return json;
}

async function getById(userId: number, token: string) {
  const response = await fetch(`${API_URL}/get?id=${userId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });
  if (!response.ok) {
    throw new Error("Failed to fetch user data");
  }

  return response.json();
}

async function getByIdForAdmin(userId: number) {
  const response = await fetch(`${API_URL}/get?id=${userId}`);

  if (!response.ok) {
    throw new Error("Failed to fetch user data");
  }

  return response.json();
}

async function editStatus(userId: number, newStatusAccount: string) {
  const response = await fetch(
    `${API_URL}/accountStatus?customerId=${userId}&status=${newStatusAccount}`,
    {
      method: "PUT",
    }
  );
  const json = await response.json();
  return json;
}

async function editUserDetails(
  userId: number,
  token: string,
  editedUserDetails: UserDetails
): Promise<UserDetails> {
  const response = await fetch(`${API_URL}/edit?customerId=${userId}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      ...editedUserDetails,
      dateOfBirth: format(editedUserDetails.dateOfBirth, "yyyy-MM-dd"),
    }),
  });
  if (!response.ok) {
    throw new Error("Failed to edit user data");
  }
  const user: UserDetails = await response.json();

  return user;
}

async function uploadAvatar(userId: number, token: string, avatar: File) {
  const formData = new FormData();
  formData.append("avatar", avatar);
  const response = await fetch(
    `${API_URL}/upload-avatar?customerId=${userId}`,
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: formData,
    }
  );

  if (!response.ok) {
    throw new Error("Failed to upload avatar");
  }
}

async function getAvatar(userId: number, token: string) {
  const response = await fetch(`${API_URL}/avatar?customerId=${userId}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw new Error("Failed to retrieve avatar");
  }

  return response.blob();
}

async function changePassword(
  userId: number,
  token: string,
  changePasswordBody: ChangePasswordBody
) {
  const response = await fetch(
    `${API_URL}/change-password?customerId=${userId}`,
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(changePasswordBody),
    }
  );
  let responseData;
  if (!response.ok) {
    responseData = await response.json();
    throw new Error(responseData.message);
  }
}

async function deactivateAccount(userId: number, token: string) {
  const response = await fetch(
    `${API_URL}/deactivate-account?customerId=${userId}`,
    {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    }
  );

  if (!response.ok) {
    throw new Error("Failed to deactivate account");
  }
}

async function deleteAccount(userId: number, token: string) {
  const response = await fetch(
    `${API_URL}/delete-account?customerId=${userId}`,
    {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    }
  );

  if (!response.ok) {
    throw new Error("Failed to deactivate account");
  }
}

export {
  getAllPageable,
  getById,
  editStatus,
  getAllPatients,
  editUserDetails,
  uploadAvatar,
  getAvatar,
  changePassword,
  deactivateAccount,
  deleteAccount,
  getAllPatientsForDoctor,
  getByIdForAdmin,
};
