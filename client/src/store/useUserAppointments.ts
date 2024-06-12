import {
  Appointment,
  AppointmentCalendar,
  HistoryAppointmentCalendar,
} from "@/data/types/Entities";
import { getHistoryOfCustomer } from "@/services/appointments_service";
import { getUserIdAndToken } from "@/services/authentication_service";
import { ref } from "vue";

const userHistoryAppointments = ref<HistoryAppointmentCalendar[]>([]);
const isLoading = ref<boolean>(false);
const totalPages = ref<number>(0);
const appointmentsCache = ref(new Map());

async function fetchUserHistoryAppointments(
  pageNumber: number,
  forceUpdate: boolean = false
): Promise<HistoryAppointmentCalendar[]> {
  const { userId, token } = getUserIdAndToken();
  isLoading.value = true;
  const cacheKey = `${userId}-page=${pageNumber}`;

  if (forceUpdate) {
    appointmentsCache.value = new Map();
  }
  if (appointmentsCache.value.has(cacheKey)) {
    const cachedData = appointmentsCache.value.get(cacheKey);
    userHistoryAppointments.value = cachedData.appointments;
    totalPages.value = cachedData.totalPages;
    isLoading.value = false;
    return cachedData.appointments;
  }
  try {
    const response: any = await getHistoryOfCustomer(
      token,
      userId,
      10,
      pageNumber - 1
    );
    if (response) {
      const appointments: HistoryAppointmentCalendar[] =
        response.pagedAppointments.content;
      if (appointments.length > 0) {
        userHistoryAppointments.value = appointments;
        totalPages.value = Math.ceil(response.total / 10);

        appointmentsCache.value.set(cacheKey, {
          appointments,
          totalPages: totalPages.value,
        });
        isLoading.value = false;
      }
    }
  } catch (error: any) {
    throw Error(error.message);
  }
  return [];
}

function changePage(pageNumber: number) {
  isLoading.value = true;
  console.log("page nmbr" + pageNumber);

  fetchUserHistoryAppointments(pageNumber);
}

export function useUserAppointments() {
  return {
    userHistoryAppointments,
    isLoading,
    totalPages,
    changePage,
    fetchUserHistoryAppointments,
  };
}
