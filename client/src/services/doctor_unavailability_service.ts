const API_URL = "http://localhost:9090/api/unavailability";

async function createUnavailability(
  startDate: string,
  endDate: string,
  doctorId: number,
  reason: string,
  startTime?: string,
  endTime?: string
) {
  let response;
  let connectionError = false;
  try {
    response = await fetch(`${API_URL}`, {
      method: "POST",
      body: JSON.stringify({
        startDate,
        endDate,
        startTime,
        endTime,
        doctorId,
        reason,
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

async function getVacationsForCalendar() {
  const response = await fetch(`${API_URL}/forCalendar`);
  const json = await response.json();
  return json;
}
export { createUnavailability, getVacationsForCalendar };
