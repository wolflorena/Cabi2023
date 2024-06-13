const API_URL = "http://localhost:9090/api/documents";

async function uploadFile(customerId: number, file: any) {
  const formData = new FormData();
  formData.append("customerId", customerId.toString());
  formData.append("file", file);

  let response;
  let connectionError = false;
  try {
    response = await fetch(`${API_URL}/upload`, {
      method: "POST",
      body: formData,
    });
  } catch (error) {
    connectionError = true;
  }

  if (connectionError) {
    throw new Error("Error uploading file.");
  }

  const json = await response?.json();
  if (response && !response.ok) {
    throw new Error(json.message);
  }

  return response;
}

async function getFilesByPatient(customerId: number) {
  const response = await fetch(`${API_URL}/patient/${customerId}`);

  const json = await response.json();
  return json;
}

export { uploadFile, getFilesByPatient };
