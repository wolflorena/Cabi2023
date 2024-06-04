import { Appointment, Doctor } from "@/data/types/Entities";
import { getDoctorAppointments } from "@/services/appointments_service";
import { getUserIdAndToken } from "@/services/authentication_service";
import { getAllDoctors } from "@/services/doctor_service";
import { SwalLoading } from "@/utils/helpers";
import { formatISO } from "date-fns";
import { ref } from "vue";

const doctors = ref<Doctor[] | null>(null);
const selectedDoctor = ref<Doctor | null>(null);
const calendarDate = ref<Date>(new Date(Date.now()));
const calendarViewtype = ref<string>("WEEK");
const appointments = ref<Appointment[]>([]);
const appointmentsCache = ref(new Map());

const { userId, token } = getUserIdAndToken();

function handleClickOnCalendarButton(value: string) {
  calendarViewtype.value = value;
}

async function loadDoctors() {
  SwalLoading.fire();
  await getAllDoctors().then((res: any) => {
    doctors.value = res;
  });
  SwalLoading.close();
}

async function fetchAppointments() {
  if (!selectedDoctor.value) return;

  const monthName = calendarDate.value.getMonth();

  const cacheKey = `${selectedDoctor.value.id}-month=${monthName}`;

  if (appointmentsCache.value.has(cacheKey)) {
    appointments.value = appointmentsCache.value.get(cacheKey);
  } else {
    const date = formatISO(calendarDate.value).split("T")[0];
    SwalLoading.fire({
      titleText: `Waiting for appointments.`,
    });

    try {
      const res = await getDoctorAppointments(
        token,
        selectedDoctor.value.id,
        date,
        "MONTH"
      );
      if (res) {
        appointments.value = res;

        appointmentsCache.value.set(cacheKey, res);
        SwalLoading.close();
      }
    } catch (error: any) {
      appointments.value = [];
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
    loadDoctors,
    handleClickOnCalendarButton,
    fetchAppointments,
  };
}
