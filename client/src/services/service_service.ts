const API_URL = "http://localhost:9090/api/services";

async function getService(serviceId: number) {
  const response = await fetch(`${API_URL}/getService?serviceId=${serviceId}`);
  const json = await response.json();
  return json;
}

async function getAllServices() {
  const response = await fetch(`${API_URL}/getAll`);
  const json = await response.json();
  return json;
}

export { getService, getAllServices };
