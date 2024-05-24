import { UserDetails } from "@/data/types/Entities";
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

async function getById(userId: number, token: string): Promise<UserDetails> {
  const response = await fetch(`${API_URL}/get?id=${userId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });
  if (!response.ok) {
    throw new Error("Failed to fetch user data");
  }
  console.log(response.json);

  const user: UserDetails = await response.json();
  return user;
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

export { getAllPageable, getById, editStatus, getAllPatients, editUserDetails };
