<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import type { Doctor, Service, UserDetails } from "@/data/types/Entities";
import { getAvailableHours } from "@/services/doctor_service";
import { getAllServices } from "@/services/service_service";
import CustomModal from "./CustomModal.vue";
import { formatISO, parseISO } from "date-fns";

const props = withDefaults(
  defineProps<{
    visible: boolean;
    selectedDoctor: Doctor | null;
    selectedDate: Date;
    userDetails: UserDetails | null;
  }>(),
  {
    visible: false,
  }
);

const emit = defineEmits(["close", "addAppointment"]);

const modalTitle = `New appointment for Dr. ${props.selectedDoctor?.firstName} ${props.selectedDoctor?.lastName}`;
const services = ref<Service[]>([]);

const selectedService = ref();
const selectedDate = ref(formatISO(props.selectedDate).split("T")[0]);
const selectedHour = ref();
const availableHours = ref<string[]>([]);

onMounted(() => {
  loadServices();
});

async function loadServices() {
  await getAllServices().then((res: any) => {
    services.value = res;
  });
}

function closeModal() {
  selectedDate.value = "";
  selectedHour.value = "";
  selectedService.value = "";
  availableHours.value = [];
  emit("close");
}

async function fetchAvailableHours() {
  if (props.selectedDoctor && props.selectedDate)
    await getAvailableHours(
      props.selectedDoctor.id,
      selectedService.value,
      formatISO(props.selectedDate).split("T")[0]
    ).then((res: any) => {
      availableHours.value = res;
      console.log(res);
    });
}

function addAppointment() {
  emit(
    "addAppointment",
    selectedDate.value,
    selectedHour.value,
    props.selectedDoctor?.id,
    selectedService.value
  );

  selectedDate.value = "";
  selectedHour.value = "";
  selectedService.value = "";
  availableHours.value = [];
}

const hoursFetched = computed(() => {
  return !(availableHours.value.length > 0);
});
</script>

<template>
  <CustomModal
    :show="visible"
    @button2="closeModal"
    @button1="addAppointment"
    :title="modalTitle"
    variant="LIGHT"
  >
    <div class="selection">
      <div class="option">
        <label>Select type of treatment *</label>
        <select @change="fetchAvailableHours" v-model="selectedService">
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
        <label>Select Hour *</label>
        <select :disabled="hoursFetched" v-model="selectedHour">
          <option v-for="hour in availableHours" :key="hour" :value="hour">
            {{ hour }}
          </option>
        </select>
      </div>
    </div>
  </CustomModal>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.selection {
  width: 40vw;
  height: 20vh;
  display: flex;
  flex-direction: column;
  gap: 10px;

  .option {
    width: 100%;
    display: flex;
    color: @sugar;
    justify-content: space-between;
    align-items: center;
    gap: 20px;

    label {
      color: @smoke;
      font-size: 20px;
    }

    select {
      width: 300px;
      background-color: @heavy-smoke;
      border-radius: 10px;
      outline: none;
      color: @white;
      font-size: 15px;
      padding: 2px;

      &:focus {
        background-color: @smoke;
      }
    }
  }
}
</style>
