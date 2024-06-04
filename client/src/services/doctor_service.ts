const API_URL = "http://localhost:9090/api/doctors";

async function getAllDoctors() {
  const response = await fetch(`${API_URL}/getAll`);
  const json = await response.json();
  return json;
}

async function getDoctorById(doctorId: number) {
  const response = await fetch(`${API_URL}/get?doctorId=${doctorId}`);
  const json = await response.json();
  return json;
}

async function getAvailableDates(doctorId: number, serviceId: number) {
  const response = await fetch(
    `${API_URL}/available-dates?doctorId=${doctorId}&serviceId=${serviceId}`
  );
  const json = await response.json();
  console.log("j" + json);

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

async function updateDoctor(
  doctorId: number,
  firstName: string,
  lastName: string,
  email: string,
  phoneNumber: string,
  address: string,
  dateOfEmployment: string
) {
  const response = await fetch(
    `${API_URL}/update-doctor?doctorId=${doctorId}`,
    {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        firstName,
        lastName,
        email,
        phoneNo: phoneNumber,
        address,
        dateOfEmployment,
      }),
    }
  );
}

async function getServicesInCurrentMonth(doctorId: number) {
  const response = await fetch(
    `${API_URL}/services/current-month?doctorId=${doctorId}`
  );
  const json = await response.json();
  return json;
}

export {
  getAllDoctors,
  getDoctorById,
  getAvailableDates,
  getAvailableHours,
  updateDoctor,
  getServicesInCurrentMonth,
};
