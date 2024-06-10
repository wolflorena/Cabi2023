<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import type { Doctor, Service, UserDetails } from "@/data/types/Entities";
import {
  getAvailableDates,
  getAvailableHours,
} from "@/services/doctor_service";
import { getAllServices } from "@/services/service_service";
import CustomModal from "./CustomModal.vue";
import {
  addDays,
  endOfWeek,
  format,
  formatISO,
  isWeekend,
  parseISO,
  startOfWeek,
} from "date-fns";
import { SwalLoading } from "@/utils/helpers";

const props = withDefaults(
  defineProps<{
    visible: boolean;
    variant?: string;
    selectedDoctor: Doctor | null;
    selectedDate: Date;
    userDetails: UserDetails | null;
  }>(),
  {
    visible: false,
    variant: "DAY",
  }
);

const emit = defineEmits(["close", "addAppointment"]);

const modalTitle = `New appointment for Dr. ${props.selectedDoctor?.firstName} ${props.selectedDoctor?.lastName}`;
const services = ref<Service[]>([]);

const selectedService = ref();
const selectedDate = ref();
const selectedHour = ref();
const availableHours = ref<string[]>([]);
const availableDates = ref<string[]>([]);

onMounted(() => {
  loadServices();
});

async function loadServices() {
  await getAllServices().then((res: any) => {
    services.value = res;
  });
}

const availableWeekDates = computed(() => {
  const startOfWeekDate = startOfWeek(props.selectedDate, { weekStartsOn: 1 });
  const endOfWeekDate = addDays(startOfWeekDate, 4);

  return availableDates.value.filter((date) => {
    const parsedDate = parseISO(date);
    return parsedDate >= startOfWeekDate && parsedDate <= endOfWeekDate;
  });
});

function closeModal() {
  selectedDate.value = "";
  selectedHour.value = "";
  selectedService.value = "";
  availableHours.value = [];
  emit("close");
}

async function fetchData() {
  if (props.variant === "DAY") {
    if (props.selectedDoctor && props.selectedDate) {
      SwalLoading.fire();
      await getAvailableHours(
        props.selectedDoctor.id,
        selectedService.value,
        formatISO(props.selectedDate).split("T")[0]
      ).then((res: any) => {
        SwalLoading.close();
        availableHours.value = res;
      });
    }
  } else if (props.variant === "WEEK") {
    if (props.selectedDoctor && props.selectedDate) {
      SwalLoading.fire();
      await getAvailableDates(
        props.selectedDoctor.id,
        selectedService.value
      ).then((res: any) => {
        availableDates.value = res;
        selectedDate.value = availableDates.value[0];
      });
    }
  }
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
  availableDates.value = [];
}

watch(
  () => selectedDate.value,
  async () => {
    if (props.selectedDoctor && selectedDate.value !== "") {
      await getAvailableHours(
        props.selectedDoctor.id,
        selectedService.value,
        selectedDate.value
      ).then((res: any) => {
        availableHours.value = res;
        SwalLoading.close();
      });
    }
  }
);

const hoursFetched = computed(() => {
  if (
    props.variant === "WEEK" &&
    (availableHours.value.length <= 0 || availableWeekDates.value.length <= 0)
  ) {
    return true;
  }
  return false;
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
        <select @change="fetchData" v-model="selectedService">
          <option
            v-for="service in services"
            :key="service.serviceId"
            :value="service.serviceId"
          >
            {{ service.name }}
          </option>
        </select>
      </div>
      <div class="option" v-if="variant === 'WEEK'">
        <label>Select date *</label>
        <select v-model="selectedDate">
          <option v-for="date in availableWeekDates" :key="date" :value="date">
            {{ format(date, "EEEE, MMMM d, yyyy") }}
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
  height: 15vh;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
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
