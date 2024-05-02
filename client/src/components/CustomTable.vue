//TODO: @wolflorena: Create a custom table compontent that can be used in
multiple views

<!-- <script setup lang="ts">
import { ref, onMounted, watch, computed } from "vue";

import {
  AppointmentAdmin,
} from "../data/types/Entities";

const props = withDefaults(
  defineProps<{
    dataArray: AppointmentAdmin[];
    headerTitles: string[];
  }>(),
  {}
);

type TableData = {
  id: number;
  firstProperty: string;
  secondProperty: string;
  thirdPropery: string;
  forthProperty: string;
};

const currentPage = ref(1);
const totalPages = ref(0);
const tableData = ref<TableData[]>([]);

function resolveType() {
  switch (props.dataArray[0].type) {
      case 'AppointmentAdmin':
        return props.dataArray.map((item: AppointmentAdmin) => ({
          firstProperty: item.customerFirstName,
          secondProperty: item.customerLastName,
          thirdProperty: item.doctorFirstName,
          fourthProperty: item.doctorLastName
        }));
      default:
        return []; 
    }
  }
  return [];
}


function formatTime(timeStr: string | undefined): string {
  if (timeStr) {
    const timeParts = timeStr.split(":");
    const date = new Date();
    date.setHours(
      parseInt(timeParts[0]),
      parseInt(timeParts[1]),
      parseInt(timeParts[2])
    );

    let hours = date.getHours();
    const minutes = date.getMinutes();

    const amPm = hours >= 12 ? "PM" : "AM";

    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'

    const formattedMinutes = minutes < 10 ? `0${minutes}` : minutes;
    return `${hours}:${formattedMinutes} ${amPm}`;
  }
  return "";
}

function formatDateForTable(dateStr: string): string {
  const monthsOfYear = [
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sept",
    "Oct",
    "Nov",
    "Dec",
  ];
  if (dateStr) {
    const date = new Date(dateStr);
    const dayOfMonth = date.getDate();
    const year = date.getFullYear();
    const month = monthsOfYear[date.getMonth()];

    return `${dayOfMonth} ${month} ${year}`;
  }
  return "";
}
</script>

<template>
  <div
    class="appointments-container"
    :class="{ empty: dataArray.length === 0 }"
  >
    <table v-if="dataArray.length > 0">
      <thead>
        <tr>
          <th v-for="title in headerTitles">{{ headerTitles }}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(element, index) in tableData">
          <td>{{ 10 * (currentPage - 1) + index + 1 }}</td>
          <td>
            {{ element.firstProperty }}
          </td>
          <td>
            <div class="date">
              <span id="time">{{ element.secondProperty }} </span>
              <span id="date">
                {{ element.thirdPropery }}
              </span>
            </div>
          </td>
          <td>
            {{ element.forthProperty }}
          </td>
          <td>
            <div class="actions" v-if="appointmentStatus === 'SCHEDULED'">
              <button @click="showInfoModal(element.appointmentId)">
                <font-awesome-icon icon="eye" id="icon" />
              </button>
              <button>
                <font-awesome-icon icon="pen" id="icon" />
              </button>
              <button @click="showDeleteModal(element.appointmentId)">
                <font-awesome-icon icon="trash-can" id="icon" />
              </button>
            </div>
            <div class="actions" v-if="appointmentStatus === 'REQUESTED'">
              <button
                @click="
                  updateAppointmentStatus(
                    appointment.appointmentId,
                    'SCHEDULED'
                  )
                "
              >
                <font-awesome-icon icon="check" id="icon" />
              </button>
              <button>
                <font-awesome-icon icon="pen" id="icon" />
              </button>
              <button
                @click="
                  updateAppointmentStatus(
                    appointment.appointmentId,
                    'CANCELLED'
                  )
                "
              >
                <font-awesome-icon icon="xmark" id="icon" />
              </button>
            </div>
            <span
              class="status"
              v-if="appointmentStatus === 'COMPLETED'"
              :class="{ canceled: appointment.status === 'CANCELLED' }"
            >
              {{ appointment.status }}
            </span>
          </td>
        </tr>
      </tbody>
    </table>
    <img src="../../assets/nodata.svg" alt="" v-else />

    <Pagination
      :total-pages="totalPages"
      :current-page="currentPage"
      @change-page="changePage"
    />
  </div>
</template> -->
