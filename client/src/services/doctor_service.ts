const API_URL = "http://localhost:9090/api/doctors";

async function getAllDoctors() {
  const response = await fetch(`${API_URL}/getAll`);
  const json = await response.json();
  return json;
}

export { getAllDoctors };
