import { Appointment, AppointmentCalendar } from "@/data/types/Entities";
import { getHistoryOfCustomer } from "@/services/appointments_service";
import { getUserIdAndToken } from "@/services/authentication_service";
import { ref } from "vue";

const userHistoryAppointments = ref<AppointmentCalendar[]>([]);

async function fetchUserHistoryAppointments(
  pageNumber: number
): Promise<AppointmentCalendar[]> {
  const { userId, token } = getUserIdAndToken();
  try {
    const response: any = await getHistoryOfCustomer(
      token,
      userId,
      10,
      pageNumber
    );
    if (response) {
      const appointments: AppointmentCalendar[] =
        response.pagedAppointments.content;
      console.log("APPOINTMENTS" + appointments);
      if (appointments.length > 0) {
        userHistoryAppointments.value = appointments;
      }
    }
  } catch (error: any) {
    throw Error(error.message);
  }
  return [];
}

export function useUserAppointments() {
  return {
    userHistoryAppointments,
    fetchUserHistoryAppointments,
  };
}
