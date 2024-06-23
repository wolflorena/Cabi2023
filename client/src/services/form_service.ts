import { Form, FormWithStatus } from "@/data/types/Entities";

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
  if (response && !response.ok) {
    throw new Error(json.message);
  }
  return json;
}

async function getForm(formId: number) {
  const response = await fetch(`${API_URL}?formId=${formId}`);

  const json = await response.json();
  if (response && !response.ok) {
    throw new Error(json.message);
  }
  return json;
}

async function deleteForm(formId: number) {
  const response = await fetch(API_URL + "?formId=" + formId, {
    method: "DELETE",
  });

  const json = await response.json();

  if (response && !response.ok) {
    throw new Error(json.message);
  }
  return json;
}

async function getFormEvents(formId: number) {
  const response = await fetch(API_URL + "/" + formId + "/customers");

  const json = await response.json();
  if (response && !response.ok) {
    throw new Error(json.message);
  }
  return json;
}

async function updateForm(
  formId: number,
  title: string,
  description: string,
  visibility: boolean
) {
  const response = await fetch(`${API_URL}/update-form?formId=${formId}`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      title,
      description,
      visibility,
    }),
  });

  if (!response.ok) {
    throw new Error("Failed to set form as viewed");
  }
}

export async function setViewedForm(formId: number, customerId: number) {
  const response = await fetch(
    `${API_URL}/formViewed?formId=${formId}&customerId=${customerId}`,
    {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
    }
  );

  if (!response.ok) {
    throw new Error("Failed to set form as viewed");
  }
}

export async function setSignedForm(formId: number, customerId: number) {
  const response = await fetch(
    `${API_URL}/formSigned?formId=${formId}&customerId=${customerId}`,
    {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
    }
  );

  if (!response.ok) {
    throw new Error("Failed to set form as viewed");
  }
}

async function getVisibleForms(
  token: string,
  customerId: number
): Promise<FormWithStatus[]> {
  const response = await fetch(
    `${API_URL}/getVisibleForms?customerId=${customerId}`,
    {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    }
  );

  return response.json();
}
export {
  getVisibleForms,
  getAllForms,
  addForm,
  getForm,
  deleteForm,
  getFormEvents,
  updateForm,
};
