const API_URL = "http://localhost:9090/api/doctors";

async function getAllDoctors() {
  const response = await fetch(`${API_URL}/getAll`);
  const json = await response.json();
  return json;
}

async function getAvailableDates(doctorId: number, serviceId: number) {
  const response = await fetch(
    `${API_URL}/available-dates?doctorId=${doctorId}&serviceId=${serviceId}`
  );
  const json = await response.json();
  return json;
}

async function getAvailableHours(
  doctorId: number,
  serviceId: number,
  date: string
) {
  const response = await fetch(
    `${API_URL}/available-hours?doctorId=${doctorId}&serviceId=${serviceId}&date=${date}`
  );
  const json = await response.json();
  return json;
}

export { getAllDoctors, getAvailableDates, getAvailableHours };
