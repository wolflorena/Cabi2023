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

export { addAdmin };
