import { RegisterCustomerBody, jwtPayload } from "@/data/types/Entities";
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

export async function registerCustomer(
  registerCustomerBody: RegisterCustomerBody
) {
  const response = await fetch(`http://localhost:9090/api/customers/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(registerCustomerBody),
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
    // throw new Error("No JWT token found");
    return { userId: 1, token: "2" };
  }
  const decodedToken: jwtPayload = jwtDecode(token);
  if (decodedToken.user_id != null) {
    const userId = decodedToken.user_id;
    return { userId, token };
  } else {
    throw new Error("The token doesn't have an user_id");
  }
}

export function isTokenValid(token: string) {
  if (!token) {
    return false;
  }

  try {
    const decodedToken: jwtPayload = jwtDecode(token);
    const currentTime = Date.now() / 1000; // Convert to seconds
    return decodedToken.exp > currentTime;
  } catch (error) {
    return false;
  }
}

export function getUserRole() {
  const token = localStorage.getItem("jwtToken");
  if (token) {
    try {
      const decodedToken: jwtPayload = jwtDecode(token);
      return decodedToken.role;
    } catch (error) {
      return "none";
    }
  }
  return "none";
}

export function isAuthenticated() {
  const token = localStorage.getItem("jwtToken");
  return token && isTokenValid(token);
}
