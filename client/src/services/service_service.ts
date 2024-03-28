const API_URL = "http://localhost:9090/api/services";

async function getService(serviceId: number) {
  const response = await fetch(`${API_URL}/getService`);
  const json = await response.json();
  return json;
}

export { getService };
