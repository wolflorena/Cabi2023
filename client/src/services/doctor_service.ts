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

async function getDoctorAvatarById(doctorId: number) {
  const response = await fetch(`${API_URL}/getAvatar?doctorId=${doctorId}`);
  return response.blob();
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

async function uploadAvatar(doctorId: number, avatar: File) {
  const formData = new FormData();
  formData.append("avatar", avatar);
  const response = await fetch(
    `${API_URL}/upload-avatar?doctorId=${doctorId}`,
    {
      method: "POST",
      body: formData,
    }
  );

  if (!response.ok) {
    throw new Error("Failed to upload avatar");
  }
}

async function changeDoctorPassword(
  doctorId: number,
  currentPassword: string,
  newPassword: string
) {
  const response = await fetch(
    `${API_URL}/change-password?doctorId=${doctorId}`,
    {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        currentPassword,
        newPassword,
      }),
    }
  );

  if (!response.ok) {
    const errorMessage = await response.json();
    throw new Error(errorMessage.message);
  }

  return response.json();
}

async function getServicesInCurrentMonth(doctorId: number) {
  const response = await fetch(
    `${API_URL}/services/current-month?doctorId=${doctorId}`
  );
  const json = await response.json();
  return json;
}

async function createDoctorAccount(
  email: string,
  firstName: string,
  lastName: string
) {
  let response;
  let connectionError = false;
  try {
    response = await fetch(`${API_URL}/addDoctor`, {
      method: "POST",
      body: JSON.stringify({
        firstName,
        lastName,
        email,
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

export {
  getDoctorAvatarById,
  getAllDoctors,
  getDoctorById,
  getAvailableDates,
  getAvailableHours,
  updateDoctor,
  getServicesInCurrentMonth,
  createDoctorAccount,
  changeDoctorPassword,
  uploadAvatar,
};
