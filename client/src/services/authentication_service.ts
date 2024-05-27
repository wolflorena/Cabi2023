import { jwtPayload } from "@/data/types/Entities";
import { parseJSON } from "date-fns";
import { jwtDecode } from "jwt-decode";

const API_URL = "http://localhost:9090/api/auth";

export async function loginService(email: string, password: string) {
  const response = await fetch(`${API_URL}/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email, password }),
  });

  let responseData;
  if (!response.ok) {
    responseData = await response.json();
    throw new Error(responseData.message);
  }
  return response;
}

export function getUserIdAndToken(): { userId: number; token: string } {
  const token = localStorage.getItem("jwtToken");
  if (!token) {
    throw new Error("No JWT token found");
  }
  const decodedToken: jwtPayload = jwtDecode(token);
  if (decodedToken.user_id != null) {
    const userId = decodedToken.user_id;
    return { userId, token };
  } else {
    throw new Error("The token doesn't have an user_id");
  }
}
