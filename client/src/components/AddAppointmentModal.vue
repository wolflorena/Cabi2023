<script setup lang="ts">
import { onMounted, ref } from "vue";
import type { AppointmentAdmin, AppointmentDetail, Patient, SelectedDoctor, Service } from "@/data/types/Entities";
import {
  getAllDoctors,
  getAvailableDates,
  getAvailableHours,
} from "@/services/doctor_service";
import { getAllServices } from "@/services/service_service";
import { getAllPatients } from "@/services/customer_service";
import CustomModal from "./CustomModal.vue";

const props = withDefaults(
  defineProps<{
    visible: boolean;
    appointment?: AppointmentAdmin;
  }>(),
  {
    visible: false,
  }
);

const emit = defineEmits(["close", "addAppointment", "editAppointment"]);

const doctors = ref<SelectedDoctor[]>([]);

const patients = ref<Patient[]>([]);
const services = ref<Service[]>([]);

const selectedDoctor = ref();
const selectedService = ref();
const availableDates = ref<string[]>([]);
const selectedDate = ref();
const selectedHour = ref();
const selectedPatient = ref();
const availableHours = ref<string[]>([]);

onMounted(() => {
  loadServices();
  fetchPatients();
  loadDoctors();
  if (props.appointment) {
    prefillAppointmentData();
  }
});

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

function prefillAppointmentData() {
  if (props.appointment) {
    selectedDoctor.value = props.appointment.doctorId;
    selectedService.value = props.appointment.serviceId;
    selectedDate.value = props.appointment.date;
    selectedHour.value = props.appointment.time;
    selectedPatient.value = props.appointment.customerId;

    fetchAvailableDates();
    fetchAvailableHours();
  }
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
  await getAvailableDates(selectedDoctor.value, selectedService.value).then(
    (res: any) => {
      availableDates.value = res;
    }
  );
}

async function fetchAvailableHours() {
  await getAvailableHours(
    selectedDoctor.value,
    selectedService.value,
    selectedDate.value
  ).then((res: any) => {
    availableHours.value = res;
  });
}

function addAppointment() {
  closeModal();

  emit(
    "addAppointment",
    selectedDate.value,
    selectedHour.value,
    selectedDoctor.value,
    selectedService.value,
    selectedPatient.value
  );

  selectedDate.value = "";
  selectedHour.value = "";
  selectedDoctor.value = "";
  selectedService.value = "";
  selectedPatient.value = "";
}
</script>

<template>
  <CustomModal
    :show="visible"
    @button2="closeModal"
    @button1="addAppointment"
    title="New appointment"
  >
    <div class="selection">
      <div class="option">
        <label>Select doctor *</label>
        <select v-model="selectedDoctor">
          <option disabled value="">Select doctor</option>
          <option v-for="doctor in doctors" :key="doctor.id" :value="doctor.id">
            Dr. {{ doctor.firstName }} {{ doctor.lastName }}
          </option>
        </select>
      </div>

      <div class="option">
        <label>Select type of treatment *</label>
        <select v-model="selectedService" @change="fetchAvailableDates">
          <option disabled value="">Select type of treatment</option>
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
          <option disabled value="">Select date</option>
          <option v-for="date in availableDates" :key="date" :value="date">
            {{ date }}
          </option>
        </select>
      </div>

      <div class="option">
        <label>Select hour *</label>
        <select v-model="selectedHour">
          <option disabled value="">Select hour</option>
          <option v-for="hour in availableHours" :key="hour" :value="hour">
            {{ hour }}
          </option>
        </select>
      </div>

      <div class="option">
        <label>Select patient name</label>
        <select v-model="selectedPatient">
          <option disabled value="">Select patient</option>
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
