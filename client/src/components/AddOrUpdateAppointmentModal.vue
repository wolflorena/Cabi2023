<script setup lang="ts">
import { nextTick, onMounted, ref, watch } from "vue";
import type { Patient, SelectedDoctor, Service } from "@/data/types/Entities";
import {
  getAllDoctors,
  getAvailableDates,
  getAvailableHours,
} from "@/services/doctor_service";
import { getAllServices } from "@/services/service_service";
import { getAllPatients } from "@/services/customer_service";
import { getById, updateAppointment } from "@/services/appointments_service";
import CustomModal from "./CustomModal.vue";
import Swal from "sweetalert2";
import LoadingSpinner from "./LoadingSpinner.vue";

const props = withDefaults(
  defineProps<{
    visible: boolean;
    appointmentId?: number; //only if the component is used for editing
  }>(),
  {
    visible: false,
  }
);

const emit = defineEmits(["close", "addAppointment", "updateSuccessfully"]);

const doctors = ref<SelectedDoctor[]>([]);

const patients = ref<Patient[]>([]);
const services = ref<Service[]>([]);

const selectedDoctor = ref();
const selectedService = ref();
const availableDates = ref<string[]>([]);
const selectedDate = ref("");
const selectedHour = ref("");
const selectedPatient = ref();
const availableHours = ref<string[]>([]);

const isLoading = ref(false);

onMounted(() => {
  loadServices();
  fetchPatients();
  loadDoctors();
  if (props.visible && props.appointmentId) {
    fetchAppointmentDetails(props.appointmentId);
  }
});

async function fetchAppointmentDetails(appointmentId: number) {
  isLoading.value = true;
  const res = await getById(appointmentId);
  selectedDoctor.value = res.doctorId;
  selectedService.value = res.serviceId;
  selectedDate.value = res.date;
  selectedHour.value = res.time;
  await fetchAvailableDates();
  await fetchAvailableHours();
  isLoading.value = false;
}

async function loadServices() {
  await getAllServices().then((res: any) => {
    services.value = res;
  });
}

async function fetchPatients() {
  await getAllPatients().then((res) => (patients.value = res));
}

async function loadDoctors() {
  await getAllDoctors().then((res: any) => {
    doctors.value = res.map((doctor: any) => ({ ...doctor, checked: true }));
  });
}

function closeModal() {
  selectedDate.value = "";
  selectedHour.value = "";
  selectedDoctor.value = "";
  selectedService.value = "";
  selectedPatient.value = "";
  emit("close");
}

async function fetchAvailableDates() {
  if (selectedDoctor.value && selectedService.value) {
    await getAvailableDates(selectedDoctor.value, selectedService.value).then(
      (res: any) => {
        availableDates.value = res;
      }
    );
  }
}

async function fetchAvailableHours() {
  if (selectedDoctor.value && selectedService.value && selectedDate.value) {
    await getAvailableHours(
      selectedDoctor.value,
      selectedService.value,
      selectedDate.value
    ).then((res: any) => {
      availableHours.value = res;
    });
  }
}

function addOrUpdateAppointment() {
  if (props.appointmentId) {
    rescheduleAppointment(props.appointmentId);
  } else {
    addAppointment();
  }
}

function addAppointment() {
  emit(
    "addAppointment",
    selectedDate.value,
    selectedHour.value,
    selectedDoctor.value,
    selectedService.value,
    selectedPatient.value
  );

  closeModal();

  selectedDate.value = "";
  selectedHour.value = "";
  selectedDoctor.value = "";
  selectedService.value = "";
  selectedPatient.value = "";
}

async function rescheduleAppointment(appointmentId: number) {
  try {
    await updateAppointment(
      appointmentId,
      selectedDate.value,
      selectedHour.value,
      selectedDoctor.value,
      selectedService.value
    );
    closeModal();
    Swal.fire({
      title: "Appointment updated successfully.",
      icon: "success",
      confirmButtonText: "OK",
    });
    emit("updateSuccessfully");
  } catch (error: any) {
    Swal.fire({
      title: "Error!",
      text: error.message,
      icon: "error",
      confirmButtonText: "OK",
    });
  }
}

watch(
  () => props.visible,
  (newVal) => {
    if (newVal && props.appointmentId) {
      fetchAppointmentDetails(props.appointmentId);
    }
  }
);
</script>

<template>
  <CustomModal
    :show="visible"
    @button2="closeModal"
    @button1="addOrUpdateAppointment"
    :title="props.appointmentId ? 'Edit Appointment' : 'New Appointment'"
  >
    <div class="selection" v-if="!isLoading">
      <div class="option">
        <label>Select doctor *</label>
        <select v-model="selectedDoctor">
          <option disabled value="">Doctor</option>
          <option v-for="doctor in doctors" :key="doctor.id" :value="doctor.id">
            Dr. {{ doctor.firstName }} {{ doctor.lastName }}
          </option>
        </select>
      </div>

      <div class="option">
        <label>Select type of treatment *</label>
        <select v-model="selectedService" @change="fetchAvailableDates">
          <option disabled value="">Type of treatment</option>
          <option
            v-for="service in services"
            :key="service.serviceId"
            :value="service.serviceId"
          >
            {{ service.name }}
          </option>
        </select>
      </div>

      <div class="option">
        <label>Select date *</label>
        <select v-model="selectedDate" @change="fetchAvailableHours">
          <option disabled value="">Date</option>
          <option v-for="date in availableDates" :key="date" :value="date">
            {{ date }}
          </option>
        </select>
      </div>

      <div class="option">
        <label>Select hour *</label>
        <select v-model="selectedHour">
          <option disabled value="">Hour</option>
          <option v-for="hour in availableHours" :key="hour" :value="hour">
            {{ hour }}
          </option>
        </select>
      </div>

      <div class="option" v-if="!appointmentId">
        <label>Select patient name</label>
        <select v-model="selectedPatient">
          <option disabled value="">Patient</option>
          <option
            v-for="patient in patients"
            :key="patient.customerId"
            :value="patient.customerId"
          >
            {{ patient.firstName }} {{ patient.lastName }}
          </option>
        </select>
      </div>
    </div>
    <LoadingSpinner v-else color="#84ce95" />
  </CustomModal>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.selection {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;

  .option {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 20px;

    label {
      color: @sugar;
      font-size: 18px;
    }

    select {
      width: 160px;
      background-color: @font-gray;
      border-radius: 10px;
      outline: none;
      color: @font-darker-gray;
      font-size: 15px;
      padding: 2px;

      &:focus {
        background-color: @sugar;
      }
    }
  }
}
</style>
