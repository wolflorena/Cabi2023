const API_URL = "http://localhost:9090/api/form";

async function addForm(
  title: string,
  description: string,
  visibility: boolean
) {
  let response;
  let connectionError = false;
  try {
    response = await fetch(`${API_URL}`, {
      method: "POST",
      body: JSON.stringify({
        title,
        description,
        visibility,
      }),
      headers: {
        "Content-type": "application/json; charset=UTF-8",
      },
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

async function getAllForms() {
  const response = await fetch(`${API_URL}/all`);

  const json = await response.json();
  return json;
}

export { getAllForms, addForm };
