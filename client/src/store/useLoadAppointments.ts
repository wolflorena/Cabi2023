import {
  Appointment,
  AppointmentCalendar,
  Doctor,
  Vacation,
} from "@/data/types/Entities";
import { getDoctorAppointments } from "@/services/appointments_service";
import { getUserIdAndToken } from "@/services/authentication_service";
import { getAllDoctors } from "@/services/doctor_service";
import {
  getVacationsForCalendar,
  getVacationsForCalendarForYear,
} from "@/services/doctor_unavailability_service";
import { SwalLoading } from "@/utils/helpers";
import { formatISO } from "date-fns";
import { ref } from "vue";

const doctors = ref<Doctor[] | null>(null);
const selectedDoctor = ref<Doctor | null>(null);
const calendarDate = ref<Date>(new Date(Date.now()));
const calendarViewtype = ref<string>("WEEK");
const appointments = ref<AppointmentCalendar[]>([]);
const appointmentsCache = ref(new Map());
const unavailabilities = ref<Vacation[] | null>(null);

function handleClickOnCalendarButton(value: string) {
  calendarViewtype.value = value;
}

async function loadDoctors() {
  SwalLoading.fire();
  await getAllDoctors().then((res: any) => {
    doctors.value = res;
  });
  SwalLoading.close;
}

async function fetchAppointments(forceUpdate: boolean = false) {
  const { userId, token } = getUserIdAndToken();

  if (!selectedDoctor.value) return;

  const monthName = calendarDate.value.getMonth();

  const cacheKey = `${selectedDoctor.value.id}-month=${monthName}`;

  if (!forceUpdate && appointmentsCache.value.has(cacheKey)) {
    appointments.value = appointmentsCache.value.get(cacheKey);
  } else {
    const date = formatISO(calendarDate.value, { representation: "date" });
    SwalLoading.fire({
      titleText: `Waiting for appointments.`,
    });

    try {
      const [resAppointments, resUnavailabilities] = await Promise.all([
        getDoctorAppointments(token, selectedDoctor.value.id, date, "MONTH"),
        getVacationsForCalendarForYear(selectedDoctor.value.id),
      ]);

      if (resAppointments) {
        appointments.value = resAppointments;
        appointmentsCache.value.set(cacheKey, resAppointments);
      }

      if (resUnavailabilities) {
        unavailabilities.value = resUnavailabilities;
      }

      SwalLoading.close();
    } catch (error: any) {
      appointments.value = [];
      unavailabilities.value = [];
      SwalLoading.close();
    }
  }
}

export function useLoadAppointments() {
  return {
    doctors,
    calendarDate,
    calendarViewtype,
    selectedDoctor,
    appointments,
    appointmentsCache,
    unavailabilities,
    loadDoctors,
    handleClickOnCalendarButton,
    fetchAppointments,
  };
}
