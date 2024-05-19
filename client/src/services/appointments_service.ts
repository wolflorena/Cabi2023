const API_URL = "http://localhost:9090/api/appointments";

async function createAppointment(
  date: string,
  time: string,
  doctorId: number,
  serviceId: number,
  customerId?: number
) {
  let response;
  let connectionError = false;
  try {
    response = await fetch(`${API_URL}/create`, {
      method: "POST",
      body: JSON.stringify({
        date,
        time,
        doctorId,
        serviceId,
        customerId,
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

async function getAllPageable(
  pageSize: number,
  pageNumber: number,
  doctorIds: number[],
  status: string
) {
  let doctorIdsString: string = doctorIds.join(",");

  const response = await fetch(
    `${API_URL}/allPage?pageSize=${pageSize}&pageNumber=${pageNumber}&doctorIds=${doctorIdsString}&status=${status}`
  );

  const json = await response.json();
  return json;
}

async function updateStatus(appointmentId: number, status: string) {
  const response = fetch(
    API_URL + "/status?appointmentId=" + appointmentId + "&status=" + status,
    {
      method: "PUT",
    }
  );

  return response;
}

async function getUpcomingAppointments(doctorId: number) {
  const response = await fetch(API_URL + "/nextByDoctor?doctorId=" + doctorId);

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
  updateStatus,
  getUpcomingAppointments,
};
