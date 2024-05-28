<script setup lang="ts">
import { AppointmentWeekly } from "@/data/types/Entities";
import { onMounted, ref, watch } from "vue";

const props = withDefaults(
  defineProps<{
    data: AppointmentWeekly[];
  }>(),
  {}
);

const chartData = ref(props.data);
const tooltipVisible = ref(false);
const tooltipText = ref("");
const tooltipStyle = ref({
  left: "0px",
  top: "0px",
});

const maxAppointments = ref(0);
const yAxisIntervals = 4;
const yAxisLabels = ref<number[]>([]);
const noData = ref<boolean>(false);

function updateYAxisLabels() {
  if (maxAppointments.value === 0) {
    yAxisLabels.value = [0];
    return;
  }
  if (maxAppointments.value < 4) {
    yAxisLabels.value = Array.from(
      { length: yAxisIntervals + 1 },
      (_, i) => (i * maxAppointments.value) / yAxisIntervals
    ).reverse();
  } else {
    const intervalSize = Math.ceil(maxAppointments.value / yAxisIntervals);
    yAxisLabels.value = Array.from(
      { length: yAxisIntervals + 1 },
      (_, i) => i * intervalSize
    ).reverse();
  }
}

function updateChartData() {
  chartData.value = props.data;
  const maxCount = Math.max(...props.data.map((item) => item.count), 0);
  maxAppointments.value = maxCount;
  noData.value = maxCount === 0;
  updateYAxisLabels();
}

onMounted(() => {
  updateChartData();
});

watch(
  () => props.data,
  () => {
    updateChartData();
  },
  { deep: true }
);

function showTooltip(event: MouseEvent, number: number) {
  tooltipText.value = number + " appointments";
  tooltipStyle.value = {
    left: `${event.clientX + 30}px`,
    top: `${event.clientY + 10}px`,
  };
  tooltipVisible.value = true;
}

function hideTooltip() {
  tooltipVisible.value = false;
}
</script>

<template>
  <div class="chart-container" v-if="!noData">
    <div class="y-axis">
      <div
        class="y-label"
        v-for="(n, index) in yAxisLabels"
        :key="n"
        :style="{ top: `${(index * 240) / yAxisIntervals - 7}px` }"
      >
        {{ n }}
      </div>
    </div>
    <div class="chart-area">
      <div
        class="horizontal-line"
        v-for="(n, index) in yAxisLabels"
        :key="n"
        :style="{ top: `${(index * 240) / yAxisIntervals}px` }"
      ></div>
      <div class="chart">
        <div
          class="bar"
          v-for="item in chartData"
          :key="item.week"
          @mousemove="showTooltip($event, item.count)"
          @mouseout="hideTooltip"
        >
          <div
            class="bar-inner"
            :style="{
              height: (item.count * 240) / maxAppointments + 'px',
            }"
          ></div>
          <span class="label">{{ item.week }}</span>
        </div>
      </div>
    </div>
    <div class="tooltip" v-show="tooltipVisible" :style="tooltipStyle">
      {{ tooltipText }}
    </div>
  </div>

  <div class="chart-container empty" v-else>
    <span>No appointments this month</span>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.chart-container {
  height: calc(50% - 60px);
  text-align: center;
  border: 1px solid @gray;
  border-radius: 20px;
  padding: 30px;
  display: flex;
  position: relative;

  .y-axis {
    position: relative;
    margin-right: 20px;

    .y-label {
      font-weight: bold;
      position: absolute;
      right: 0;
    }
  }

  .chart-area {
    width: 100%;
    display: flex;
    flex-direction: column;
    position: relative;
    height: 270px;

    .horizontal-line {
      position: absolute;
      width: 100%;
      border-top: 1px dashed @gray;
      z-index: 0;
    }

    .chart {
      display: flex;
      justify-content: space-around;
      align-items: flex-end;
      height: 100%;

      .bar {
        width: 15%;
        display: flex;
        flex-direction: column;
        align-items: center;

        .bar-inner {
          width: 100%;
          background-color: @blue;
          border-radius: 5px;
          z-index: 1;
        }

        .label {
          margin-top: 10px;
          font-weight: bold;
        }
      }
    }
  }

  .tooltip {
    position: fixed;
    background-color: rgba(0, 0, 0, 0.7);
    color: white;
    padding: 5px;
    border-radius: 3px;
    pointer-events: none;
    z-index: 10;
  }

  &.empty {
    align-items: center;
    justify-content: center;

    span {
      font-size: 20px;
      color: @gray;
    }
  }
}
</style>
