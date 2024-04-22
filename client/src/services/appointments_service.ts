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

async function getById(appointmentId: number) {
  const response = await fetch(`${API_URL}?appointmentId=${appointmentId}`);
  const json = await response.json();
  return json;
}

async function deleteAppointment(appointmentId: number) {
  const response = fetch(API_URL + "?appointmentId=" + appointmentId, {
    method: "DELETE",
  });
  return response;
}

async function getAll() {
  const response = await fetch(`${API_URL}/getAll`);
  const json = await response.json();
  return json;
}

async function getAllForCalendar() {
  const response = await fetch(`${API_URL}/getAllForCalendar`);
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

async function getAllPageable(pageSize: number, pageNumber: number) {
  const response = await fetch(
    `${API_URL}/allPage?pageSize=${pageSize}&pageNumber=${pageNumber}`
  );
  const json = await response.json();
  return json;
}

export {
  getAppointmentsByDateAndDoctor,
  getAll,
  createAppointment,
  getAllForCalendar,
  getAllPageable,
  getById,
  deleteAppointment,
};
