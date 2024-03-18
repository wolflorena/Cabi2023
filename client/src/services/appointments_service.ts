const API_URL = "http://localhost:9090/api/appointments";

async function createAppointment(customerId: number) {
  let response;
  let connectionError = false;
  try {
    response = await fetch(`${API_URL}/create?customerId=${customerId}`, {
      method: "POST",
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

async function getAll() {
  const response = await fetch(`${API_URL}/getAll`);
  const json = await response.json();
  return json;
}

async function getAppointmentsByDateAndDoctor(date: string, doctorId: number) {
  const response = await fetch(
    `${API_URL}/getAllByDateAndDoctor?date=${date}&doctorId=${doctorId}`
  );
  const json = await response.json();
  return json;
}

export { getAppointmentsByDateAndDoctor, getAll, createAppointment };
