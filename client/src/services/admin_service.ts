const API_URL = "http://localhost:9090/api/admin";

async function addAdmin(email: string, hashPassword: string) {
  let response;
  let connectionError = false;
  try {
    response = await fetch(`${API_URL}?email=${email}`, {
      method: "POST",
      body: JSON.stringify({
        password: hashPassword,
      }),
    });
  } catch (error) {
    connectionError = true;
  }

  if (connectionError) {
    throw new Error("Server connection error");
  }

  const json = await response?.json();
  if (response && !response.ok) {
    throw new Error(json.message);
  }

  return json;
}

async function loginAdmin(email: string, hashPassword: string) {
  let connectionError = false;
  let response;
  try {
    response = await fetch(`${API_URL}/login`, {
      method: "POST",
      body: JSON.stringify({
        email: email,
        password: hashPassword,
      }),
    });
  } catch (error) {
    connectionError = true;
  }

  if (connectionError) {
    throw new Error("Server connection error");
  }

  const json = await response?.json();

  if (!response?.ok) {
    throw new Error("Invalid email or password");
  } else {
    localStorage.setItem("email", json.email);
  }
  return json;
}

export { addAdmin, loginAdmin };
