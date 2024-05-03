const API_URL = "http://localhost:9090/api/customers";

async function getAllPageable(pageSize: number, pageNumber: number) {
  const response = await fetch(
    `${API_URL}/allPage?pageSize=${pageSize}&pageNumber=${pageNumber}`
  );

  const json = await response.json();
  return json;
}

async function getById(patientId: number) {
  const response = await fetch(`${API_URL}/get?id=${patientId}`);
  const json = await response.json();
  return json;
}

async function editStatus(patientId: number, newStatusAccount: string) {
  const response = await fetch(
    `${API_URL}/accountStatus?customerId=${patientId}&status=${newStatusAccount}`,
    {
      method: "PUT",
    }
  );
  const json = await response.json();
  return json;
}

export { getAllPageable, getById, editStatus };
